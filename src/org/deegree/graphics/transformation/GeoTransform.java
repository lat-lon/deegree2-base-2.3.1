//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/transformation/GeoTransform.java $
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
package org.deegree.graphics.transformation;

import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.Position;

/**
 * <code>GeoTransformInterface</code> declares the methods which have to be implemented by each
 * class that executes a geographical coordinat transformation.
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 */
public interface GeoTransform {
    /**
     * 
     * 
     * @param xdest
     * 
     * @return source x coordinate
     */
    public double getSourceX( double xdest );

    /**
     * 
     * 
     * @param xsource
     * 
     * @return destination x coordinate
     */
    public double getDestX( double xsource );

    /**
     * 
     * 
     * @param ydest
     * 
     * @return source y coordinate
     */
    public double getSourceY( double ydest );

    /**
     * 
     * 
     * @param ysource
     * 
     * @return destination y coordinate
     */
    public double getDestY( double ysource );

    /**
     * @param rect
     * 
     */
    public void setSourceRect( Envelope rect );

    /**
     * 
     * 
     * @param xMin
     * @param yMin
     * @param xMax
     * @param yMax
     */
    public void setSourceRect( double xMin, double yMin, double xMax, double yMax );

    /**
     * @return source rectange
     * 
     */
    public Envelope getSourceRect();

    /**
     * @param rect
     * 
     */
    public void setDestRect( Envelope rect );

    /**
     * 
     * 
     * @param xMin
     * @param yMin
     * @param xMax
     * @param yMax
     */
    public void setDestRect( double xMin, double yMin, double xMax, double yMax );

    /**
     * @return destination rectange
     * 
     */
    public Envelope getDestRect();

    /**
     * 
     * 
     * @param point
     * 
     * @return source position
     */
    public Position getSourcePoint( Position point );

    /**
     * 
     * 
     * @param point
     * 
     * @return destination position
     */
    public Position getDestPoint( Position point );
}