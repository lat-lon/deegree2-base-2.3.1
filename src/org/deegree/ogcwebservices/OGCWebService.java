// $HeadURL: /cvsroot/deegree/src/org/deegree/ogcwebservices/OGCWebService.java,v
// 1.7 2004/06/23 13:37:40 mschneider Exp $
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
package org.deegree.ogcwebservices;

import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;

/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */
public interface OGCWebService {

    /**
     * returns the capabilities of a OGC web service
     * 
     * @return the capabilities of a OGC web service
     */
    OGCCapabilities getCapabilities();

    /**
     * the implementation of this method performs the handling of the passed OGCWebServiceEvent
     * directly and returns the result to the calling class/ method
     * 
     * @param request
     *            request (WMS, WCS, WFS, CSW, WFS-G, WMPS) to perform
     * 
     * @return result of a service operation
     * @throws OGCWebServiceException
     */
    Object doService( OGCWebServiceRequest request )
                            throws OGCWebServiceException;

}
