//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wms/operation/DescribeLayer.java $
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
package org.deegree.ogcwebservices.wms.operation;

import java.util.Map;

import org.deegree.i18n.Messages;
import org.deegree.ogcwebservices.InconsistentRequestException;

/**
 * <code>DescribeLayer</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 9697 $, $Date: 2008-01-23 12:19:46 +0100 (Wed, 23 Jan 2008) $
 */
public class DescribeLayer extends WMSRequestBase {

    private static final long serialVersionUID = 3600055196281010553L;

    private String[] layers;

    /**
     * Creates a new DescribeLayer object.
     * 
     * @param version
     * @param id
     * @param vendorSpecificParameter
     */
    private DescribeLayer( String version, String id, Map<String, String> vendorSpecificParameter, String[] layers ) {
        super( version, id, vendorSpecificParameter );
        this.layers = layers;
    }

    /**
     * @param map
     * @return the new describe layer request
     * @throws InconsistentRequestException
     */
    public static DescribeLayer create( Map<String, String> map )
                            throws InconsistentRequestException {
        String id = map.get( "ID" );
        map.remove( "ID" );
        String version = map.get( "VERSION" );
        map.remove( "VERSION" );

        if ( version == null ) {
            throw new InconsistentRequestException( Messages.getMessage( "WMS_PARAMETER_MUST_BE_SET", "VERSION" ) );
        }

        String ls = map.get( "LAYERS" );
        if ( ls == null ) {
            throw new InconsistentRequestException( Messages.getMessage( "WMS_PARAMETER_MUST_BE_SET", "LAYERS" ) );
        }

        String[] layers = ls.split( "," );
        if ( layers.length == 0 ) {
            throw new InconsistentRequestException( Messages.getMessage( "WMS_PARAMETER_MUST_BE_SET", "LAYERS" ) );
        }

        return new DescribeLayer( version, id, map, layers );
    }

    /**
     * @return the list of requested layers
     */
    public String[] getLayers() {
        return layers;
    }

}
