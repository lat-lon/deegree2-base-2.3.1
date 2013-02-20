//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/getcoverage/DomainSubset.java $
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
package org.deegree.ogcwebservices.wcs.getcoverage;

import org.deegree.datatypes.Code;
import org.deegree.datatypes.time.TimeSequence;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcwebservices.wcs.WCSException;

/**
 * @version $Revision: 10679 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * $Revision: 10679 $, $Date: 2008-03-25 18:26:57 +0100 (Tue, 25 Mar 2008) $
 */

public class DomainSubset {

    private Code requestSRS = null;

    private SpatialSubset spatialSubset = null;

    private TimeSequence temporalSubset = null;

    /**
     * @param requestSRS
     * @param spatialSubset
     * @throws WCSException
     */
    public DomainSubset( Code requestSRS, SpatialSubset spatialSubset ) throws WCSException {
        this( requestSRS, spatialSubset, null );
    }

    /**
     * @param requestSRS
     * @param temporalSubset
     * @throws WCSException
     */
    public DomainSubset( Code requestSRS, TimeSequence temporalSubset ) throws WCSException {
        this( requestSRS, null, temporalSubset );
    }

    /**
     * @param requestSRS
     * @param spatialSubset
     * @param temporalSubset
     * @throws WCSException
     *             if one of the parameters is null
     */
    public DomainSubset( Code requestSRS, SpatialSubset spatialSubset, TimeSequence temporalSubset )
                            throws WCSException {
        if ( spatialSubset == null && temporalSubset == null ) {
            ExceptionCode code = ExceptionCode.MISSINGPARAMETERVALUE;
            throw new WCSException( "GetCoverage", "at least spatialSubset "
                                                   + "or temporalSubset must be <> null in DomainSubset", code );
        }
        if ( requestSRS == null ) {
            ExceptionCode code = ExceptionCode.MISSINGPARAMETERVALUE;
            throw new WCSException( "GetCoverage", "'crs/requestSRS' is missing", code );
        }
        this.requestSRS = requestSRS;
        this.spatialSubset = spatialSubset;
        this.temporalSubset = temporalSubset;
    }

    /**
     * @return Returns the spatialSubset.
     * 
     */
    public SpatialSubset getSpatialSubset() {
        return spatialSubset;
    }

    /**
     * @return Returns the temporalSubset.
     */
    public TimeSequence getTemporalSubset() {
        return temporalSubset;
    }

    /**
     * @return Returns the requestSRS.
     */
    public Code getRequestSRS() {
        return requestSRS;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer( 300 );
        sb.append( "requestSRS=" );
        sb.append( requestSRS );
        sb.append( ", spatialSubset=" );
        sb.append( spatialSubset );
        return sb.toString();
    }

}
