//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/discovery/DescribeRecordResult.java $
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

import org.deegree.ogcwebservices.DefaultOGCWebServiceResponse;

/**
 * Class representation of a <code>DescribeRecordResponse/code>.
 * 
 * @since 2.0
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @version $Revision: 11997 $
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * 
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 2.0, $Revision: 11997 $, $Date: 2008-05-29 09:57:58 +0200 (Do, 29. Mai 2008) $
 */

public class DescribeRecordResult extends DefaultOGCWebServiceResponse {

    private String version = "2.0.0";

    private SchemaComponent[] schemaComponents;

    /**
     * Constructs a new <code>DescribeRecordResponse</code> instance.
     * 
     * @param request
     * @param version
     * @param schemaComponents
     */
    public DescribeRecordResult( DescribeRecord request, String version, SchemaComponent[] schemaComponents ) {
        super( request );
        this.version = version;
        this.schemaComponents = schemaComponents;
    }

    /**
     * The version attribute of the <code>Response</code>.
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * The SchemaComponents may contain any content so that schema language descriptions other than
     * XML Schema may be accommodated
     * 
     * @return the schema components
     */
    public SchemaComponent[] getSchemaComponents() {
        return schemaComponents;
    }

}