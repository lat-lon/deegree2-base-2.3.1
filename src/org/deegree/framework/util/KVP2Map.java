// $HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/KVP2Map.java $
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
package org.deegree.framework.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

/**
 * offeres utility method for transformating a key-value-pair encoded request to a <tt>Map</tt>
 * 
 * @version $Revision: 11888 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 11888 $, $Date: 2008-05-26 10:41:56 +0200 (Mo, 26. Mai 2008) $
 * 
 * @since 2.0
 */
public class KVP2Map {

    /**
     * Transforms a String/KVPs like it is used for HTTP-GET request to a Map.
     * 
     * TODO: Check if the trim () call may cause side effects. It is currently used to eliminate
     * possible new line characters at the end of the string, that occured using the
     * <code>GenericClient</code>.
     * 
     * @param kvp
     *            key-value-pair encoded request
     * @return created Map
     */
    public static Map<String, String> toMap( String kvp ) {

        StringTokenizer st = new StringTokenizer( kvp.trim(), "&?" );
        HashMap<String, String> map = new HashMap<String, String>();

        while ( st.hasMoreTokens() ) {
            String s = st.nextToken();
            if ( s != null ) {
                int pos = s.indexOf( '=' );

                if ( pos > -1 ) {
                    String s1 = s.substring( 0, pos );
                    String s2 = s.substring( pos + 1, s.length() );
                    map.put( s1.toUpperCase(), s2 );
                }
            }
        }

        return map;

    }

    /**
     * @param iterator
     *            Enumeration containing KVP encoded parameters
     * @return created Map
     */
    public static Map<String, String> toMap( Enumeration<String> iterator ) {
        HashMap<String, String> map = new HashMap<String, String>();

        while ( iterator.hasMoreElements() ) {
            String s = iterator.nextElement();
            if ( s != null ) {
                int pos = s.indexOf( '=' );

                if ( pos > -1 ) {
                    String s1 = s.substring( 0, pos );
                    String s2 = s.substring( pos + 1, s.length() );
                    map.put( s1.toUpperCase(), s2 );
                }
            }
        }

        return map;
    }

    /**
     * returns the parameters of a <tt>HttpServletRequest</tt> as <tt>Map</tt>. (HINT: all the
     * keys get changed to upper case)
     * 
     * @param request
     * @return a Map which contains kvp's from the given request
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> toMap( HttpServletRequest request ) {
        HashMap<String, String> map = new HashMap<String, String>();

        Enumeration<String> iterator = request.getParameterNames();
        while ( iterator.hasMoreElements() ) {
            String s = iterator.nextElement();
            String val = request.getParameter( s );
            map.put( s.toUpperCase(), val );
        }

        return map;
    }

}