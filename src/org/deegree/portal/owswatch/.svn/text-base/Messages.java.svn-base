//$$Header: $$
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

package org.deegree.portal.owswatch;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * i118n class for error messages
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.1 $, $Date: 2008-03-07 14:49:52 $
 */
public class Messages {

    private static final String BUNDLE_NAME = "org.deegree.portal.owswatch.messages_en";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

    private Messages() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Returns the message assigned to the passed key. If no message is assigned, an error message will be returned that
     * indicates the missing key.
     * 
     * @see MessageFormat for conventions on string formatting and escape characters.
     * 
     * @param key
     * @param arguments
     * @return the message assigned to the passed key
     */
    public static String getMessage( String key, Object... arguments ) {
        String s = RESOURCE_BUNDLE.getString( key );
        if ( s != null ) {
            return MessageFormat.format( s, arguments );
        }
        // to avoid NPEs
        return "$Message with key: " + key + " not found$";
    }

    /**
     * @param key
     * @return String
     */
    public static String getString( String key ) {
        try {
            return RESOURCE_BUNDLE.getString( key );
        } catch ( MissingResourceException e ) {
            return '!' + key + '!';
        }
    }

    /**
     * transforms a string array to one string. the array fields are seperated by the submitted delimiter:
     * 
     * @param s
     *            stringarray to transform
     * @param delimiter
     * @return String
     */
    public static String arrayToString( String[] s, String delimiter ) {
        StringBuffer res = new StringBuffer( s.length * 20 );

        for ( int i = 0; i < s.length; i++ ) {
            res.append( s[i] );

            if ( i < ( s.length - 1 ) ) {
                res.append( delimiter );
            }
        }
        return res.toString();
    }
}
