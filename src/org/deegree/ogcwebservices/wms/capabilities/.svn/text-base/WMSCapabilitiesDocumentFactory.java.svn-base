//$HeadURL$
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
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

package org.deegree.ogcwebservices.wms.capabilities;

import java.io.IOException;
import java.net.URL;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class WMSCapabilitiesDocumentFactory {

    private static NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();

    private static final ILogger LOG = LoggerFactory.getLogger( WMSCapabilitiesDocumentFactory.class );

    /**
     * 
     * @param url
     * @return loaded version depending WMSCapabilitiesDocument
     * @throws SAXException
     * @throws IOException
     * @throws XMLParsingException
     */
    public static WMSCapabilitiesDocument getWMSCapabilitiesDocument( URL url )
                            throws IOException, SAXException, XMLParsingException {
        LOG.logDebug( "WMS capabilities URL: ", url );
        XMLFragment xml = new XMLFragment( url );

        return getWMSCapabilitiesDocument( xml.getRootElement() );
    }

    /**
     * 
     * @param root
     * @return the document object
     * @throws XMLParsingException
     */
    public static WMSCapabilitiesDocument getWMSCapabilitiesDocument( Element root )
                            throws XMLParsingException {
        WMSCapabilitiesDocument caps = null;
        String version = XMLTools.getRequiredNodeAsString( root, "./@version", nsContext );
        if ( "1.1.0".equals( version ) || "1.1.1".equals( version ) ) {
            caps = new WMSCapabilitiesDocument();
        } else if ( "1.3.0".equals( version ) || "1.3".equals( version ) ) {
            caps = new WMSCapabilitiesDocument_1_3_0();
        } else if ( "1.0.0".equals( version ) ) {
            caps = new WMSCapabilitiesDocument_1_0_0();
        } else {
            // TODO
            // for now WMS 1.1.1 will be use das default; a correct implementation should
            // realize a version negotiation
            caps = new WMSCapabilitiesDocument();
        }
        caps.setRootElement( root );
        return caps;
    }

}
