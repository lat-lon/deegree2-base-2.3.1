//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/discovery/GetRecordByIdDocument.java $
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
package org.deegree.ogcwebservices.csw.discovery;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.csw.AbstractCSWRequestDocument;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @version $Revision: 9499 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 9499 $, $Date: 2008-01-09 16:47:04 +0100 (Wed, 09 Jan 2008) $
 * 
 * @since 2.0
 */
public class GetRecordByIdDocument extends AbstractCSWRequestDocument {

    private static final long serialVersionUID = 2796229558893029054L;

    private static final String XML_TEMPLATE = "GetRecordByIdTemplate.xml";

    /**
     * 
     * @param id
     * @return
     */
    GetRecordById parse( String id )
                            throws OGCWebServiceException {

        String version = null;
        Map<String, String> vendorSpecificParameters = null;
        String[] ids = null;
        String elementSetName = null;

        try {
            // '<csw:GetRecords>'-element (required)
            Node contextNode = XMLTools.getRequiredNode( this.getRootElement(), "self::csw:GetRecordById", nsContext );

            // 'service'-attribute (required, must be CSW)
            String service = XMLTools.getRequiredNodeAsString( contextNode, "@service", nsContext );
            if ( !service.equals( "CSW" ) ) {
                ExceptionCode code = ExceptionCode.INVALIDPARAMETERVALUE;
                throw new InvalidParameterValueException( "GetRecordById", "'service' must be 'CSW'", code );
            }

            // 'version'-attribute (required)
            version = XMLTools.getNodeAsString( contextNode, "@version", nsContext, GetRecords.DEFAULT_VERSION );

            // '<csw:ResponseHandler>'-elements (optional)
            ids = XMLTools.getNodesAsStrings( contextNode, "csw:Id", nsContext );

            // '<csw:ElementSetName>'-element (optional)
            Node elementSetNameElement = XMLTools.getNode( contextNode, "csw:ElementSetName", nsContext );

            if ( elementSetNameElement != null ) {
                // must contain one of the values 'brief', 'summary' or
                // 'full'
                elementSetName = XMLTools.getRequiredNodeAsString( elementSetNameElement, "text()", nsContext,
                                                                   new String[] { "brief", "summary", "full" } );

            } else {
                elementSetName = "summary";
            }
            // in the future the vendorSpecificParameters
            vendorSpecificParameters = parseDRMParams( this.getRootElement() );
        } catch ( Exception e ) {
            ExceptionCode code = ExceptionCode.INVALIDPARAMETERVALUE;
            throw new OGCWebServiceException( "GetRecordByIdDocument", StringTools.stackTraceToString( e ), code );
        }

        return new GetRecordById( id, version, vendorSpecificParameters, ids, elementSetName );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.framework.xml.XMLFragment#createEmptyDocument()
     */
    void createEmptyDocument()
                            throws IOException, SAXException {
        URL url = GetRecordByIdDocument.class.getResource( XML_TEMPLATE );
        if ( url == null ) {
            throw new IOException( "The resource '" + XML_TEMPLATE + " could not be found." );
        }
        load( url );
    }

}
