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
package org.xwiki.captcha.script.internal;

import java.util.Map;

import org.slf4j.Logger;
import org.xwiki.captcha.Captcha;
import org.xwiki.script.wrap.AbstractWrappingObject;

/**
 * Wrap a {@link Captcha} implementation for script API access.
 *
 * @version $Id$
 * @since 16.2.0RC1
 */
public class WrappedScriptCaptcha extends AbstractWrappingObject<Captcha> implements Captcha
{
    private Logger logger;

    /**
     * @param captcha the {@link Captcha} to wrap for Script API access
     * @param logger the logging component
     */
    public WrappedScriptCaptcha(Captcha captcha, Logger logger)
    {
        super(captcha);
        this.logger = logger;
    }

    protected Logger getLogger()
    {
        return this.logger;
    }

    /**
     * @return the displayed wrapped CAPTCHA or {@code null} in case of an exception
     * @see Captcha#display()
     */
    @Override
    public String display()
    {
        return display(null);
    }

    /**
     * @param parameters the CAPTCHA override parameters
     * @return the displayed wrapped CAPTCHA or {@code null} in case of an exception
     * @see Captcha#display(Map)
     */
    @Override
    public String display(Map<String, Object> parameters)
    {
        try {
            return getWrapped().display(parameters);
        } catch (Exception e) {
            getLogger().error("Failed to display captcha", e);
        }

        return null;
    }

    /**
     * @return {@code true} if the answer is valid; {@code false} otherwise or if an exception occurs
     * @see Captcha#isValid()
     */
    @Override
    public boolean isValid()
    {
        return isValid(null);
    }

    /**
     * @param parameters the CAPTCHA override parameters
     * @return {@code true} if the answer is valid; {@code false} otherwise or if an exception occurs
     * @see Captcha#isValid(Map)
     */
    @Override
    public boolean isValid(Map<String, Object> parameters)
    {
        try {
            return getWrapped().isValid(parameters);
        } catch (Exception e) {
            getLogger().error("Failed to validate captcha", e);
        }

        return false;
    }
}
