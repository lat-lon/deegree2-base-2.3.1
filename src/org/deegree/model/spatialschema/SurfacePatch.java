//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/spatialschema/SurfacePatch.java $
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

import org.deegree.model.crs.CoordinateSystem;

/**
 * 
 * Defining the iso geometry <code>SurfacePatch</code> which is used for building surfaces. A
 * surface patch is made of one exterior ring and 0..n interior rings. By definition there can't be
 * a surface patch with no exterior ring. A polygon is a specialized surface patch.
 * 
 * -----------------------------------------------------
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 9343 $ $Date: 2007-12-27 14:30:32 +0100 (Thu, 27 Dec 2007) $
 */

public interface SurfacePatch extends GenericSurface {

    /**
     * The interpolation determines the surface interpolation mechanism used for this SurfacePatch.
     * This mechanism uses the control points and control parameters defined in the various
     * subclasses to determine the position of this SurfacePatch.
     */
    SurfaceInterpolation getInterpolation();

    /**
     * @return the exterior ring of a surfacePatch
     */
    Position[] getExteriorRing();

    /**
     * @return the interior rings of a surfacePatch
     */
    Position[][] getInteriorRings();

    /**
     * 
     * @return the exterior ring of a surfacePatch
     */
    Ring getExterior();

    /**
     * 
     * @return the interior rings of a surfacePatch
     */
    Ring[] getInterior();

    /**
     * @return the coordinate system of the surface patch
     */
    CoordinateSystem getCoordinateSystem();

    /**
     * The Boolean valued operation "intersects" shall return TRUE if this Geometry intersects
     * another Geometry. Within a Complex, the Primitives do not intersect one another. In general,
     * topologically structured data uses shared geometric objects to capture intersection
     * information.
     */
    boolean intersects( Geometry gmo );

    /**
     * The Boolean valued operation "contains" shall return TRUE if this Geometry contains another
     * Geometry.
     */
    boolean contains( Geometry gmo );

    /**
     * The operation "centroid" shall return the mathematical centroid for this Geometry. The result
     * is not guaranteed to be on the object.
     */
    public Point getCentroid();

    /**
     * The operation "area" shall return the area of this GenericSurface. The area of a 2
     * dimensional geometric object shall be a numeric measure of its surface area Since area is an
     * accumulation (integral) of the product of two distances, its return value shall be in a unit
     * of measure appropriate for measuring distances squared.
     */
    public double getArea();

}
