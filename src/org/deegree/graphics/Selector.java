//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/Selector.java $
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

package org.deegree.graphics;

import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.Position;

/**
 * 
 * <p>
 * ------------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 12062 $ $Date: 2008-06-02 10:37:28 +0200 (Mo, 02. Jun 2008) $
 */
public interface Selector {

    /**
     * adds a Theme to the Selector that shall be notified if something happens.
     * @param theme to add.
     */
    void addTheme( Theme theme );

    /**
     * @param theme to be removed
     * @see Selector#addTheme(Theme)
     */
    void removeTheme( Theme theme );

    /**
     * selects all features (display elements) that are located within the submitted bounding box.
     * @param boundingbox to select all features within
     * 
     * @return id's of the selected features (display elements)
     */
    String[] select( Envelope boundingbox );

    /**
     * selects all features (display elements) that intersects the submitted point. if a feature is
     * already selected it remains selected.
     * @param position to select the features with.
     * 
     * @return id's of the selected features (display elements)
     */
    String[] select( Position position );

    /**
     * selects all features (display elements) that are located within the circle described by the
     * position and the radius. if a feature is already selected it remains selected.
     * @param position to select al features
     * @param radius around the given position
     * 
     * @return id's of the selected features (display elements)
     */
    String[] select( Position position, double radius );

    /**
     * selects all features (display elements) that are specified by the submitted ids. if a feature
     * is already selected it remains selected.
     * @param ids to select
     * 
     * @return ids of the selected features (display elements)
     */
    String[] select( String[] ids );

    /**
     * inverts the current selection.
     * @return the id's of the inverted selection
     */
    String[] invertSelection();

    /**
     * mark all features as unselected
     */
    void reset();

}
