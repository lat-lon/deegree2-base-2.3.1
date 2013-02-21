//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/sql/postgis/PostGISWhereBuilder.java $
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

package org.deegree.io.datastore.sql.mysql;

import java.sql.Types;

import org.deegree.io.datastore.DatastoreException;
import org.deegree.io.datastore.schema.MappedFeatureType;
import org.deegree.io.datastore.sql.StatementBuffer;
import org.deegree.io.datastore.sql.TableAliasGenerator;
import org.deegree.io.datastore.sql.VirtualContentProvider;
import org.deegree.io.datastore.sql.postgis.PGgeometryAdapter;
import org.deegree.io.datastore.sql.wherebuilder.WhereBuilder;
import org.deegree.model.filterencoding.Filter;
import org.deegree.model.filterencoding.OperationDefines;
import org.deegree.model.filterencoding.SpatialOperation;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.model.spatialschema.GeometryException;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Surface;
import org.deegree.ogcbase.SortProperty;
import org.postgis.PGgeometry;
import org.postgis.binary.BinaryWriter;

/**
 * {@link WhereBuilder} implementation for MySQL spatial databases.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </A>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 12482 $, $Date: 2008-06-23 11:23:34 +0000 (Mo, 23 Jun 2008) $
 */
class MySQLWhereBuilder extends WhereBuilder {

    /**
     * Creates a new instance of <code>MySQLWhereBuilder</code> from the given parameters.
     * 
     * @param rootFts
     *            selected feature types, more than one type means that the types are joined
     * @param aliases
     *            aliases for the feature types, may be null (must have same length as rootFts otherwise)
     * @param filter
     *            filter that restricts the matched features
     * @param sortProperties
     *            sort criteria for the result, may be null or empty
     * @param aliasGenerator
     *            used to generate unique table aliases
     * @param vcProvider
     * @throws DatastoreException
     */
    public MySQLWhereBuilder( MappedFeatureType[] rootFts, String[] aliases, Filter filter,
                              SortProperty[] sortProperties, TableAliasGenerator aliasGenerator,
                              VirtualContentProvider vcProvider ) throws DatastoreException {
        super( rootFts, aliases, filter, sortProperties, aliasGenerator, vcProvider );
    }

    /**
     * Generates an SQL-fragment for the given object.
     * 
     * @throws DatastoreException
     */
    @Override
    protected void appendSpatialOperationAsSQL( StatementBuffer query, SpatialOperation operation )
                            throws DatastoreException {

        try {
            switch ( operation.getOperatorId() ) {
            case OperationDefines.BBOX: {
                appendBBOXOperationAsSQL( query, operation );
                break;
            }
            case OperationDefines.INTERSECTS: {
                appendSimpleOperationAsSQL( query, operation, "Intersects" );
                break;
            }
            case OperationDefines.CROSSES: {
                appendSimpleOperationAsSQL( query, operation, "Crosses" );
                break;
            }
            case OperationDefines.EQUALS: {
                appendSimpleOperationAsSQL( query, operation, "Equals" );
                break;
            }
            case OperationDefines.WITHIN: {
                appendSimpleOperationAsSQL( query, operation, "Within" );
                break;
            }
            case OperationDefines.OVERLAPS: {
                appendSimpleOperationAsSQL( query, operation, "Overlaps" );
                break;
            }
            case OperationDefines.TOUCHES: {
                appendSimpleOperationAsSQL( query, operation, "Touches" );
                break;
            }
            case OperationDefines.DISJOINT: {
                appendSimpleOperationAsSQL( query, operation, "Disjoint" );
                break;
            }
            case OperationDefines.CONTAINS: {
                appendSimpleOperationAsSQL( query, operation, "Contains" );
                break;
            }
            case OperationDefines.DWITHIN: {
                appendDWithinOperationAsSQL( query, operation );
                break;
            }
            case OperationDefines.BEYOND: {
                appendBeyondOperationAsSQL( query, operation );
                break;
            }
            default: {
                String msg = "Spatial operator " + OperationDefines.getNameById( operation.getOperatorId() )
                             + " is not supported by '" + this.getClass().toString() + "'.";
                throw new DatastoreException( msg );
            }
            }
        } catch ( GeometryException e ) {
            throw new DatastoreException( e );
        }
    }

    private void appendSimpleOperationAsSQL( StatementBuffer query, SpatialOperation operation, String operationName ) {
        query.append( operationName );
        query.append( "(" );
        appendPropertyNameAsSQL( query, operation.getPropertyName() );
        query.append( ",GeomFromWKB(?))" );
        query.addArgument( operation.getGeometry(), Types.OTHER );
    }

    private void appendBBOXOperationAsSQL( StatementBuffer query, SpatialOperation operation )
                            throws GeometryException {

        // a polygon is used as parameter here, because MySQL spatial does not seem to offer
        // an envelope type
        Envelope env = operation.getGeometry().getEnvelope();
        Surface surface = GeometryFactory.createSurface( env, env.getCoordinateSystem() );

        query.append( "Intersects(" );
        appendPropertyNameAsSQL( query, operation.getPropertyName() );
        query.append( ",GeomFromWKB(?))" );
        addMySQLGeometryArgument( query, surface );
    }

    private void appendDWithinOperationAsSQL( StatementBuffer query, SpatialOperation operation ) throws GeometryException {
        query.append( "Distance(" );
        appendPropertyNameAsSQL( query, operation.getPropertyName() );
        query.append( ",GeomFromWKB(?))<=" );
        addMySQLGeometryArgument( query, operation.getGeometry() );
        query.append( "" + operation.getDistance() );
    }

    private void appendBeyondOperationAsSQL( StatementBuffer query, SpatialOperation operation ) throws GeometryException {
        query.append( "Distance(" );
        appendPropertyNameAsSQL( query, operation.getPropertyName() );
        query.append( ",GeomFromWKB(?))>" );
        addMySQLGeometryArgument( query, operation.getGeometry() );
        query.append( "" + operation.getDistance() );
    }

    private void addMySQLGeometryArgument( StatementBuffer query, Geometry geometry )
                            throws GeometryException {
        
        PGgeometry pgGeometry = PGgeometryAdapter.export( geometry, -1 );      
        byte[] wkb = new BinaryWriter().writeBinary( pgGeometry.getGeometry() );
        query.addArgument( wkb, org.deegree.datatypes.Types.BINARY );
    }
}