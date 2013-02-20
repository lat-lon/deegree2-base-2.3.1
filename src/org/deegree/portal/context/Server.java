//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/context/Server.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53115 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 
 ---------------------------------------------------------------------------*/
package org.deegree.portal.context;

import java.net.URL;

import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;

/**
 * encapsulates the server description as defined in the OGC Web Map Context specification 1.0.0
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10906 $, $Date: 2008-04-03 09:50:02 +0200 (Thu, 03 Apr 2008) $
 */
public class Server {
    private String service = null;

    private String title = null;

    private String version = null;

    private URL onlineResource = null;

    private OGCCapabilities capabilities = null;

    /**
     * Creates a new Server object.
     * 
     * @param title
     *            the title of the service
     * @param version
     *            Version number of the OGC interface specification which corresponds to the service
     * @param service
     *            the type of the service according to OGC interfaces, such as WMS, WFS.
     * @param onlineResource
     *            the link to the online resource
     * @param capabilities
     * 
     * @throws ContextException
     */
    public Server( String title, String version, String service, URL onlineResource, OGCCapabilities capabilities )
                            throws ContextException {
        setTitle( title );
        setVersion( version );
        setService( service );
        setOnlineResource( onlineResource );
        setCapabilities( capabilities );
    }

    /**
     * the title of the service (extracted from the Capabilities by the Context document creator)
     * 
     * @return the title of the service (extracted from the Capabilities by the Context document
     *         creator)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Version number of the OGC interface specification which corresponds to the service
     * 
     * @return Version number of the OGC interface specification which corresponds to the service
     */
    public String getVersion() {
        return version;
    }

    /**
     * the type of the service according to OGC interfaces, such as WMS, WFS.
     * 
     * @return the type of the service according to OGC interfaces, such as WMS, WFS.
     */
    public String getService() {
        return service;
    }

    /**
     * link to the online resource
     * 
     * @return link to the online resource
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * see also org.deegree.clients.context.Server#getTitle()
     * 
     * @param title
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * see also org.deegree.clients.context.Server#getFeatureVersion()
     * 
     * @param version
     * 
     * @throws ContextException
     */
    public void setVersion( String version )
                            throws ContextException {
        if ( version == null ) {
            throw new ContextException( "version isn't allowed to be null" );
        }
        this.version = version;
    }

    /**
     * see also org.deegree.clients.context.Server#getService()
     * 
     * @param service
     * 
     * @throws ContextException
     */
    public void setService( String service )
                            throws ContextException {
        if ( service == null ) {
            throw new ContextException( "service isn't allowed to be null" );
        }
        this.service = service;
    }

    /**
     * see also org.deegree.clients.context.Server#getOnlineResource()
     * 
     * @param onlineResource
     * 
     * @throws ContextException
     */
    public void setOnlineResource( URL onlineResource )
                            throws ContextException {
        if ( onlineResource == null ) {
            throw new ContextException( "onlineResource isn't allowed to be null" );
        }
        this.onlineResource = onlineResource;
    }

    /**
     * returns the capabilities of the encapsulated server
     * 
     * @return the capabilities of the encapsulated server
     */
    public OGCCapabilities getCapabilities() {
        return capabilities;
    }

    /**
     * @see #getCapabilities()
     * @param capabilities
     */
    public void setCapabilities( OGCCapabilities capabilities ) {
        this.capabilities = capabilities;
    }

    @Override
    public boolean equals( Object other ) {
        if ( other == null || !( other instanceof Server ) ) {
            return false;
        }
        Server os = (Server) other;
        return os.getOnlineResource().equals( getOnlineResource() ) && os.getService().equals( getService() )
               && os.getVersion().equals( getVersion() );
    }

}