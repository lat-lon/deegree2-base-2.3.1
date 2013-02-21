//$HeadURL: $
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

package org.deegree.ogcwebservices.csw.capabilities;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deegree.i18n.Messages;

/**
 * The <code>CSWFeature</code> class is a simple bean holding a property map.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */

public class CSWFeature {
    private Map<URI, List<String>> properties;

    /**
     * Initialisded with no properties.
     */
    public CSWFeature() {
        this.properties = new HashMap<URI, List<String>>();
    }

    /**
     * sets the features properties to the given map.
     * 
     * @param properties
     *            to set to.
     */
    public CSWFeature( Map<URI, List<String>> properties ) {
        this.properties = properties;
    }

    /**
     * @return the properties.
     */
    public Map<URI, List<String>> getAllProperties() {
        return properties;
    }

    /**
     * @param propName
     *            an unambiguous name for the property to insert
     * @param values
     *            of the properties
     * @throws IllegalArgumentException
     *             if the propName is empty, unambiguous or null.
     */
    public void addPropertie( URI propName, List<String> values ) {
        if ( propName == null || "".equals( propName.toASCIIString().trim() ) ) {
            throw new IllegalArgumentException( Messages.getMessage( "WRS_NULL_FEAT_PROP" ) );
        }
        if ( properties.containsKey( propName ) ) {
            throw new IllegalArgumentException( Messages.getMessage( "WRS_UNAMBIGUOUS_FEAT_PROP", propName.toString() ) );
        }
        this.properties.put( propName, values );
    }

}
