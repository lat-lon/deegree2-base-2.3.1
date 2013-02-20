//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/WFSRequester.java $
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
 lat/lon GmbH
 Aennchenstraße 19
 53177 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.ogcwebservices.sos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.GMLFeatureAdapter;
import org.deegree.ogcwebservices.OGCWebService;
import org.deegree.ogcwebservices.wfs.operation.FeatureResult;
import org.deegree.ogcwebservices.wfs.operation.GetFeature;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * sends a request to a WFS and checks the result for errors
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * @author last edited by: $Author:wanhoff$
 * 
 * @version $Revision: 9345 $, $Date:20.03.2007$
 */
public class WFSRequester {

    private static ILogger LOG = LoggerFactory.getLogger( WFSRequester.class );

    /**
     * 
     * @param request
     * @param ows
     * @return
     * @throws Exception
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XMLParsingException
     * @throws TransformerException
     */
    public static Document sendWFSrequest( Document request, OGCWebService ows )
                            throws Exception {

        GetFeature getFeature = GetFeature.create( "ID-" + System.currentTimeMillis(), request.getDocumentElement() );
        FeatureResult result = (FeatureResult) ows.doService( getFeature );
        ByteArrayOutputStream bos = new ByteArrayOutputStream( 100000 );

        new GMLFeatureAdapter().export( (FeatureCollection) result.getResponse(), bos );

        Document resultDoc = XMLTools.parse( new ByteArrayInputStream( bos.toByteArray() ) );

        String nn = resultDoc.getDocumentElement().getLocalName();

        if ( "FeatureCollection".equals( nn ) ) {
            return resultDoc;
        }
        LOG.logDebug( "error: invalid wfs result" );
        throw new XMLParsingException( "returned document doesn't contain a valid " + "WFS GetFeature response" );

    }

}
