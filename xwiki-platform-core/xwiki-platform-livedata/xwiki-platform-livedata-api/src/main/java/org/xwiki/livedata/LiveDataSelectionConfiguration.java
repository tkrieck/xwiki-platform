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
package org.xwiki.livedata;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Describes the live data selection configuration.
 *
 * @version $Id$
 * @since 12.10.1
 * @since 13.0
 */
public class LiveDataSelectionConfiguration implements InitializableLiveDataElement
{
    /**
     * Specified whether live data entry selection is enabled.
     */
    private boolean enabled;

    /**
     * @return {@code true} if live data entry selection is enabled, {@code false} otherwise
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * Sets whether live data entry selection is enabled.
     *
     * @param enabled {@code true} to enable the live data entry selection, {@code false} to disable it
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * @since 17.4.0RC1
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LiveDataSelectionConfiguration that = (LiveDataSelectionConfiguration) o;

        return new EqualsBuilder()
            .append(this.enabled, that.enabled)
            .isEquals();
    }

    /**
     * @since 17.4.0RC1
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
            .append(this.enabled)
            .toHashCode();
    }
}
