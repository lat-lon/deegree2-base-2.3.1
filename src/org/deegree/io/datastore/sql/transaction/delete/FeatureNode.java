//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/sql/transaction/delete/FeatureNode.java $
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
package org.deegree.io.datastore.sql.transaction.delete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deegree.io.datastore.FeatureId;
import org.deegree.io.datastore.schema.MappedFeaturePropertyType;
import org.deegree.model.feature.Feature;
import org.deegree.ogcwebservices.csw.manager.Delete;

/**
 * A node of a {@link FeatureGraph}. Represents one {@link Feature} instance which must or must not be deleted during a
 * {@link Delete} operation.
 * 
 * @see FeatureGraph
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 13510 $, $Date: 2008-08-05 16:29:23 +0200 (Di, 05. Aug 2008) $
 */
class FeatureNode {

    private FeatureGraph graph;

    private FeatureId fid;

    private Set<FeatureId> subFids = new HashSet<FeatureId>();

    private Set<FeatureId> superFids;

    private Map<MappedFeaturePropertyType, List<FeatureId>> ptToSubFids;

    private boolean isDeletable = true;

    /**
     * Creates a new <code>FeatureNode</code> instance that represents the feature with the given {@link FeatureId}.
     * 
     * @param graph
     *            <code>FeatureGraph</code> that the node belongs to
     * @param fid
     *            id of the represented feature
     * @param subFeatureProperties
     *            complex property types of the feature and the ids of the subfeatures that they contain
     * @param superFeatures
     *            ids of all features that contain the represented feature as a subfeature
     */
    FeatureNode( FeatureGraph graph, FeatureId fid,
                 Map<MappedFeaturePropertyType, List<FeatureId>> subFeatureProperties, Set<FeatureId> superFeatures ) {
        this.graph = graph;
        this.fid = fid;
        this.ptToSubFids = subFeatureProperties;
        this.superFids = superFeatures;
        for ( Collection<FeatureId> subFids : subFeatureProperties.values() ) {
            this.subFids.addAll( subFids );
        }
    }

    /**
     * Returns the associated {@link FeatureId}.
     * 
     * @return the associated <code>FeatureId</code>
     */
    FeatureId getFid() {
        return this.fid;
    }

    /**
     * Returns the ids of all subfeatures of the represented feature.
     * 
     * @return the ids of all subfeatures of the represented feature
     */
    Set<FeatureId> getSubFeatureIds() {
        return this.subFids;
    }

    /**
     * Returns the ids of all subfeatures stored in the specified property of the represented feature.
     * 
     * @param pt
     *            property type
     * @return the ids of all subfeatures of the represented feature
     */
    List<FeatureId> getSubFeatureIds( MappedFeaturePropertyType pt ) {
        return this.ptToSubFids.get( pt );
    }

    /**
     * Returns all subfeatures of the represented feature.
     * 
     * @return all subfeatures of the represented feature
     */
    List<FeatureNode> getSubFeatures() {
        List<FeatureNode> subFeatures = new ArrayList<FeatureNode>( this.subFids.size() );
        for ( FeatureId subFid : this.subFids ) {
            subFeatures.add( this.graph.getNode( subFid ) );
        }
        return subFeatures;
    }

    /**
     * Returns the ids of all superfeatures of the represented feature (features that contain the represented feature as
     * a subfeature).
     * 
     * @return the ids of all superfeatures of the represented feature
     */
    Set<FeatureId> getSuperFeatureIds() {
        return this.superFids;
    }

    /**
     * Returns whether the represented feature may be deleted.
     * 
     * @return true, if the represented feature may be deleted, otherwise false
     */
    boolean isDeletable() {
        return this.isDeletable;
    }

    /**
     * Marks this feature as undeletable. This is also applied to all it's descendant subfeatures.
     */
    void markAsUndeletable() {
        if ( this.isDeletable ) {
            this.isDeletable = false;
            for ( FeatureId subFid : this.subFids ) {
                this.graph.getNode( subFid ).markAsUndeletable();
            }
        }
    }

    @Override
    public int hashCode() {
        return this.fid.hashCode();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null || !( obj instanceof FeatureNode ) ) {
            return false;
        }
        return this.fid.equals( obj );
    }

    /**
     * Generates an indented string representation of this <code>FeatureNode</code> and all it's descendant subfeatures.
     * 
     * @param indent
     *            current indentation (string consisting of spaces)
     * @param printedNodes
     *            <code>FeatureNode</code>s that have all ready been encountered (to avoid endless recursion)
     * @return an indented string representation
     */
    String toString( String indent, Set<FeatureNode> printedNodes ) {
        StringBuffer sb = new StringBuffer();
        if ( !printedNodes.contains( this ) ) {
            printedNodes.add( this );
            sb.append( indent );
            sb.append( "- " + this.fid + ", super fids: [" );
            for ( FeatureId superFid : superFids ) {
                sb.append( superFid );
                sb.append( " " );
            }
            sb.append( "], deletable: " + this.isDeletable + "\n" );
            for ( MappedFeaturePropertyType pt : this.ptToSubFids.keySet() ) {
                for ( FeatureId subFid : this.ptToSubFids.get( pt ) ) {
                    sb.append( indent );
                    sb.append( " + " );
                    sb.append( pt.getName().getLocalName() );
                    sb.append( ":\n" );
                    FeatureNode subNode = this.graph.getNode( subFid );
                    sb.append( subNode.toString( "  " + indent, printedNodes ) );
                }
            }
        } else {
            sb.append( indent );
            sb.append( "- " + this.fid + " (already printed)\n" );
        }
        return sb.toString();
    }
}