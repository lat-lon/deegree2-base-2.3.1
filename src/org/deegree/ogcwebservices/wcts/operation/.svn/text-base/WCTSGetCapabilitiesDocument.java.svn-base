//$HeadURL: $
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

package org.deegree.ogcwebservices.wcts.operation;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.owscommon_1_1_0.operations.GetCapabilitiesDocument;
import org.w3c.dom.Element;

/**
 * <code>WCTSGetCapabilitiesDocument</code> parses the xml-dom representation of a GetCapabilites request.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class WCTSGetCapabilitiesDocument extends GetCapabilitiesDocument {
    
    private static ILogger LOG = LoggerFactory.getLogger( WCTSGetCapabilitiesDocument.class );

    private static final long serialVersionUID = 678779302024110331L;

    private final WCTSGetCapabilities getCapabilities;

    /**
     * @param id
     *            of the request
     * @param requestRoot
     *            the GetCapabilities request root element.
     * @throws OGCWebServiceException
     *             if the requestRoot is <code>null</code>
     */
    public WCTSGetCapabilitiesDocument( String id, Element requestRoot ) throws OGCWebServiceException {
        if ( requestRoot == null ) {
            throw new IllegalArgumentException( "The root element of a GetCapabilities request may not be null" );
        }

        setRootElement( requestRoot );
        try {
            String service = requestRoot.getAttribute( "service" );
            if ( service== null || !"WCTS".equals( service.toUpperCase().trim() ) ) {
                throw new OGCWebServiceException( "The sevice attribute must be set to 'WCTS'",
                                                  ExceptionCode.INVALIDPARAMETERVALUE );
            }

            getCapabilities = new WCTSGetCapabilities( id,
                                                       parseUpdateSequence(),
                                                       parseAcceptVersions(),
                                                       parseSections(),
                                                       parseAcceptFormats() );
        } catch ( XMLParsingException e ) {
            LOG.logError( e.getMessage(), e );
            throw new OGCWebServiceException( e.getMessage(), ExceptionCode.MISSINGPARAMETERVALUE );
        }
    }

    /**
     * @return the getCapabilities request bean.
     */
    public final WCTSGetCapabilities getGetCapabilities() {
        return getCapabilities;
    }

}
