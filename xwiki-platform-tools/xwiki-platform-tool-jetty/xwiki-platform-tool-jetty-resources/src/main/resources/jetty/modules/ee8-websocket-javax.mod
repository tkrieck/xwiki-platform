# ---------------------------------------------------------------------------
# See the NOTICE file distributed with this work for additional
# information regarding copyright ownership.
#
# This is free software; you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation; either version 2.1 of
# the License, or (at your option) any later version.
#
# This software is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this software; if not, write to the Free
# Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
# 02110-1301 USA, or see the FSF site: http://www.fsf.org.
# ---------------------------------------------------------------------------

# DO NOT EDIT - See: https://jetty.org/docs/index.html

[description]
Enable javax.websocket APIs for deployed web applications.

[environment]
ee8

[tags]
websocket

[depend]
client
ee8-annotations

[lib]
lib/jetty-websocket-core-common-${jetty.version}.jar
lib/jetty-websocket-core-client-${jetty.version}.jar
lib/jetty-websocket-core-server-${jetty.version}.jar
lib/ee8-websocket/jetty-ee8-websocket-servlet-${jetty.version}.jar
lib/ee8-websocket/jetty-javax-websocket-api-1.1.2.jar
lib/ee8-websocket/jetty-ee8-websocket-javax-client-${jetty.version}.jar
lib/ee8-websocket/jetty-ee8-websocket-javax-common-${jetty.version}.jar
lib/ee8-websocket/jetty-ee8-websocket-javax-server-${jetty.version}.jar