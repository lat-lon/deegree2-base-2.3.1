//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/utils/SunLight.java $
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
package org.deegree.ogcwebservices.wpvs.utils;

import javax.vecmath.Color3f;

/**
 * class for calculating sun light according to a specific tima, day of
 * the year (northern hemisper)
 * 
 *
 * @version $Revision: 9345 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 *
 * $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 *
 */
public class SunLight {
    
    private static final float BASE_LIGHT_INTENSITY = 0.95f;
    private SunPosition sunPosition;
    private double latitude;    
    
    /**
     * @param latitude the position on the earth
     * @param sunPosition the Position of the sun
     */
    public SunLight( double latitude, SunPosition sunPosition ){
        this.latitude = latitude;
        this.sunPosition = sunPosition;
    }
    
    
    /**
     * @return a Color of the sunlight
     */
    public Color3f calculateSunlight( ) {
                                                              
        double vDir = sunPosition.getVerticalSunposition( latitude );
        float c = 7.25f*((float)Math.sin( vDir ));

        float r = (float)(BASE_LIGHT_INTENSITY + (c/16.0) + 0.05)*0.6f;
        float g = (float)(BASE_LIGHT_INTENSITY + (c/18.5) + 0.05)*0.6f;
        float b = (float)(BASE_LIGHT_INTENSITY  +(c/17.0) + 0.05)*0.55f;
        if ( r > 1 ) r = 1;
        if ( g > 1 ) g = 1;
        if ( b > 1 ) b = 1;
        
        return new Color3f( r, g, b );
    }
    
    /**
     * @param cloudFactor describing howmuch clouds cover the sun
     * @return the intensity of the 
     */
    public float calcSunlightIntensity( float cloudFactor) {
        if( cloudFactor < 0 || cloudFactor > 1.0 )
            cloudFactor = 1;
        Color3f vec = calculateSunlight( );
        return ((vec.x + vec.y + vec.z)* 0.33333f )* cloudFactor;
    }
        
}