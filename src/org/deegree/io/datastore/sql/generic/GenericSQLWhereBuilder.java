//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/sql/generic/GenericSQLWhereBuilder.java $
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
package org.deegree.io.datastore.sql.generic;

import java.sql.Types;
import java.util.List;

import org.deegree.i18n.Messages;
import org.deegree.io.JDBCConnection;
import org.deegree.io.datastore.DatastoreException;
import org.deegree.io.datastore.schema.MappedFeatureType;
import org.deegree.io.datastore.schema.content.MappingField;
import org.deegree.io.datastore.sql.StatementBuffer;
import org.deegree.io.datastore.sql.TableAliasGenerator;
import org.deegree.io.datastore.sql.VirtualContentProvider;
import org.deegree.io.datastore.sql.wherebuilder.WhereBuilder;
import org.deegree.io.quadtree.DBQuadtree;
import org.deegree.io.quadtree.DBQuadtreeManager;
import org.deegree.io.quadtree.IndexException;
import org.deegree.io.quadtree.Quadtree;
import org.deegree.model.filterencoding.Filter;
import org.deegree.model.filterencoding.OperationDefines;
import org.deegree.model.filterencoding.SpatialOperation;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.ogcbase.SortProperty;

/**
 * {@link WhereBuilder} implementation for the {@link GenericSQLDatastore}.
 * <p>
 * Uses the {@link Quadtree} to speed up BBOX queries.
 * 
 * @see org.deegree.io.quadtree
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 10506 $, $Date: 2008-03-06 17:50:33 +0100 (Thu, 06 Mar 2008) $
 */
class GenericSQLWhereBuilder extends WhereBuilder {

    private final static String SQL_TRUE = "1=1";

    private final static String SQL_FALSE = "1!=1";

    private JDBCConnection jdbc;

    /**
     * Creates a new instance of <code>GenericSQLWhereBuilder</code> from the given parameters.
     * 
     * @param rootFts
     *            selected feature types, more than one type means that the types are joined
     * @param aliases
     *            aliases for the feature types, may be null (must have same length as rootFts
     *            otherwise)
     * @param filter
     *            filter that restricts the matched features
     * @param sortProperties
     *            sort criteria for the result, may be null or empty
     * @param aliasGenerator
     *            used to generate unique table aliases
     * @param vcProvider
     * @param jdbc
     * @throws DatastoreException
     */
    public GenericSQLWhereBuilder( MappedFeatureType[] rootFts, String[] aliases, Filter filter,
                                   SortProperty[] sortProperties, TableAliasGenerator aliasGenerator,
                                   VirtualContentProvider vcProvider, JDBCConnection jdbc ) throws DatastoreException {
        super( rootFts, aliases, filter, sortProperties, aliasGenerator, vcProvider );
        this.jdbc = jdbc;
    }

    /**
     * Generates an SQL-fragment for the given object.
     * 
     * TODO: Implement BBOX faster using explicit B0X-constructor
     * 
     * @throws DatastoreException
     */
    @Override
    protected void appendSpatialOperationAsSQL( StatementBuffer query, SpatialOperation operation )
                            throws DatastoreException {

        switch ( operation.getOperatorId() ) {
        case OperationDefines.BBOX: {
            appendBBOXOperationAsSQL( query, operation );
            break;
        }
        case OperationDefines.DISJOINT:
        case OperationDefines.CROSSES:
        case OperationDefines.EQUALS:
        case OperationDefines.WITHIN:
        case OperationDefines.OVERLAPS:
        case OperationDefines.TOUCHES:
        case OperationDefines.CONTAINS:
        case OperationDefines.INTERSECTS:
        case OperationDefines.DWITHIN:
        case OperationDefines.BEYOND: {
            query.append( SQL_TRUE );
            break;
        }
        default: {
            String msg = Messages.getMessage( "DATASTORE_UNKNOWN_SPATIAL_OPERATOR",
                                              OperationDefines.getNameById( operation.getOperatorId() ) );
            throw new DatastoreException( msg );
        }
        }
    }

    /**
     * Appends a constraint (FEATURE_ID IN (...)) to the given {@link StatementBuffer} which is
     * generated by using the associated {@link DBQuadtree} index.
     * 
     * @param query
     * @param operation
     * @throws DatastoreException
     */
    private void appendBBOXOperationAsSQL( StatementBuffer query, SpatialOperation operation )
                            throws DatastoreException {

        Envelope env = operation.getGeometry().getEnvelope();

        try {
            DBQuadtreeManager<?> qtm = new DBQuadtreeManager<Object>( jdbc, this.rootFts[0].getTable(), "geometry", null, Integer.MIN_VALUE );
            Object type = qtm.determineQuattreeType();
            int dataType = Types.VARCHAR;
            if( type instanceof Integer ){
                LOG.logDebug( "The elements of the quadtree are of type Integer.");
                qtm = new DBQuadtreeManager<Integer>( jdbc, this.rootFts[0].getTable(), "geometry", null, Types.INTEGER );
                dataType = Types.INTEGER;
            } else if ( type instanceof String ){
                LOG.logDebug( "The elements of the quadtree are of type String.");
                qtm = new DBQuadtreeManager<String>( jdbc, this.rootFts[0].getTable(), "geometry", null, Types.INTEGER );
            }            
            
            Envelope qtEnv = qtm.getQuadtree().getRootBoundingBox();
            if ( qtEnv.intersects( env ) ) {
                // check if features within this bbox are available
                // if not -> return an empty list
                List<?> ids = qtm.getQuadtree().query( env );
                if ( ids.size() > 0 ) {

                    MappingField[] idFields = this.rootFts[0].getGMLId().getIdFields();
                    if ( idFields.length > 1 ) {
                        String msg = "GenericSQLDatastore cannot handle composite feature ids.";
                        throw new DatastoreException( msg );
                    }

                    query.append( getRootTableAlias(0) + '.' + idFields[0].getField() + " IN (" );
                    for ( int i = 0; i < ids.size() - 1; i++ ) {
                        query.append( "?," );
                        if ( dataType == Types.VARCHAR ) {
                            query.addArgument( ( "" + ids.get( i ) ).trim(), Types.VARCHAR );
                        } else {
                            query.addArgument( ids.get( i ), Types.INTEGER );
                        }
                    }
                    if ( dataType == Types.VARCHAR ) {
                        query.addArgument( ( "" + ids.get( ids.size() - 1 ) ).trim(), Types.VARCHAR );
                    } else {
                        query.addArgument( ids.get( ids.size() - 1 ), Types.INTEGER );
                    }
                    query.append( "?)" );
                } else {
                    query.append( SQL_FALSE );
                }
            } else {
                query.append( SQL_FALSE );
            }
        } catch ( IndexException e ) {
            LOG.logError( e.getMessage(), e );
            throw new DatastoreException( "Could not append bbox operation as sql into the Quadtree: " + e.getMessage(), e);
        }
    }
}