//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/idgenerator/ParentIDGenerator.java $
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
package org.deegree.io.datastore.idgenerator;

import java.util.Properties;

import org.deegree.io.datastore.DatastoreTransaction;
import org.deegree.io.datastore.FeatureId;
import org.deegree.io.datastore.schema.MappedFeatureType;

/**
 * {@link IdGenerator} that takes the {@link FeatureId} of the parent feature. 
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Do, 27. Dez 2007) $
 */
public class ParentIDGenerator extends IdGenerator {

    /**
     * Creates a new <code>ParentIDGenerator</code> instance.
     * 
     * @param params
     *            configuration parameters
     */    
    public ParentIDGenerator (Properties params) {
        super (params);
    }

    /**
     * Returns a new primary key.
     * 
     * @param ta
     *            datastore transaction (context)
     * @return a new primary key.
     * @throws IdGenerationException
     *             if the generation of the id could not be performed
     */
    @Override
    public String getNewId( DatastoreTransaction ta ) throws IdGenerationException {
        throw new UnsupportedOperationException(
            "ParentIDGenerator cannot be used to generate primary keys (that are no feature ids)." );
    }
    
    /**
     * Returns a new id for a feature of the given type.
     * 
     * @param ft
     *            (mapped) feature type (irrelevant for this generator)
     * @param ta
     *            datastore transaction (context)
     * @return a new feature id.
     * @throws IdGenerationException 
     */
    @Override
    public FeatureId getNewId(  MappedFeatureType ft, DatastoreTransaction ta ) throws IdGenerationException {
        throw new UnsupportedOperationException(
            "ParentIDGenerator cannot be used to generate feature ids (without information on the parent feature." );    
    }
}
