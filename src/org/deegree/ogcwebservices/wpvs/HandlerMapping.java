//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/HandlerMapping.java $
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
package org.deegree.ogcwebservices.wpvs;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $
 * 
 */
public class HandlerMapping {
    // private static final String BUNDLE_NAME = ;

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( "org.deegree.ogcwebservices.wpvs.handler" );

    private static HashMap<String, String> configuredHandlers = null;

    private HandlerMapping() {
        // empty private constructor
    }

    /**
     * @param key
     * @return the class name
     */
    public static String getString( String key ) {
        try {
            return RESOURCE_BUNDLE.getString( key );
        } catch ( MissingResourceException e ) {
            return "!" + key + "!";
        }
    }

    /**
     * @return the configured GetViewHandlers in the bundle
     *         "org.deegree.ogcwebservices.wpvs.handler", but with the package name of the keys
     *         removed, e.g the bundle key: "WPVService.GETVIEW.BOX " will be transformed to "BOX"
     *         (for easy acces).
     */
    synchronized public static HashMap<String, String> getConfiguredGetViewHandlers() {
        if ( configuredHandlers != null )
            return configuredHandlers;
        configuredHandlers = new HashMap<String, String>();
        Enumeration<String> keys = RESOURCE_BUNDLE.getKeys();
        while ( keys.hasMoreElements() ) {
            String keyValue = keys.nextElement();
            String[] packageNames = keyValue.split( "[.]" );
            String pName = packageNames[0];
            if ( packageNames.length > 0 )
                pName = packageNames[packageNames.length - 1];
            configuredHandlers.put( pName, RESOURCE_BUNDLE.getString( keyValue ) );
        }
        return configuredHandlers;
    }
}
