//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/getcoverage/SpatialSubset.java $
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

import org.deegree.model.spatialschema.Envelope;
import org.deegree.ogcwebservices.wcs.WCSException;

/**
 * @version $Revision: 9345 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */

public class SpatialSubset {

    private Envelope envelope = null;

    private Object grid = null;

    /**
     * @param envelope
     * @param grid
     * @throws WCSException
     *             if one of the parameters is null
     */
    public SpatialSubset( Envelope envelope, Object grid ) throws WCSException {
        if ( envelope == null ) {
            throw new WCSException( "envelope must be <> null for SpatialSubset" );
        }
        if ( grid == null ) {
            throw new WCSException( "grid must be <> null for SpatialSubset" );
        }
        this.envelope = envelope;
        this.grid = grid;
    }

    /**
     * @return Returns the envelope.
     */
    public Envelope getEnvelope() {
        return envelope;
    }

    /**
     * @return Returns the grid.
     */
    public Object getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer( 300 );
        sb.append( "envelope=(" );
        sb.append( envelope );
        sb.append( "), grid=(" );
        sb.append( grid );
        sb.append( ')' );
        return sb.toString();
    }

}
