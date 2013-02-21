//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/manager/Update.java $
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
package org.deegree.ogcwebservices.csw.manager;

import java.net.URI;
import java.util.List;

import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.filterencoding.Filter;
import org.w3c.dom.Element;

/**
 * An Update object is used to specify values to be used to change existing information in the
 * catalogue. If a complete record instance value is specified then the entire record in the
 * catalogue will be replaced by the value constained in the Update object. If individual record
 * property values are specified in an Update object, then those individual property values of the
 * catalogue record shall be updated.
 * 
 * @version $Revision: 11965 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 11965 $, $Date: 2008-05-28 16:55:15 +0200 (Mi, 28. Mai 2008) $
 * 
 * @since 2.0
 */
public class Update extends Operation {

    private URI typeName = null;

    private Filter constraint = null;

    private Element record = null;

    private List<FeatureProperty> recordProperties = null;

    /**
     * 
     * @param handle
     * @param typeName
     * @param constraint
     * @param record
     * @param recordProperties
     */
    public Update( String handle, URI typeName, Filter constraint, Element record,
                   List<FeatureProperty> recordProperties ) {
        super( "Update", handle );

        this.typeName = typeName;
        this.constraint = constraint;
        this.record = record;
        this.recordProperties = recordProperties;
    }

    /**
     * The number of records affected by an update action is determined by the contents of the
     * constraint.
     * 
     * @return the filter
     */
    public Filter getConstraint() {
        return constraint;
    }

    /**
     * sets the constraint to be considered with an Update operation
     * 
     * @param constraint
     */
    public void setConstraint( Filter constraint ) {
        this.constraint = constraint;
    }

    /**
     * complete metadata record to be updated. can be used as an alternative for recordProperties.
     * 
     * @return the element
     */
    public Element getRecord() {
        return record;
    }

    /**
     * The <csw:RecordProperty> element contains a <csw:Name> element and a <csw:Value> element. The
     * <csw:Name> element is used to specify the name of the record property to be updated. The
     * value of the <csw:Name> element may be a path expression to identify complex properties. The
     * <csw:Value> element contains the value that will be used to update the record in the
     * catalogue.
     * 
     * @return the properties
     */
    public List<FeatureProperty> getRecordProperties() {
        return recordProperties;
    }

    /**
     * The optional typeName attribute may be used to specify the collection name from which records
     * will be updated.
     * 
     * @return the name
     */
    public URI getTypeName() {
        return typeName;
    }

}