//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wmps/operation/PrintMapResponseDocument.java $
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
 Aennchenstraße 19
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

package org.deegree.ogcwebservices.wmps.operation;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.DefaultOGCWebServiceResponse;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Represents an Initial Response document for a WMPS 1.1.0 compliant web service.
 * 
 * @author <a href="mailto:deshmukh@lat-lon.de">Anup Deshmukh</a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9348 $, $Date: 2007-12-27 17:59:14 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */

public class PrintMapResponseDocument extends DefaultOGCWebServiceResponse {

    private static final ILogger LOG = LoggerFactory.getLogger( PrintMapResponseDocument.class );

    protected static final URI WMPSNS = CommonNamespaces.WMPSNS;

    private static final String XML_TEMPLATE = "WMPSInitialResponseTemplate.xml";

    private Element root;

    /**
     * @param request
     */
    public PrintMapResponseDocument( OGCWebServiceRequest request ) {
        super( request );
    }

    /**
     * Creates a skeleton response document that contains the mandatory elements only.
     * 
     * @throws IOException
     * @throws SAXException
     */
    public void createEmptyDocument()
                            throws IOException, SAXException {
        
        URL url = PrintMapResponseDocument.class.getResource( XML_TEMPLATE );
        if ( url == null ) {
            throw new IOException( "The resource '" + XML_TEMPLATE + " could not be found." );
        }
        XMLFragment fragment = new XMLFragment();
        fragment.load( url );
        this.root = fragment.getRootElement();
        
    }

    /**
     * Get Root Element of the document.
     * 
     * @return Element
     */
    public Element getRootElement() {
        return this.root;
    }

}
