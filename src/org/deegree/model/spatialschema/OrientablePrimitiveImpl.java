//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/spatialschema/OrientablePrimitiveImpl.java $
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
package org.deegree.model.spatialschema;

import java.io.Serializable;

import org.deegree.model.crs.CoordinateSystem;

/**
 * default implementation of the OrientablePrimitive interface from package jago.model. the
 * implementation is abstract because it doesn't make sense to instantiate it.
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 10547 $, $Date: 2008-03-11 09:40:28 +0100 (Tue, 11 Mar 2008) $
 */
abstract class OrientablePrimitiveImpl extends PrimitiveImpl implements OrientablePrimitive, Serializable {
    /** Use serialVersionUID for interoperability. */
    private final static long serialVersionUID = 5655221930434396483L;

    protected char orientation = '+';

    /**
     * the constructor sets the curves orientation
     * 
     * @param crs
     *            spatial reference system of the geometry
     * @param orientation
     *            orientation of the curve ('+'|'-')
     * 
     * @exception GeometryException
     *                will be thrown if orientation is invalid
     */
    protected OrientablePrimitiveImpl( CoordinateSystem crs, char orientation ) throws GeometryException {
        super( crs );
        setOrientation( orientation );
    }

    /**
     * returns the orientation of a curve
     * 
     * @return curve orientation ('+'|'-')
     * 
     */
    public char getOrientation() {
        return orientation;
    }

    /**
     * sets the curves orientation
     * 
     * @param orientation
     *            orientation of the curve ('+'|'-')
     * 
     * @exception GeometryException
     *                will be thrown if orientation is invalid
     * 
     */
    public void setOrientation( char orientation )
                            throws GeometryException {
        if ( ( orientation != '+' ) && ( orientation != '-' ) ) {
            throw new GeometryException( orientation + " isn't a valid direction" );
        }

        this.orientation = orientation;
    }

}
