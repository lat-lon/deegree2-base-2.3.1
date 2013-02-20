//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_new/Parameter.java $
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
package org.deegree.owscommon_new;

/**
 * <code>Parameter</code> is an abstraction of <code>DomainType</code> according to the OWS
 * common specification 1.0.0. It defines some elementary methods that are self explanatory.
 * 
 * @see DomainType
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */

public abstract class Parameter {

    private boolean optional = false;

    private boolean repeatable = false;

    private String description = null;

    private int direction = 0;

    /**
     * Standard constructor that initializes all encapsulated data.
     * 
     * @param optional
     * @param repeatable
     * @param description
     * @param direction
     */
    public Parameter( boolean optional, boolean repeatable, String description, int direction ) {
        this.optional = optional;
        this.repeatable = repeatable;
        this.description = description;
        this.direction = direction;
    }

    /**
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the direction.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @return whether this parameter is optional.
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * @return whether this parameter is repeatable (?).
     */
    public boolean isRepeatable() {
        return repeatable;
    }

}
