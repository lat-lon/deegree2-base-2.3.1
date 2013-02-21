//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/schema/MappedFeaturePropertyType.java $
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
import org.deegree.model.feature.schema.FeaturePropertyType;

/**
 * Representation of property types that contain features with mapping (persistence) information.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Do, 27. Dez 2007) $
 */
public class MappedFeaturePropertyType extends FeaturePropertyType implements MappedPropertyType {

    private boolean isIdentityPart;

    private TableRelation[] tableRelations;

    private MappedFeatureTypeReference containedFT;

    private boolean isReferenceType;

    /**
     * Constructs a new instance of <code>MappedFeaturePropertyType</code> from the given
     * parameters.
     * 
     * @param name
     * @param typeName
     * @param type
     * @param minOccurs
     * @param maxOccurs
     * @param isIdentityPart
     * @param tableRelations
     * @param containedFT
     * @param isReferenceType
     */
    public MappedFeaturePropertyType( QualifiedName name, QualifiedName typeName, int type,
                                     int minOccurs, int maxOccurs, boolean isIdentityPart,
                                     TableRelation[] tableRelations,
                                     MappedFeatureTypeReference containedFT, boolean isReferenceType ) {
        super( name, typeName, type, minOccurs, maxOccurs );
        this.isIdentityPart = isIdentityPart;
        this.tableRelations = tableRelations;
        this.containedFT = containedFT;
        this.isReferenceType = isReferenceType;
    }

    /**
     * Returns the {@link MappedFeatureTypeReference} to the feature type that is stored in
     * this property.
     * 
     * @return reference to the contained feature type
     */
    public MappedFeatureTypeReference getFeatureTypeReference() {
        return this.containedFT;
    }

    /**
     * Returns the path of {@link TableRelation}s that describe how to get to the table
     * where the content is stored.
     * 
     * @return path of TableRelations, may be null
     */
    public TableRelation[] getTableRelations() {
        return this.tableRelations;
    }

    /**
     * Returns whether this property has to be considered when two instances of the parent feature
     * are checked for equality.
     * 
     * @return true, if this property is part of the feature's identity
     */
    public boolean isIdentityPart() {
        return this.isIdentityPart;
    }

    /**
     * Returns whether this property is of type "gml:ReferenceType" (in which case it's content
     * must be specified by an xlink:href attribute).
     * 
     * @return true, if this property is of type "gml:ReferenceType", false otherwise
     */
    public boolean isReferenceType() {
        return this.isReferenceType;
    }    
}
