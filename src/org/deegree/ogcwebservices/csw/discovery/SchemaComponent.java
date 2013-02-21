//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/discovery/SchemaComponent.java $
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

import java.net.URI;

import org.deegree.framework.xml.XMLFragment;

/**
 * A schema component includes a schema fragment (type definition) or an entire schema from some
 * target namespace; the schema language is identified by URI. If the component is a schema fragment
 * its parent MUST be referenced (parentSchema).
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */
public class SchemaComponent {

    private XMLFragment schema;

    private URI targetNamespace;

    private URI parentNamespace;

    private URI schemaLanguage;

    /**
     * 
     * @param schema
     * @param targetNamespace
     * @param parentNamespace
     * @param schemaLanguage
     */
    public SchemaComponent( XMLFragment schema, URI targetNamespace, URI parentNamespace, URI schemaLanguage ) {
        this.schema = schema;
        this.targetNamespace = targetNamespace;
        this.parentNamespace = parentNamespace;
        this.schemaLanguage = schemaLanguage;
    }

    /**
     * This is the type description itself (as an <code>XMLFragment</code>).
     * 
     * @return schema
     * 
     */
    public XMLFragment getSchema() {
        return schema;
    }

    /**
     * mandatory
     * 
     * @return target namespace URI
     */
    public URI getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * optional
     * 
     * @return parent schema
     */
    public URI getParentSchema() {
        return parentNamespace;
    }

    /**
     * fixed: XMLSCHEMA
     * 
     * @return schema language
     */
    public URI getSchemaLanguage() {
        return schemaLanguage;
    }

}