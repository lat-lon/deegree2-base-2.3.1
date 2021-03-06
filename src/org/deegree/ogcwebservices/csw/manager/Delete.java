//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/manager/Delete.java $
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

import org.deegree.model.filterencoding.Filter;

/**
 * A Delete object constains a constraint that defines a set of records that are to be deleted from
 * the catalogue. A constraint must be specified in order to prevent every record in the catalogue
 * from inadvertently being deleted.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 11965 $, $Date: 2008-05-28 16:55:15 +0200 (Mi, 28. Mai 2008) $
 */
public class Delete extends Operation {

    private URI typeName = null;

    private Filter constraint = null;

    /**
     * 
     * @param handle
     * @param typeName
     * @param constraint
     */
    public Delete( String handle, URI typeName, Filter constraint ) {
        super( "Delete", handle );
        this.typeName = typeName;
        this.constraint = constraint;
    }

    /**
     * The number of records affected by a delete action is determined by the contents of the
     * constraint.
     * 
     * @return the filter
     */
    public Filter getConstraint() {
        return constraint;
    }

    /**
     * sets the constraint to be considered with a Delete operation
     * 
     * @param constraint
     */
    public void setConstraint( Filter constraint ) {
        this.constraint = constraint;
    }

    /**
     * The typeName attribute is used to specify the collection name from which records will be
     * deleted.
     * 
     * @return the uri
     */
    public URI getTypeName() {
        return typeName;
    }
}