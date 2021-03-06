//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/values/Values.java $
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

import java.net.URI;

/**
 * @version $Revision: 12296 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 12296 $, $Date: 2008-06-11 11:08:58 +0200 (Mi, 11. Jun 2008) $
 * 
 * @since 2.0
 */

public class Values extends ValueEnum implements Cloneable {

    private static final long serialVersionUID = 1L;

    private TypedLiteral default_ = null;

    /**
     * @param interval 
     * @param singleValue
     * @param default_
     */
    public Values( Interval[] interval, TypedLiteral[] singleValue, TypedLiteral default_ ) {
        super( interval, singleValue );
        this.default_ = default_;
    }

    /**
     * @param interval 
     * @param singleValue
     * @param type
     * @param semantic
     * @param default_
     */
    public Values( Interval[] interval, TypedLiteral[] singleValue, URI type, URI semantic, TypedLiteral default_ ) {
        super( interval, singleValue, type, semantic );
        this.default_ = default_;
    }

    /**
     * @return Returns the default_.
     */
    public TypedLiteral getDefault() {
        return default_;
    }

    /**
     * @param default_
     *            The default_ to set.
     */
    public void setDefault( TypedLiteral default_ ) {
        this.default_ = default_;
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        ValueEnum ve = (ValueEnum) super.clone();
        TypedLiteral default__ = null;
        if ( default_ != null ) {
            default__ = (TypedLiteral) default_.clone();
        }
        return new Values( ve.getInterval(), ve.getSingleValue(), ve.getType(), ve.getSemantic(), default__ );
    }

}
