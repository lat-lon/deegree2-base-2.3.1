//$$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/tools/shape/AVLPointSymbolCodeList.java $$
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
package org.deegree.tools.shape;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.1, $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 1.1
 */
class AVLPointSymbolCodeList {

    private static final String CODELIST = "AVLPointSymbolCodeList.xml";

    private Map<String, String> map = new HashMap<String, String>();

    /**
     * 
     */
    public AVLPointSymbolCodeList() throws SAXException, IOException, XMLParsingException {

        XMLFragment frag = new XMLFragment( AVLPointSymbolCodeList.class.getResource( CODELIST ) );
        Document doc = frag.getRootElement().getOwnerDocument();

        /* ************************* OLD ********************************* */
        // Node nsNode = XMLTools.getNamespaceNode( new String[] {} );
        // NodeList nl = XMLTools.getXPath( "Symbols", element, nsNode );
        NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();
        List nl = XMLTools.getNodes( doc, "Symbols", nsContext );

        for ( Object o : nl ) {
            if ( o instanceof Node ) {
                Node n = (Node) o;
                String code = XMLTools.getRequiredNodeAsString( n, "@code", nsContext );
                String sym = XMLTools.getRequiredNodeAsString( n, "@symbol", nsContext );
                map.put( code, sym );
            }
        }
    }

    public String getSymbol( String code ) {
        return map.get( code );
    }
}
