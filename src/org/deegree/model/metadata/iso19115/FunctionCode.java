//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/metadata/iso19115/FunctionCode.java $
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

package org.deegree.model.metadata.iso19115;

import java.io.Serializable;

/**
 * FunctionCode.java
 * 
 * Created on 16. September 2002, 10:02
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:schaefer@lat-lon.de">Axel Schaefer</a>
 * @version $Revision: 9343 $ $Date: 2007-12-27 14:30:32 +0100 (Thu, 27 Dec 2007) $
 */

public class FunctionCode implements Serializable {

    String value = null;

    /**
     * 
     * @param value
     */
    public FunctionCode( String value ) {
        setValue( value );
    }

    /**
     * returns the attribute "value". use="required" Possible values are:
     * <ul>
     * <li>access
     * <li>additionalInformation
     * <li>download
     * <li>order
     * <li>search
     * 
     * @return the value-attribute
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * @see #getValue()
     * @param value
     */
    public void setValue( String value ) {
        this.value = value;
    }

    /**
     * to String method
     * 
     * @return string representation
     */
    public String toString() {
        String ret = null;
        ret = "value = " + value;
        return ret;
    }

}