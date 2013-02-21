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

package org.deegree.ogcwebservices.wms;

import java.util.LinkedList;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wms.capabilities.Layer;
import org.deegree.ogcwebservices.wms.configuration.AbstractDataSource;
import org.deegree.ogcwebservices.wms.configuration.LocalWFSDataSource;
import org.deegree.ogcwebservices.wms.configuration.WMSConfigurationType;
import org.deegree.ogcwebservices.wms.operation.DescribeLayer;
import org.deegree.ogcwebservices.wms.operation.DescribeLayerResult;
import org.w3c.dom.Element;

/**
 * <code>DescribeLayerHandler</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class DescribeLayerHandler {

    /**
     * @param request
     * @param configuration
     * @return a result object with the XML result
     * @throws OGCWebServiceException
     */
    public DescribeLayerResult perform( DescribeLayer request, WMSConfigurationType configuration )
                            throws OGCWebServiceException {
        String[] layers = request.getLayers();

        LinkedList<Layer> ls = new LinkedList<Layer>();
        // check layers for existance
        for ( String l : layers ) {
            Layer layer = configuration.getLayer( l );
            if ( layer == null ) {
                throw new OGCWebServiceException( Messages.getMessage( "WMS_UNKNOWNLAYER", l ) );
            }
            ls.add( layer );
        }

        XMLFragment doc = new XMLFragment( new QualifiedName( "WMS_DescribeLayerResponse" ) );
        Element root = doc.getRootElement();
        root.setAttribute( "version", "1.1.1" );

        for ( Layer l : ls ) {
            Element lay = XMLTools.appendElement( root, null, "LayerDescription" );
            lay.setAttribute( "name", l.getName() );
            AbstractDataSource[] ds = l.getDataSource();
            if ( ds != null ) {
                for ( AbstractDataSource d : ds ) {
                    if ( d instanceof LocalWFSDataSource ) {
                        // lay.setAttribute( "wfs", "unknown" );
                        LocalWFSDataSource wfsds = (LocalWFSDataSource) d;
                        Element e = XMLTools.appendElement( lay, null, "Query" );
                        QualifiedName qn = wfsds.getName();
                        e.setAttribute( "xmlns:" + qn.getPrefix(), qn.getNamespace().toASCIIString() );
                        e.setAttribute( "typeName", qn.getPrefixedName() );
                    }
                }
            }
        }

        return new DescribeLayerResult( request, doc );
    }

}
