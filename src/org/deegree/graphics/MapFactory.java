//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/MapFactory.java $
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
package org.deegree.graphics;

import org.deegree.graphics.sld.UserStyle;
import org.deegree.model.coverage.grid.GridCoverage;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.spatialschema.Envelope;

/**
 * Factory class for creating <tt>MapView</tt>s, <tt>Layer</tt>s and <tt>Theme</tt>s.
 * 
 * <p>
 * ------------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 9340 $ $Date: 2007-12-27 13:32:12 +0100 (Thu, 27 Dec 2007) $
 */
public class MapFactory {
    /**
     * creates an empty feature layer with EPSG:4326 as default coordinate reference system. All
     * data that will be added to the layer will be converted to the EPSG:4326 coordinate reference
     * system if no other CRS is set.
     */
    public static synchronized Layer createFeatureLayer( String name )
                            throws Exception {
        return new FeatureLayer( name );
    }

    /**
     * creates an empty feature layer. All data that will be added to the layer will be converted to
     * the submitted coordinate reference system if no other CRS is set.
     */
    public static synchronized Layer createFeatureLayer( String name, CoordinateSystem crs )
                            throws Exception {
        return new FeatureLayer( name, crs );
    }

    /**
     * creates a complete feature layer. If the CRS of the geometries contained within the submitted
     * feature collection are not the same as the submitted CRS all geometries will be converted to
     * the submitted CRS.
     */
    public static synchronized Layer createFeatureLayer( String name, CoordinateSystem crs, FeatureCollection fc )
                            throws Exception {
        return new FeatureLayer( name, crs, fc );
    }

    /**
     * creates a raster layer. The required CRS is contained within the <tt>GC_GridCoverage</tt>
     * object
     */
    public static synchronized Layer createRasterLayer( String name, GridCoverage raster )
                            throws Exception {
        return new RasterLayer( name, raster );
    }

    /**
     * creates a theme with a name, a Layer containing the themes data and an array of styles to be
     * known by the <tt>Theme</tt>
     */
    public static synchronized Theme createTheme( String name, Layer layer, UserStyle[] styles ) {
        return new Theme( name, layer, styles );
    }

    /**
     * creates a theme with a name, a Layer containing the themes data and an array of styles to be
     * known by the <tt>Theme</tt>
     */
    public static synchronized Theme createTheme( String name, Layer layer ) {
        return new Theme( name, layer, new UserStyle[] { null } );
    }

    /**
     * creates a <tt>MapView</tt> with a name, a boundingbox describing the area coverd by the
     * <tt>MapView</tt>, a CRS and n <tt>Theme</tt>s.
     */
    public static synchronized MapView createMapView( String name, Envelope boundingbox, CoordinateSystem crs,
                                                      Theme[] themes, double pixelsize )
                            throws Exception {
        MapView mv = new MapView( name, boundingbox, crs, pixelsize );
        for ( int i = 0; i < themes.length; i++ ) {
            mv.addTheme( themes[i] );
        }

        return mv;
    }

}