//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/schema/MappedElementDeclaration.java $
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
package org.deegree.io.datastore.schema;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.xml.schema.ElementDeclaration;
import org.deegree.framework.xml.schema.TypeReference;
import org.w3c.dom.Element;

/**
 * Represents an annotated XML element declaration in an {@link MappedGMLSchema}.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Do, 27. Dez 2007) $
 */
public class MappedElementDeclaration extends ElementDeclaration {

    private Element annotationElement;
    
    /**
     * Creates a new <code>ElementDeclaration</code> instance from the given parameters.
     * 
     * @param name
     * @param isAbstract
     * @param type
     * @param minOccurs
     * @param maxOccurs
     * @param substitutionGroup
     * @param annotationElement
     */
    public MappedElementDeclaration( QualifiedName name, boolean isAbstract, TypeReference type, int minOccurs, int maxOccurs,
                              QualifiedName substitutionGroup, Element annotationElement ) {
        super (name, isAbstract, type, minOccurs, maxOccurs, substitutionGroup);
        this.annotationElement = annotationElement;
    }


    /**
     * Returns the "xs:annotation" element (which contains the mapping information).
     * 
     * @return the "xs:annotation" element
     */
    public Element getAnnotation () {
        return this.annotationElement;
    }
}
