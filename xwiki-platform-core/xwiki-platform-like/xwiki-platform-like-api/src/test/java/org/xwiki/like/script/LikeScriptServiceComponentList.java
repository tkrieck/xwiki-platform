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
package org.xwiki.like.script;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.xwiki.like.internal.DefaultLikeConfiguration;
import org.xwiki.like.internal.DefaultLikeManager;
import org.xwiki.like.internal.LikeConfigurationSource;
import org.xwiki.like.internal.LikeManagerCacheHelper;
import org.xwiki.ratings.internal.DefaultRatingsConfiguration;
import org.xwiki.ratings.internal.DefaultRatingsManagerFactory;
import org.xwiki.ratings.internal.RatingsConfigurationSource;
import org.xwiki.test.annotation.ComponentList;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Pack of default Component implementations that are needed for {@link LikeScriptService}.
 *
 * @version $Id$
 * @since 13.9RC1
 * @since 13.4.4
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
@ComponentList({
    LikeScriptService.class,
    LikeManagerCacheHelper.class,
    DefaultLikeManager.class,
    DefaultRatingsManagerFactory.class,
    DefaultLikeConfiguration.class,
    LikeConfigurationSource.class,
    // Ratings dependencies.
    DefaultRatingsConfiguration.class,
    RatingsConfigurationSource.class,
})
@Inherited
public @interface LikeScriptServiceComponentList
{
}
