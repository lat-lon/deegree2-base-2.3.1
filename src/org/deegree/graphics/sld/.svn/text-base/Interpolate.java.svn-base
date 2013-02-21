//$HeadURL: https://sushibar/svn/deegree/base/trunk/resources/eclipse/svn_classfile_header_template.xml $
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

package org.deegree.graphics.sld;

import java.util.List;

import org.deegree.graphics.sld.SLDFactory.Method;
import org.deegree.graphics.sld.SLDFactory.Mode;

/**
 * <code>Interpolate</code> encapsulates data from the Symbology Encoding Schema
 * (InterpolateType).
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class Interpolate {

    private String fallbackValue;

    private ParameterValueType lookupValue;

    private Mode mode;

    private Method method;

    private List<InterpolationPoint> points;

    /**
     * @param fallbackValue
     */
    public Interpolate( String fallbackValue ) {
        this.fallbackValue = fallbackValue;
    }

    /**
     * @param lookupValue
     */
    public void setLookupValue( ParameterValueType lookupValue ) {
        this.lookupValue = lookupValue;
    }

    /**
     * @return the fallbackValue
     */
    public String getFallbackValue() {
        return fallbackValue;
    }

    /**
     * @return the lookupValue
     */
    public ParameterValueType getLookupValue() {
        return lookupValue;
    }

    /**
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @param mode
     */
    public void setMode( Mode mode ) {
        this.mode = mode;
    }

    /**
     * @param method
     */
    public void setMethod( Method method ) {
        this.method = method;
    }

    /**
     * @param d
     * @param opac
     *            the default opacity if none has been set
     * @return the color value as int
     */
    public int interpolate( float d, int opac ) {
        int opacity = opac >> 24;

        InterpolationPoint p = points.get( 0 );
        int r = p.redValue, g = p.greenValue, b = p.blueValue, o = p.opacitySet ? p.opacity : opacity;
        double data = p.data;
        if ( d < p.data ) {
            return ( o << 24 ) + ( r << 16 ) + ( g << 8 ) + b;
        }

        for ( int i = 1; i < points.size(); ++i ) {
            p = points.get( i );
            if ( p.data > d ) {
                double f = ( p.data - d ) / ( p.data - data );
                return ( ( (int) ( f * ( p.opacitySet ? p.opacity : opacity - o ) + o ) ) << 24 )
                       + ( ( (int) ( f * ( p.redValue - r ) + r ) ) << 16 )
                       + ( ( (int) ( f * ( p.greenValue - g ) + g ) ) << 8 )
                       + ( (int) ( ( f * ( p.blueValue - b ) + b ) ) );
            }
            r = p.redValue;
            g = p.greenValue;
            b = p.blueValue;
            o = p.opacitySet ? p.opacity : opacity;
            data = p.data;
        }

        return ( o << 24 ) + ( r << 16 ) + ( g << 8 ) + b;
    }

    /**
     * @param points
     */
    public void setInterpolationPoints( List<InterpolationPoint> points ) {
        this.points = points;
    }

}
