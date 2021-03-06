//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/spatialschema/MultiCurve.java $
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

package org.deegree.model.spatialschema;

/**
 * 
 * The interface defines the access to a aggregations of <tt>Curve</tt> objects.
 * 
 * <p>
 * -----------------------------------------------------
 * </p>
 * 
 * @author Andreas Poth
 * @version $Revision: 11448 $ $Date: 2008-04-24 14:55:20 +0200 (Do, 24. Apr 2008) $
 *          <p>
 */
public interface MultiCurve extends MultiPrimitive {
    /**
     * adds a Curve to the aggregation
     * 
     * @param curve
     *            to add.
     */
    public void addCurve( Curve curve );

    /**
     * inserts a Curve in the aggregation. all elements with an index equal or larger index will be moved. if index is
     * larger then getSize() - 1 or smaller then 0 or curve equals null an exception will be thrown.
     * 
     * @param curve
     *            Curve to insert.
     * @param index
     *            position where to insert the new Curve
     * @throws GeometryException
     */
    public void insertCurveAt( Curve curve, int index )
                            throws GeometryException;

    /**
     * sets the submitted Curve at the submitted index. the element at the position <code>index</code> will be
     * removed. if index is larger then getSize() - 1 or smaller then 0 or curve equals null an exception will be
     * thrown.
     * 
     * @param curve
     *            Curve to set.
     * @param index
     *            position where to set the new Curve
     * @throws GeometryException
     */
    public void setCurveAt( Curve curve, int index )
                            throws GeometryException;

    /**
     * removes the submitted Curve from the aggregation
     * 
     * @param curve
     * 
     * @return the removed Curve
     */
    public Curve removeCurve( Curve curve );

    /**
     * removes the Curve at the submitted index from the aggregation. if index is larger then getSize() - 1 or smaller
     * then 0 an exception will be thrown.
     * 
     * @param index
     * 
     * @return the removed Curve
     * @throws GeometryException
     */
    public Curve removeCurveAt( int index )
                            throws GeometryException;

    /**
     * @param index
     * @return the Curve at the submitted index. if index is larger then getSize() - 1 or smaller then 0 an exception
     *         will be thrown.
     */
    public Curve getCurveAt( int index );

    /**
     * @return all Curves as array
     */
    public Curve[] getAllCurves();

}
