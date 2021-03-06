// $HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/configuration/ShapeResolution.java $
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
package org.deegree.ogcwebservices.wcs.configuration;

/**
 * modls a <tt>Resolution<tT> by describing the assigned coverages through
 * a Shapefile containing name an boundingbox of each available file
 * 
 * @version $Revision: 10679 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10679 $, $Date: 2008-03-25 18:26:57 +0100 (Di, 25. Mär 2008) $
 * 
 * @since 2.0
 */

public class ShapeResolution extends AbstractResolution {

    private Shape shape = null;

    /**
     * @param minScale
     * @param maxScale
     * @param ranges
     * @param shape
     * @throws IllegalArgumentException
     */
    public ShapeResolution( double minScale, double maxScale, Range[] ranges, Shape shape )
                            throws IllegalArgumentException {
        super( minScale, maxScale, ranges );
        this.shape = shape;
    }

    /**
     * @return Returns the shape.
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * @param shape
     *            The shape to set.
     */
    public void setShape( Shape shape ) {
        this.shape = shape;
    }

}
