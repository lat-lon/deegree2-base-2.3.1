//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/Pair.java $
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
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
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

package org.deegree.framework.util;

/**
 * <code>Pair</code> is a convenience class, which pairs two objects.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * @param <T>
 *            the first Object of the pair
 * @param <U>
 *            the second Object of the pair
 * 
 */

public class Pair<T, U> {
    /**
     * first value of the pair.
     */
    public T first;

    /**
     * second value of the pair.
     */
    public U second;

    /**
     * @param first
     *            value of the pair.
     * @param second
     *            value of the pair.
     */
    public Pair( T first, U second ) {
        this.first = first;
        this.second = second;
    }

    /**
     * Create a pair with null objects.
     */
    public Pair() {
        // nothing to do here
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals( Object other ) {
        if ( other != null && other instanceof Pair ) {
            // what ever, unchecked.
            final Pair that = (Pair) other;
            return ( first == null ? that.first == null : first.equals( that.first ) )
                   && ( second == null ? that.second == null : second.equals( that.second ) );
        }
        return false;
    }

    @Override
    public String toString() {
        return "<" + first + ", " + second + ">";
    }

}
