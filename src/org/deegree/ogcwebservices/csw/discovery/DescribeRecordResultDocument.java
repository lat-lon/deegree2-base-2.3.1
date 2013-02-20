//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/discovery/DescribeRecordResultDocument.java $
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

package org.deegree.ogcwebservices.csw.discovery;

import java.io.IOException;
import java.net.URL;

import org.deegree.ogcbase.OGCDocument;
import org.xml.sax.SAXException;

/**
 * Represents an XML DescribeRecordResponse document of an OGC CSW 2.0 compliant service.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * 
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 9499 $, $Date: 2008-01-09 16:47:04 +0100 (Wed, 09 Jan 2008) $
 */

public class DescribeRecordResultDocument extends OGCDocument {

    private static final long serialVersionUID = 2010172386556761252L;

    private static final String XML_TEMPLATE = "DescribeRecordResponseTemplate_2.0.0.xml";

    private static final String XML_TEMPLATE202 = "DescribeRecordResponseTemplate_2.0.2.xml";

    /**
     * Generates a <code>DescribeRecordResponse</code> representation of this object.
     * 
     * @param request
     * 
     * @return a <code>DescribeRecordResponse</code> representation of this object.
     */
    public DescribeRecordResult parseDescribeRecordResponse( DescribeRecord request ) {

        String version = null;
        SchemaComponent[] schemaComponents = null;

        // TODO: Implement me!

        return new DescribeRecordResult( request, version, schemaComponents );
    }

    /**
     * @param version
     * @throws IOException
     * @throws SAXException
     * @see "org.deegree.framework.xml.XMLFragment#createEmptyDocument()"
     */
    public void createEmptyDocument( String version )
                            throws IOException, SAXException {
        String tmpl = version.equals( "2.0.2" ) ? XML_TEMPLATE202 : XML_TEMPLATE;
        URL url = DescribeRecordResultDocument.class.getResource( tmpl );
        if ( url == null ) {
            throw new IOException( "The resource '" + tmpl + " could not be found." );
        }
        load( url );
    }
}
