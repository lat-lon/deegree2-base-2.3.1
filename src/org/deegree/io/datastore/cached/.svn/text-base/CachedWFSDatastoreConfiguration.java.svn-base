//$HeadURL: svn+ssh://developername@svn.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/memory/MemoryWFSDatastoreConfiguration.java $
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
package org.deegree.io.datastore.cached;

import java.net.URL;

import org.deegree.datatypes.QualifiedName;
import org.deegree.io.datastore.DatastoreConfiguration;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 7468 $, $Date: 2007-06-05 16:33:13 +0200 (Di, 05 Jun 2007) $
 */
public class CachedWFSDatastoreConfiguration implements DatastoreConfiguration {

    private QualifiedName featureType;
    private URL schemLocation;

    /**
     * 
     * @param featureType
     * @param url schema location
     */
    public CachedWFSDatastoreConfiguration( QualifiedName featureType, URL url ) {
        this.featureType = featureType;
        this.schemLocation = url;
    }

    /**
     * @return datastore class
     */
    public Class<CachedWFSDatastore> getDatastoreClass() {
        return CachedWFSDatastore.class;
    }
    
    /**
     * 
     * @return name of the cached feature type
     */
    public QualifiedName getFeatureType() {
        return featureType;
    }
    
    /**
     * 
     * @return schema location
     */
    public URL getSchemaLocation() {
        return schemLocation;
    }
}