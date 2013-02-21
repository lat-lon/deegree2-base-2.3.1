//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/ColorUtils.java $
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
package org.deegree.framework.util;

import java.awt.Color;

/**
 * offeres some methods for handling colors
 * 
 * 
 * @version $Revision: 11861 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 11861 $, $Date: 2008-05-21 09:52:14 +0200 (Mi, 21. Mai 2008) $
 * 
 * @since 2.0
 */
public class ColorUtils {

    /**
     * returns a random color
     * 
     * @param useTransparency
     * @return a random color
     */
    public static Color getRandomColor( boolean useTransparency ) {
        float r = (float) Math.random();
        float g = (float) Math.random();
        float b = (float) Math.random();
        float a = 0;
        if ( useTransparency ) {
            a = (float) Math.random() / 1.5f;
        }
        return new Color( r, g, b, a );
    }

    /**
     * transforms the color of the request from java.awt.Color to the hexadecimal representation as
     * in an OGC conform WMS-GetMap request (e.g. white == "#ffffff").
     * @param prefix to add to the hex
     * @param color to get hex code from
     * 
     * @return the color as hexadecimal representation
     */
    public static String toHexCode( String prefix, Color color ) {
        String r = Integer.toHexString( color.getRed() );
        if ( r.length() < 2 )
            r = "0" + r;
        String g = Integer.toHexString( color.getGreen() );
        if ( g.length() < 2 )
            g = "0" + g;
        String b = Integer.toHexString( color.getBlue() );
        if ( b.length() < 2 )
            b = "0" + b;
        return prefix + r + g + b;
    }

}
