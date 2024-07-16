/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
(function() {
  'use strict';
  var $ = jQuery;
  
  // Declare the configuration namespace.
  CKEDITOR.config['xwiki-realtime'] = CKEDITOR.config['xwiki-realtime'] || {
    __namespace: true
  };

  CKEDITOR.plugins.add('xwiki-realtime', {
    requires: 'notification',

    init : function(editor) {
      applyStyleSheets(editor);

      // CKEditor's HTML parser doesn't preserve the space character typed at the end of a line of text. For instance,
      // the following:
      //   CKEDITOR.htmlParser.fragment.fromHtml('<p>x </p>')
      // is parsed as:
      //   <p>x</p>
      // Normally browsers should insert a non-breaking space when the user types a space at the end of a line. Chrome
      // behaves like this. Firefox inserts a normal space character instead. We need to fix this otherwise that space
      // character is lost when the content is saved or when the user switches to Source. This is especially critical
      // for the realtime editing where the content you type is merged with the content typed by other users and fed
      // back to your editor. So if you type at the end of a paragraph while remote changes are received you don't want
      // the space between the words you type to be lost.
      preserveSpaceCharAtTheEndOfLine(editor);

      require([
        'xwiki-realtime-loader',
        'xwiki-ckeditor-realtime-adapter'
      ], (Loader, Adapter) => {
        enableRealtimeEditing(editor, Loader, Adapter).then(() => {
          // The edited (HTML) content is normalized when the realtime editing is enabled (e.g. by adding some BR
          // elements to ensure the HTML is the same across different browsers) which makes the editor dirty, although
          // there aren't any real content changes (that would be noticed in the source wiki syntax). We reset the dirty
          // state in order to avoid getting the leave confirmation when leaving the editor just after it was loaded.
          editor.resetDirty();
        });
      });
    }
  });

  function applyStyleSheets(editor) {
    const styleSheets = editor.config['xwiki-realtime'].stylesheets;

    // Add the stylesheet to the page where the editor is used (e.g. for the realtime toolbar).
    addStyleSheets(styleSheets, document);

    // Add the stylesheet also to the edited content, in case the edited content is inside an iframe, for the user
    // caret indicators. We have to do this each time the iframe is reloaded (e.g. after a macro is inserted).
    // Note that we can't simply use the editor#addContentsCss method because we have the fullPage configuration on.
    editor.on('contentDom', () => {
      addStyleSheets(styleSheets, editor.document.$);
    });
    if (editor.document && editor.document.$ !== document) {
      // Initial content load.
      addStyleSheets(styleSheets, editor.document.$);
    }
  }

  function addStyleSheets(urls, doc) {
    urls.forEach(url => {
      $('<link/>', doc).attr({
        type: 'text/css',
        rel: 'stylesheet',
        href: url
      }).appendTo(doc.head);
    });
  }

  function preserveSpaceCharAtTheEndOfLine(editor) {
    editor.on('beforeGetData', () => {
      const range = editor.getSelection()?.getRanges()[0];
      const textNode = range?.startContainer;
      // Check if the caret is at the end of a text node that ends with a space and is followed by a line break.
      if (editor.mode === 'wysiwyg' && range?.collapsed && textNode.type === CKEDITOR.NODE_TEXT &&
          range.startOffset === textNode.getLength() && textNode.getText().endsWith(' ') &&
          isFollowedByLineBreak(textNode)) {
        fixTrailingSpace(textNode);
        // Make sure the caret remains at the end of the text node after we modify its data.
        range.setStart(textNode, textNode.getLength());
        range.select();
      }
    // Note that we use a high priority (0) to ensure that our listener is called before the HTML parser.
    }, null, null, 0);
  }

  function isFollowedByLineBreak(textNode) {
    // Is directly inside a block element...
    return CKEDITOR.dtd.$block[textNode.getParent().getName()] &&
      // ...and is either the last child or followed by a block element or a BR element.
      (!textNode.getNext() || CKEDITOR.dtd.$block[textNode.getNext().getName?.()] ||
        textNode.getNext().getName?.() === 'br');
  }

  function fixTrailingSpace(textNode) {
    // We replicate here the behavior of Chrome when multiple space characters are typed.
    const whitespaceLength = textNode.getLength() - textNode.getText().trimEnd().length;
    const whitespaceSuffix = '\u00A0 '.repeat((whitespaceLength - 1) / 2) +
      '\u00A0'.repeat((whitespaceLength - 1) % 2 + 1);
    textNode.$.replaceData(textNode.getLength() - whitespaceLength, whitespaceLength, whitespaceSuffix);
  }

  function enableRealtimeEditing(editor, Loader, Adapter) {
    const info = {
      type: 'wysiwyg',
      field: editor.name,
      // This is used to generate the WYSIWYG editor URL so we need to take into account if we are in view mode
      // (i.e. in-place editing) or in edit mode (standalone editing).
      href: window.XWiki?.contextaction === 'view' ? '&force=1' : '&editor=wysiwyg&force=1',
      name: 'WYSIWYG',
      compatible: ['wysiwyg', 'wiki']
    };

    return Loader.bootstrap(info).then(realtimeContext => {
      return new Promise((resolve, reject) => {
        require(['xwiki-realtime-wysiwyg'], RealtimeWysiwygEditor => {
          editor._realtime = new RealtimeWysiwygEditor(new Adapter(editor, CKEDITOR), realtimeContext);
  
          if (realtimeContext.realtimeEnabled) {
            editor.ui.space('top').$.querySelector('.cke_button__source')?.remove();
          } else {
            // When someone is offline, they may have left their tab open for a long time and the lock may have
            // disappeared. We're refreshing it when the editor is focused so that other users will know that someone is
            // editing the document.
            editor.on('focus', () => {
              editor._realtime.lockDocument();
            });
          }

          resolve(editor._realtime);
        }, reject);
      });
    });
  }
})();