//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/base/trunk/resources/eclipse/svn_classfile_header_template.xml $
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

package org.deegree.io.datastore.shape;

import java.net.MalformedURLException;
import java.net.URL;

import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.io.datastore.AnnotationDocument;
import org.w3c.dom.Element;

/**
 * Handles the parsing of {@link ShapeDatastore} annotations.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class ShapeAnnotationDocument extends AnnotationDocument {

    private static final long serialVersionUID = -2801053207484913910L;

    @Override
    public ShapeDatastoreConfiguration parseDatastoreConfiguration()
                            throws XMLParsingException {
        Element appinfoElement = (Element) XMLTools.getRequiredNode( getRootElement(), "xs:annotation/xs:appinfo",
                                                                     nsContext );
        URL file = null;
        String fileName = XMLTools.getRequiredNodeAsString( appinfoElement, "deegreewfs:File/text()", nsContext );
        try {
            file = resolve( fileName );
        } catch ( MalformedURLException e ) {
            throw new XMLParsingException( "File '" + fileName + "' is not a valid URL.", e );
        }
        return new ShapeDatastoreConfiguration( file );
    }
}