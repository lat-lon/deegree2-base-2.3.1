//$HeadURL$
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

package org.deegree.ogcwebservices.csw;

import java.util.HashMap;
import java.util.Map;

import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.OGCDocument;
import org.w3c.dom.Element;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public abstract class AbstractCSWRequestDocument extends OGCDocument {

    protected Map<String, String> parseDRMParams( Element root )
                            throws XMLParsingException {
        String user = XMLTools.getNodeAsString( root, "@user", nsContext, null );
        String password = XMLTools.getNodeAsString( root, "@password", nsContext, null );
        String sessionID = XMLTools.getNodeAsString( root, "@sessionID", nsContext, null );
        Map<String, String> vendorSpecificParam = new HashMap<String, String>();
        vendorSpecificParam.put( "USER", user );
        vendorSpecificParam.put( "PASSWORD", password );
        vendorSpecificParam.put( "SESSIONID", sessionID );
        return vendorSpecificParam;
    }

}
