//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/sql/idgenerator/DBSeqIdGenerator.java $
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
package org.deegree.io.datastore.sql.idgenerator;

import java.sql.Connection;
import java.util.Properties;

import org.deegree.io.datastore.Datastore;
import org.deegree.io.datastore.DatastoreException;
import org.deegree.io.datastore.DatastoreTransaction;
import org.deegree.io.datastore.FeatureId;
import org.deegree.io.datastore.idgenerator.IdGenerationException;
import org.deegree.io.datastore.idgenerator.IdGenerator;
import org.deegree.io.datastore.schema.MappedFeatureType;
import org.deegree.io.datastore.schema.MappedGMLId;
import org.deegree.io.datastore.sql.AbstractSQLDatastore;
import org.deegree.io.datastore.sql.transaction.SQLTransaction;

/**
 * Feature id generator that uses an SQL sequence to create new values.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Do, 27. Dez 2007) $
 */
public class DBSeqIdGenerator extends IdGenerator {

    private String sequenceName;

    /**
     * Creates a new <code>DBSeqIdGenerator</code> instance.
     * <p>
     * Supported configuration parameters: <table>
     * <tr>
     * <th>Name</th>
     * <th>optional?</th>
     * <th>Usage</th>
     * </tr>
     * <tr>
     * <td>sequence</td>
     * <td>no</td>
     * <td>name of the SQL sequence to be used</td>
     * </tr>
     * </table>
     * 
     * @param params
     *            configuration parameters
     * @throws IdGenerationException
     */
    public DBSeqIdGenerator( Properties params ) throws IdGenerationException {
        super( params );
        this.sequenceName = params.getProperty( "sequence" );
        if ( this.sequenceName == null ) {
            throw new IdGenerationException( "DBSeqIdGenerator requires 'sequence' parameter." );
        }
    }

    /**
     * Returns the name of the used sequence.
     * 
     * @return the name of the used sequence
     */
    public String getSequenceName() {
        return this.sequenceName;
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
    public Object getNewId( DatastoreTransaction ta )
                            throws IdGenerationException {
        if ( !( ta instanceof SQLTransaction ) ) {
            throw new IllegalArgumentException( "DBSeqIdGenerator can only be used with SQL datastores." );
        }

        Object pk;
        try {
            AbstractSQLDatastore ds = (AbstractSQLDatastore) ta.getDatastore();
            Connection conn = ( (SQLTransaction) ta ).getConnection();
            pk = ds.getSequenceNextVal( conn, this.sequenceName );
        } catch ( DatastoreException e ) {
            throw new IdGenerationException( e.getMessage(), e );
        }
        return pk;
    }

    /**
     * Returns a new id for a feature of the given type.
     * 
     * @param ft
     *            (mapped) feature type
     * @return a new feature id.
     * @throws IdGenerationException
     *             if the generation of the id could not be performed
     */
    @Override
    public FeatureId getNewId( MappedFeatureType ft, DatastoreTransaction ta )
                            throws IdGenerationException {

        MappedGMLId fidDefinition = ft.getGMLId();
        if ( fidDefinition.getKeySize() != 1 ) {
            throw new IdGenerationException( "Cannot generate feature ids that are mapped to "
                                             + fidDefinition.getKeySize() + " columns." );
        }

        Datastore ds = ft.getGMLSchema().getDatastore();
        if ( !( ds instanceof AbstractSQLDatastore ) ) {
            throw new IllegalArgumentException( "DBSeqIdGenerator can only be used with SQL datastores." );
        }

        Object fidNucleus;
        try {
            fidNucleus = ( (AbstractSQLDatastore) ds ).getSequenceNextVal( ( (SQLTransaction) ta ).getConnection(),
                                                                           this.sequenceName );
        } catch ( DatastoreException e ) {
            throw new IdGenerationException( e.getMessage(), e );
        }

        FeatureId fid = new FeatureId( ft, new Object[] { fidNucleus } );
        return fid;
    }
}
