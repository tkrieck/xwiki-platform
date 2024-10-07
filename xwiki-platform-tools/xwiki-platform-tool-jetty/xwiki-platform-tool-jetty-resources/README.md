XWiki Jetty Configuration
=========================

These instructions are useful when upgrading the Jetty version used.

We brought the following changes from the default Jetty files obtained from the Jetty zip file (in `jetty-home`):

1. Addition of XWiki license headers to all files
1. Addition of `modules/xwiki.mod`, to group all modules we depend on.
1. Addition of `start.d/xwiki.ini` to configure the following properties:
   1. Disable WAR scanning/hot deployment (since we use static deployment, and it speeds up 
      Jetty) by changing the default values for:
      ```
      jetty.deploy.scanInterval=0
      jetty.deploy.extractWars=false
      ```
   1. Configure Jetty to use RFC3986 for URLs + allow for ambiguous elements in the URLs as XWiki currently needs 
      them (see the doc in start.d/xwiki.ini).
      ```
      jetty.httpConfig.uriCompliance=RFC3986,AMBIGUOUS_PATH_ENCODING,AMBIGUOUS_EMPTY_SEGMENT,AMBIGUOUS_PATH_SEPARATOR
      ``` 
1. Addition of `etc/jetty-xwiki.xml` to print a message in the console when XWiki is started.
1. Remove support for JSP (since XWiki doesn't use JSPs) by:
   1. Removing the following from `etc/webdefault-ee8.xml`:
      ```
      <servlet id="jsp">
      ...
      </servlet>
      ```
      Also remove the `<servlet-mapping>` just below it.
      Under `<welcome-file-list>` alors remove the `<welcome-file>index.jsp</welcome-file>` line.
   1. Keep only the `apache-el-<version>` lib in `modules/ee8-apache-jsp.mod` (i.e. remove the JSP lib references).
      We need the EL lib for Hibernate Validator (see XWIKI-19314)
1. Remove alpn (we don't need TLS/SSL for a demo packaging) and http2 support by:
   1. Remove `lib/jetty-alpn-client-${jetty.version}.jar` from `modules/client.mod`
   1. Remove references to the `alpn` and `http2` modules from `modules/https.mod`
1. Addition of `modules/xwiki-logging.mod` to configure logging for XWiki (provides the Jetty `logging` module name)
1. Modification of `etc/console-capture.xml` to send logs to both the console and files. Namely, we wrap:
   ```
   <Arg>
     <New class="org.eclipse.jetty.util.RolloverFileOutputStream">
     ...
   </Arg>
   ```
   With:
   ```
   <Arg>
     <!-- XWiki wants to log to both the console and also to file, and thus we're wrapping the RolloverFileOutputStream
          class with a TeeOutputStream to split the logs. -->
     <New class="org.apache.commons.io.output.TeeOutputStream">
       ...
       <!-- Since we can't send to both out and err, we choose to send to out since logs are not errors by default,
            so it seems the most logical one to use -->
       <Arg><Get class="java.lang.System" name="out"/></Arg>
     </New>
    </Arg>
   ``` 
1. Note that we don't include all `etc/*.xml` files nor all `modules/*.mod` files since we don't use these extra
    features.  
