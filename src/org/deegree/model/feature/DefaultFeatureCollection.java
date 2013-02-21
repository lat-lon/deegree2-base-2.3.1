//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/feature/DefaultFeatureCollection.java $
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
package org.deegree.model.feature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.deegree.datatypes.QualifiedName;
import org.deegree.io.datastore.PropertyPathResolvingException;
import org.deegree.model.feature.schema.FeatureType;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryException;
import org.deegree.ogcbase.PropertyPath;

/**
 * This interface provides services for the management of groups of features. These groups can come into being for a
 * number of reasons: e.g. a project as a whole, for the scope of a query, as the result of a query or arbitrarily
 * selected by a user for some common manipulation. A feature's membership of a particular FeatureCollection does not
 * necessarily imply any relationship with other member features. Composite or compound features which own constituent
 * member Features (e.g. an Airport composed of Terminals, Runways, Aprons, Hangars, etc) may also support the
 * FeatureCollection interface to provide a generic means for clients to access constituent members without needing to
 * be aware of the internal implementation details of the compound feature.
 * <p>
 * -----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 11928 $ $Date: 2008-05-28 09:20:21 +0200 (Mi, 28. Mai 2008) $
 */
class DefaultFeatureCollection extends AbstractFeatureCollection implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 843595367254599228L;

    private List<Feature> collection = new LinkedList<Feature>();

    private Envelope envelope = null;

    DefaultFeatureCollection( String id, int initialCapacity ) {
        super( id );
        collection = new ArrayList<Feature>( initialCapacity );
    }

    /**
     * constructor for initializing a feature collection with an id and an array of features.
     */
    DefaultFeatureCollection( String id, Feature[] features ) {
        this( id, features, null );
    }

    /**
     * Constructor for initializing a feature collection with an id and an array of features. It's name will be taken
     * from the given name.
     * 
     * @param id
     *            of the feature collection
     * @param features
     *            to be added
     * @param qName
     *            of the feature collection, if <code>null</code> the default name of wfs:FeatureCollection will be
     *            given.
     */
    DefaultFeatureCollection( String id, Feature[] features, QualifiedName qName ) {
        super( id, qName );
        if ( features != null ) {
            for ( int i = 0; i < features.length; i++ ) {
                add( features[i] );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.model.feature.FeatureCollection#clear()
     */
    public void clear() {
        collection.clear();
    }

    /**
     * @return the FeatureType of this Feature(Collection)
     */
    @Override
    public FeatureType getFeatureType() {
        return this.featureType;
    }

    /**
     * returns an array of all features
     */
    public Feature[] toArray() {
        return collection.toArray( new Feature[collection.size()] );
    }

    /**
     * returns an <tt>Iterator</tt> on the feature contained in a collection
     * 
     * @return an <tt>Iterator</tt> on the feature contained in a collection
     */
    public Iterator<Feature> iterator() {
        return collection.iterator();
    }

    /**
     * returns the feature that is assigned to the submitted index. If the submitted value for <tt>index</tt> is
     * smaller 0 and larger then the number features within the featurecollection-1 an exeption will be thrown.
     */
    public Feature getFeature( int index ) {
        return collection.get( index );
    }

    /**
     * @return the feature that is assigned to the submitted id. If no valid feature could be found <code>null</code>
     *         will be returned.
     */
    public Feature getFeature( String id ) {
        for ( int i = 0; i < collection.size(); i++ ) {
            Feature feature = collection.get( i );
            if ( feature.getId().equals( id ) ) {
                return feature;
            }
        }
        return null;
    }

    /**
     * removes the submitted feature from the collection
     */
    public Feature remove( Feature feature ) {
        int index = collection.indexOf( feature );
        return remove( index );
    }

    /**
     * removes a feature identified by its index from the feature collection. The removed feature will be returned. If
     * the submitted value for <tt>index</tt> is smaller 0 and larger then the number features within the
     * featurecollection-1 an ArrayIndexOutOfBoundsExcpetion will raise
     */
    public Feature remove( int index ) {
        return collection.remove( index );
    }

    /**
     * Appends a feature to the collection. If the submitted feature doesn't matches the feature type defined for all
     * features within the collection an exception will be thrown.
     */
    public void add( Feature feature ) {
        collection.add( feature );
    }

    /**
     * returns the number of features within the collection
     */
    public int size() {
        return collection.size();
    }

    public void setProperty( FeatureProperty property, int index ) {
        // TODO Auto-generated method stub
    }

    public void addProperty( FeatureProperty property ) {
        // TODO Auto-generated method stub
    }

    public void removeProperty( QualifiedName propertyName ) {
        // TODO Auto-generated method stub
    }

    public void replaceProperty( FeatureProperty oldProperty, FeatureProperty newProperty ) {
        // TODO Auto-generated method stub
    }

    @Override
    public synchronized Envelope getBoundedBy()
                            throws GeometryException {

        Envelope combinedEnvelope = this.envelope;

        if ( combinedEnvelope == null && this.collection.size() > 0 ) {
            // for ( int i = 0; i < this.collection.size(); i++ ) {
            for ( Feature f : this.collection ) {
                Envelope nextFeatureEnvelope = f.getBoundedBy();
                if ( combinedEnvelope == null ) {
                    combinedEnvelope = nextFeatureEnvelope;
                } else if ( nextFeatureEnvelope != null ) {
                    combinedEnvelope = combinedEnvelope.merge( nextFeatureEnvelope );
                }
            }
            this.envelope = combinedEnvelope;
        }
        return combinedEnvelope;
    }

    @Override
    public void setEnvelopesUpdated() {
        this.envelope = null;
    }

    @Override
    public String toString() {
        String ret = null;
        ret = "collection = " + collection + "\n";
        return ret;
    }

    public FeatureProperty getDefaultProperty( PropertyPath path )
                            throws PropertyPathResolvingException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return a shallow clone of a feature collection. Property values of contained features will not be cloned except
     *         for properties that are features (DefaultFeature) or feature collections (DefaultFeatureCollection).
     */
    @Override
    public Object clone() {
        FeatureCollection fc = FeatureFactory.createFeatureCollection( UUID.randomUUID().toString(), collection.size() );
        for ( Feature feature : collection ) {
            fc.add( (Feature) ( (DefaultFeature) feature ).clone() );
        }
        return fc;
    }

}
