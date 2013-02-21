//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/sql/transaction/delete/FeatureGraph.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
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
package org.deegree.io.datastore.sql;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.io.datastore.DatastoreException;
import org.deegree.io.datastore.FeatureId;
import org.deegree.io.datastore.schema.MappedFeatureType;
import org.deegree.io.datastore.sql.transaction.delete.FeatureGraph;
import org.deegree.ogcwebservices.wfs.operation.Lock;
import org.deegree.ogcwebservices.wfs.operation.LockFeature;

/**
 * Responsible for the handling of {@link LockFeature} requests.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class LockHandler extends AbstractRequestHandler {

    private static final ILogger LOG = LoggerFactory.getLogger( LockHandler.class );

    private List<Lock> requestParts;

    /**
     * Creates a new <code>LockHandler</code> from the given parameters.
     * 
     * @param ds
     * @param aliasGenerator
     * @param conn
     * @param requestParts
     */
    LockHandler( AbstractSQLDatastore ds, TableAliasGenerator aliasGenerator, Connection conn, List<Lock> requestParts ) {
        super( ds, aliasGenerator, conn );
        this.requestParts = requestParts;
    }

    /**
     * Determines all {@link FeatureId}s that have to be locked.
     * 
     * @return all <code>FeatureId</code>s that have to be locked
     * @throws DatastoreException
     */
    Set<FeatureId> determineFidsToLock()
                            throws DatastoreException {

        Set<FeatureId> rootFids = determineRootFids();
        if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
            LOG.logDebug( "Root features to be locked: " );
            for ( FeatureId id : rootFids ) {
                LOG.logDebug( id.getAsString() );
            }
        }

        // build the feature graph to determine all descendant features
        FeatureGraph featureGraph = new FeatureGraph( rootFids, this );
        Set<FeatureId> lockedFids = new TreeSet<FeatureId>();
        lockedFids.addAll( featureGraph.getAllFids() );

        // also add ids of super features (and super-super features, etc.)
        addSuperFids( lockedFids );

        return lockedFids;
    }

    private void addSuperFids( Set<FeatureId> fids )
                            throws DatastoreException {
        FeatureId[] origFids = fids.toArray( new FeatureId[fids.size()] );
        for ( FeatureId fid : origFids ) {
            Set<FeatureId> superFids = determineSuperFeatures( fid );
            for ( FeatureId superFid : superFids ) {
                addSuperFids( superFid, fids );
            }
        }
    }

    private void addSuperFids( FeatureId fid, Set<FeatureId> fids )
                            throws DatastoreException {
        if ( !fids.contains( fid ) ) {
            fids.add( fid );
            Set<FeatureId> superFids = determineSuperFeatures( fid );
            for ( FeatureId superFid : superFids ) {
                addSuperFids( superFid, fids );
            }
        }
    }

    /**
     * Determines all root features that have to be locked by the associated {@link LockFeature}
     * request (and that are served by the associated {@link AbstractSQLDatastore}.
     * <p>
     * NOTE: The returned set only contains the feature ids that are <b>directly</b> targeted by
     * the request, but not necessarily all the subfeatures or superfeatures that have to be locked
     * as well.
     * 
     * @return <b>directly</b> affected feature ids
     * @throws DatastoreException
     */
    private Set<FeatureId> determineRootFids()
                            throws DatastoreException {
        Set<FeatureId> fids = new HashSet<FeatureId>();
        for ( Lock lock : this.requestParts ) {
            QualifiedName ftName = lock.getTypeName();
            MappedFeatureType ft = this.datastore.getFeatureType( ftName );
            if ( ft != null ) {
                fids.addAll( determineAffectedFIDs( ft, lock.getFilter() ) );
            }
        }
        return fids;
    }
}