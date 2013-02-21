//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/feature/FeatureCollection.java $
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
package org.deegree.model.feature;

import java.util.Iterator;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11349 $, $Date: 2008-04-22 13:49:42 +0200 (Di, 22. Apr 2008) $
 */
public interface FeatureCollection extends Feature {

    /**
     * removes all features from a collection
     * 
     */
    public void clear();

    /**
     * @param index
     *            of the feature.
     * @return the feature at the submitted index
     */
    public Feature getFeature( int index );

    /**
     * @param id
     *            of the feature
     * @return the feature that is assigned to the submitted id. If no valid feature could be found <code>null</code>
     *         will be returned.
     */
    public Feature getFeature( String id );

    /**
     * @return an array of all features
     */
    public Feature[] toArray();

    /**
     * returns an <tt>Iterator</tt> on the feature contained in a collection
     * 
     * @return an <tt>Iterator</tt> on the feature contained in a collection
     */
    public Iterator<Feature> iterator();

    /**
     * adds a feature to the collection
     * 
     * @param feature
     *            to add.
     */
    public void add( Feature feature );

    /**
     * adds a list of features to the collection
     * 
     * @param features
     *            to add.
     */
    public void addAll( Feature[] features );

    /**
     * adds a list of features to the collection
     * 
     * @param featureCollection
     *            to add.
     */
    public void addAll( FeatureCollection featureCollection );

    /**
     * removes the submitted feature from the collection
     * 
     * @param feature
     *            to remove
     * 
     * @return removed feature
     */
    public Feature remove( Feature feature );

    /**
     * removes the feature at the submitted index from the collection
     * 
     * @param index
     *            of the feature to remove.
     * 
     * @return removed feature
     */
    public Feature remove( int index );

    /**
     * removes the feature that is assigned to the submitted id. The removed feature will be returned. If no valid
     * feature could be found null will be returned
     * 
     * @param id
     *            of the feature to remove.
     * 
     * @return removed feature
     */
    public Feature remove( String id );

    /**
     * @return the number of features within the collection
     */
    public int size();
}
