//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/filterencoding/Operation.java $
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

package org.deegree.model.filterencoding;

import org.deegree.model.feature.Feature;

/**
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 13773 $, $Date: 2008-08-28 16:51:49 +0200 (Do, 28. Aug 2008) $
 */
public interface Operation {

    /**
     * Returns the name of the operator.
     * 
     * @return the name of the operator.
     */
    String getOperatorName();

    /**
     * Returns the operator's id.
     * 
     * @return the operator's id.
     */
    int getOperatorId();

    /**
     * Calculates the <tt>Filter</tt>'s logical value based on the certain property values of the given feature.
     * 
     * @param feature
     *            that determines the values of <tt>PropertyNames</tt> in the expression
     * @return true, if the <tt>Filter</tt> evaluates to true, else false
     * @throws FilterEvaluationException
     *             if the evaluation fails
     */
    public boolean evaluate( Feature feature )
                            throws FilterEvaluationException;

    /**
     * Produces an XML representation of this object.
     * 
     * @return an XML representation of this object.
     */
    public StringBuffer toXML();

    /**
     * Produces an XML representation of this object that complies to Filter Encoding specification 1.0.0.
     * 
     * @return an XML representation of this object
     */
    public StringBuffer to100XML();

    /**
     * Produces an XML representation of this object that complies to Filter Encoding specification 1.1.0.
     * 
     * @return an XML representation of this object
     */
    public StringBuffer to110XML();
}
