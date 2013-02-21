//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/displayelements/DisplayElement.java $
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
package org.deegree.graphics.displayelements;

import java.awt.Graphics;

import org.deegree.graphics.transformation.GeoTransform;
import org.deegree.model.feature.Feature;

/**
 * Basic interface for all display elements. A <code>DisplayElement</code> is associated to one
 * {@link Feature} instance that may have a geometry property or not (which is the common case).
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 */
public interface DisplayElement {

    /**
     * Returns the associated {@link Feature}.
     * 
     * @return the associated feature
     */
    Feature getFeature();

    /**
     * Sets the associated {@link Feature}.
     * 
     * @param feature
     */
    void setFeature( Feature feature );

    /**
     * Returns the id of the associated feature.
     * 
     * @return the associated feature's id
     */
    String getAssociateFeatureId();

    /**
     * Renders the <code>DisplayElement</code> to the submitted graphic context.
     * 
     * @param g
     *            graphics context to render to
     * @param projection
     * @param scale
     */
    void paint( Graphics g, GeoTransform projection, double scale );

    /**
     * Sets the selection state of the <code>DisplayElement</code>.
     * 
     * @param selected
     *            true, if the <code>DisplayElement</code> is selected, false otherwise
     */
    void setSelected( boolean selected );

    /**
     * Returns whether the <code>DisplayElement</code> is selected.
     * 
     * @return true, if the <code>DisplayElement</code> is selected, false otherwise
     */
    boolean isSelected();

    /**
     * Sets the highlighting state of the <code>DisplayElement</code>.
     * 
     * @param highlighted
     *            true, if the <code>DisplayElement</code> is highlighted, false otherwise
     */
    void setHighlighted( boolean highlighted );

    /**
     * Returns whether the <code>DisplayElement</code> is highlighted.
     * 
     * @return true, if the <code>DisplayElement</code> is highlighted, false otherwise
     */
    boolean isHighlighted();

    /**
     * Returns whether the <code>DisplayElement</code> should be painted at the given scale.
     * 
     * @param scale
     *            scale to check
     * @return true, if the <code>DisplayElement</code> has to be painted, false otherwise
     */
    boolean doesScaleConstraintApply( double scale );
}
