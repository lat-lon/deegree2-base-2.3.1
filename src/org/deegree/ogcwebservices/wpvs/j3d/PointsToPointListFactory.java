//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/j3d/PointsToPointListFactory.java $
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
 Aennchenstraße 19
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

package org.deegree.ogcwebservices.wpvs.j3d;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.spatialschema.Point;

/**
 * ... 
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */

public class PointsToPointListFactory/* implements PointListFactory*/ {


    /**
     * Builds a point list from the <code>Point</code>s in the 
     * FeatureCollection fc. 
     * 
     * @param fc a feature collection containing <code>Point</code>s. This collection cannot be null
     * and must contain a geometry property of <code>Point</code> type. No check for this is done.    
     * @return a List with <code>Point</code>s
     */
	public List<Point3d> createFromFeatureCollection( FeatureCollection fc  ) {
		if ( fc == null ) {
			 throw new NullPointerException("FeatureColection cannot be null.");
        }

        List<Point3d> ptsList = new ArrayList<Point3d>( fc.size() +1 );
        for ( int i = 0; i < fc.size(); i++ ) {
            Point point = (Point)fc.getFeature(i).getDefaultGeometryPropertyValue();
            if( point != null ){
                ptsList.add( new Point3d( point.getX(), point.getY(), point.getZ() ) );
            }
        }
 
        return ptsList;
	}
	
}