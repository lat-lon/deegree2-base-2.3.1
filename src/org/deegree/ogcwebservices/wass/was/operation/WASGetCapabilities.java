//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/was/operation/WASGetCapabilities.java $
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
package org.deegree.ogcwebservices.wass.was.operation;

import java.util.Map;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.getcapabilities.GetCapabilities;
import org.w3c.dom.Element;

/**
 * This is the bean class for the WAS GetCapabilities operation.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9348 $, $Date: 2007-12-27 17:59:14 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */

public class WASGetCapabilities extends GetCapabilities {

	private static final long serialVersionUID = -5377481260861904187L;

	private static final String SERVICE = "WAS";
    
    private static final ILogger LOG = LoggerFactory.getLogger(WASGetCapabilities.class);

    /**
     * @param id
     *            the request id
     * @param version
     * @param updateSequence
     * @param acceptVersions
     * @param sections
     * @param acceptFormats
     * @param vendoreSpec
     */
    public WASGetCapabilities( String id, String version, String updateSequence,
                                 String[] acceptVersions, String[] sections, String[] acceptFormats,
                                 Map<String,String> vendoreSpec) {
        super( id, version, updateSequence, acceptVersions, sections, acceptFormats, vendoreSpec );
    }
    
    /**
     * @param id the request id
     * @param keyValues 
     */
    public WASGetCapabilities( String id, Map<String, String> keyValues ){
        super( id, getParam( "VERSION", keyValues, null ), null, null, null, null, keyValues );
    }

	/* (non-Javadoc)
	 * @see org.deegree.ogcwebservices.OGCWebServiceRequest#getServiceName()
	 */
	public String getServiceName() {
		return SERVICE;
	}

    /**
     * @param id
     * @param documentElement
     * @return a new instance of this class
     * @throws OGCWebServiceException
     */
    public static OGCWebServiceRequest create( String id, Element documentElement )
                            throws OGCWebServiceException {
        
        
        WASGetCapabilities res = null;
        try {
            res = new WASGetCapabilitiesDocument().parseCapabilities( id, documentElement );
        } catch ( XMLParsingException e ) {
            LOG.logError( e.getMessage(), e );
            throw new OGCWebServiceException( e.getMessage() );
        }
        
        
        return res;
    }

    /**
     * @param id
     * @param kvp
     * @return a new instance of this class
     */
    public static OGCWebServiceRequest create( String id, Map<String, String> kvp ) {
        return new WASGetCapabilities(id, kvp);
    }

}
