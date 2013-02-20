//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/schema/content/ConstantContent.java $
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
package org.deegree.io.datastore.schema.content;

import org.deegree.io.datastore.schema.MappedSimplePropertyType;

/**
 * Special content class for {@link MappedSimplePropertyType}s that contain constant string
 * values.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Thu, 27 Dec 2007) $
 */
public class ConstantContent extends VirtualContent implements FunctionParam {

    private String constant;

    /**
     * Initializes a newly created <code>ConstantContent</code> object so that it represents
     * the given {@link String} value.
     * 
     * @param constant
     */
    public ConstantContent( String constant ) {
        this.constant = constant;
    }

    /**
     * Returns false, because it makes no sense to use a constant as a sort criterion. 
     * 
     * @return false, because it makes no sense to use a constant as a sort criterion.
     */    
    public boolean isSortable () {
        return false;
    }    
    
    /**
     * Returns the constant {@link String} that this object contains.
     * 
     * @return constant String that this object contains
     */
    public String getValue () {
        return this.constant;
    }
}
