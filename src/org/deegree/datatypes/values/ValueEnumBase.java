//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/values/ValueEnumBase.java $
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
package org.deegree.datatypes.values;

import java.io.Serializable;

/**
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */

public abstract class ValueEnumBase implements Serializable {

    private TypedLiteral[] singleValue = null;

    private Interval[] interval = null;

    /**
     * @param singleValue
     */
    public ValueEnumBase( TypedLiteral[] singleValue ) throws IllegalArgumentException {
        setSingleValue( singleValue );
    }

    /**
     * @param interval
     */
    public ValueEnumBase( Interval[] interval ) throws IllegalArgumentException {
        setInterval( interval );
    }

    /**
     * @param singleValue
     * @param interval
     */
    public ValueEnumBase( Interval[] interval, TypedLiteral[] singleValue ) throws IllegalArgumentException {
        setSingleValue( singleValue );
        setInterval( interval );
    }

    /**
     * @return Returns the interval.
     * 
     */
    public Interval[] getInterval() {
        return interval;
    }

    /**
     * @param interval
     *            The interval to set.
     * 
     */
    public void setInterval( Interval[] interval )
                            throws IllegalArgumentException {
        if ( interval == null && singleValue == null ) {
            throw new IllegalArgumentException( "at least interval or singleValue must "
                                                + "be <> null in ValueEnumBase" );
        }
        this.interval = interval;
    }

    /**
     * @return Returns the singleValue.
     * 
     */
    public TypedLiteral[] getSingleValue() {
        return singleValue;
    }

    /**
     * @param singleValue
     *            The singleValue to set.
     * 
     */
    public void setSingleValue( TypedLiteral[] singleValue )
                            throws IllegalArgumentException {
        if ( interval == null && singleValue == null ) {
            throw new IllegalArgumentException( "at least interval or singleValue must "
                                                + "be <> null in ValueEnumBase" );
        }
        this.singleValue = singleValue;
    }

}
