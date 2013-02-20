//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/shape/ShapeDatastoreConfiguration.java $
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
package org.deegree.io.datastore.shape;

import java.net.URL;

import org.deegree.io.datastore.DatastoreConfiguration;

/**
 * Represents the configuration for a {@link ShapeDatastore} instance.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11353 $, $Date: 2008-04-22 14:04:35 +0200 (Tue, 22 Apr 2008) $
 */
public class ShapeDatastoreConfiguration implements DatastoreConfiguration {

    private URL file;

    /**
     * Creates a new instance of <code>ShapeDatastoreConfiguration</code> from the given parameters.
     * 
     * @param file
     */
    public ShapeDatastoreConfiguration( URL file ) {
        this.file = file;
    }

    public Class<ShapeDatastore> getDatastoreClass() {
        return ShapeDatastore.class;
    }

    /**
     * Returns the shape file that this datastore operates upon.
     * 
     * @return the shape file that this datastore operates upon
     */
    public URL getFile() {
        return this.file;
    }

    @Override
    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append( ShapeDatastoreConfiguration.class.getName() );
        sb.append( this.file );
        return sb.toString().hashCode();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( !( obj instanceof ShapeDatastoreConfiguration ) ) {
            return false;
        }
        ShapeDatastoreConfiguration that = (ShapeDatastoreConfiguration) obj;
        if ( !this.file.equals( that.file ) ) {
            return false;
        }
        return true;
    }
}