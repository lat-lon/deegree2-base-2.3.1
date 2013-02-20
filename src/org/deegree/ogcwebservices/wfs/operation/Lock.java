//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wfs/operation/Lock.java $
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
package org.deegree.ogcwebservices.wfs.operation;

import org.deegree.datatypes.QualifiedName;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.schema.FeatureType;
import org.deegree.model.filterencoding.Filter;

/**
 * Represents a <code>wfs:Lock</code> element (usually part of <code>wfs:LockFeature</code>
 * documents).
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 10374 $, $Date: 2008-03-04 09:20:51 +0100 (Tue, 04 Mar 2008) $
 */
public class Lock {

    private String handle;

    private QualifiedName typeName;

    private Filter filter;

    /**
     * Creates a new <code>Lock</code> instance from the given parameters.
     * 
     * @param handle
     * @param typeName
     * @param filter
     */
    public Lock( String handle, QualifiedName typeName, Filter filter ) {
        this.handle = handle;
        this.typeName = typeName;
        this.filter = filter;
    }

    /**
     * Returns the lock's handle.
     * 
     * @return the lock's handle (may be null)
     */
    public String getHandle() {
        return this.handle;
    }

    /**
     * Returns the name of the {@link FeatureType} that is affected by this lock.
     * 
     * @return the name of the <code>FeatureType</code> (never null)
     */
    public QualifiedName getTypeName() {
        return this.typeName;
    }

    /**
     * Returns the filter that is used to select the {@link Feature} instances for locking.
     * 
     * @return the filter that is used to select the <code>Feature<code> instances (may be null)
     */
    public Filter getFilter() {
        return this.filter;
    }
}