//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/xml/CityGMLUtils.java $
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
package org.deegree.framework.xml;

import org.deegree.framework.util.StringTools;
import org.w3c.dom.Node;

/**
 * 
 * 
 * 
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */
public class CityGMLUtils {

    /**
     * 
     * @param color
     * @param transparency
     * @return
     */
    public static String getColor( Node color, Node transparency ) {
        StringBuffer clr = new StringBuffer( 10 );

        if ( transparency != null ) {
            String s = XMLTools.getStringValue( transparency );
            float f = Float.parseFloat( s );
            int v = Math.round( f * 255 );
            clr.append( Integer.toHexString( v ) );
        } else {
            clr.append( "e5" );
        }

        if ( color != null ) {
            String s = XMLTools.getStringValue( color );
            float[] tmp = StringTools.toArrayFloat( s, " " );
            for ( int i = 0; i < tmp.length; i++ ) {
                int v = Math.round( tmp[i] * 255 );
                s = Integer.toHexString( v );
                if ( s.length() == 1 ) {
                    s = '0' + s;
                }
                clr.append( s );
            }
        } else {
            clr.append( "ff0000" );
        }

        return clr.toString();
    }

}
