//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/shpapi/shape_new/Shape.java $
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
package org.deegree.io.shpapi.shape_new;

import org.deegree.model.spatialschema.Geometry;

/**
 * <code>Shape</code> defines methods to read, write and use objects read from/written to a
 * shapefile (as well as some basic information).
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Thu, 27 Dec 2007) $
 */
public interface Shape {

    /**
     * Reads the object from a byte array.
     * 
     * @param bytes
     * @param offset
     *            where to start reading
     * @return the new offset or -1, if the type was wrong.
     */
    public int read( byte[] bytes, int offset );

    /**
     * Writes the object to a byte array.
     * 
     * @param bytes
     * @param offset
     * @return the new offset.
     */
    public int write( byte[] bytes, int offset );

    /**
     * @return the number of bytes necessary to write this instance.
     */
    public int getByteLength();

    /**
     * @return the type of the shape
     */
    public int getType();

    /**
     * @return the shapes' envelope, or null, if it has none
     */
    public ShapeEnvelope getEnvelope();

    /**
     * @return the shape as deegree Geometry
     * @throws ShapeGeometryException
     *             if the deegree geometry could not be constructed
     */
    public Geometry getGeometry()
                            throws ShapeGeometryException;

}
