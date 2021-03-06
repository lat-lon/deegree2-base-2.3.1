//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/sld/RemoteOWS.java $
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
package org.deegree.graphics.sld;

import static org.deegree.framework.xml.XMLTools.escape;

import java.net.URL;

import org.deegree.framework.util.NetWorker;
import org.deegree.framework.xml.Marshallable;

/**
 * Since a layer is defined as a collection of potentially mixed-type features, the UserLayer
 * element must provide the means to identify the features to be used. All features to be rendered
 * are assumed to be fetched from a Web Feature Server (WFS) or a Web Coverage CapabilitiesService
 * (WCS, in which case the term features is used loosely).
 * <p>
 * </p>
 * The remote server to be used is identified by RemoteOWS (OGC Web CapabilitiesService) element.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * @version $Revision: 12168 $ $Date: 2008-06-04 16:08:42 +0200 (Mi, 04. Jun 2008) $
 */
public class RemoteOWS implements Marshallable {

    /**
     * WFS
     */
    final static public String WFS = "WFS";

    /**
     * WCS
     */
    final static public String WCS = "WCS";

    private String service = null;

    private URL onlineResource = null;

    /**
     * Creates a new RemoteOWS object.
     * 
     * @param service
     * @param onlineResource
     */
    RemoteOWS( String service, URL onlineResource ) {
        setService( service );
        setOnlineResource( onlineResource );
    }

    /**
     * type of service that is represented by the remote ows. at the moment <tt>WFS</tt> and
     * <tt>WCS</tt> are possible values.
     * 
     * @return the type of the services
     * 
     */
    public String getService() {
        return service;
    }

    /**
     * @see RemoteOWS#getService()
     * @param service
     *            the type of the services
     * 
     */
    public void setService( String service ) {
        this.service = service;
    }

    /**
     * address of the the ows as URL
     * 
     * @return the adress of the ows as URL
     * 
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * @see RemoteOWS#getOnlineResource()
     * @param onlineResource
     *            the adress of the ows as URL
     * 
     */
    public void setOnlineResource( URL onlineResource ) {
        this.onlineResource = onlineResource;
    }

    /**
     * exports the content of the RemoteOWS as XML formated String
     * 
     * @return xml representation of the RemoteOWS
     */
    public String exportAsXML() {

        StringBuffer sb = new StringBuffer( 200 );
        sb.append( "<RemoteOWS>" );
        sb.append( "<Service>" ).append( escape( service ) ).append( "</Service>" );
        sb.append( "<OnlineResource xmlns:xlink='http://www.w3.org/1999/xlink' " );
        sb.append( "xlink:type='simple' xlink:href='" );
        sb.append( escape( NetWorker.url2String( onlineResource ) ) + "'/>" );
        sb.append( "</RemoteOWS>" );

        return sb.toString();
    }
}