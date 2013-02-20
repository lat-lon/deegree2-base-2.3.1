//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/coverage/grid/GridRange.java $
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
package org.deegree.model.coverage.grid;

import java.io.Serializable;

/**
 * Specifies the range of valid coordinates for each dimension of the coverage.
 * 
 * @version $Revision: 9343 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9343 $, $Date: 2007-12-27 14:30:32 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */
public class GridRange implements Serializable {

    private static final long serialVersionUID = -7292466343852424913L;

    private int[] up = null;

    private int[] lo = null;

    /**
     * @param lo
     * @param up
     */
    public GridRange( int[] lo, int[] up ) {
        this.up = up;
        this.lo = lo;
    }

    /**
     * The valid maximum exclusive grid coordinate. The sequence contains a maximum value for each
     * dimension of the grid coverage.
     * 
     */
    public int[] getUpper() {
        return up;
    }

    /**
     * The valid minimum inclusive grid coordinate. The sequence contains a minimum value for each
     * dimension of the grid coverage. The lowest valid grid coordinate is zero.
     * 
     */
    public int[] getLower() {
        return lo;
    }

}
