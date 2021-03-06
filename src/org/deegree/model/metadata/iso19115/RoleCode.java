//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/metadata/iso19115/RoleCode.java $
/*
 ----------------    FILE HEADER  ------------------------------------------
 
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

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 10547 $, $Date: 2008-03-11 09:40:28 +0100 (Di, 11. Mär 2008) $
 */
public class RoleCode {

    String value = null;

    /**
     * Creates a new instance of RoleCode
     * 
     * @param value
     */
    public RoleCode( String value ) {
        setValue( value );
    }

    /**
     * returns the value-attribute. use="required". Possible value-values are:
     * <ul>
     * <li>contentProvider
     * <li>custodianSteward
     * <li>owner
     * <li>user
     * <li>distributor
     * <li>metadataProvider
     * <li>originator
     * <li>pointOfContact
     * <li>principalInvestigator
     * <li>processor
     * <li>publisher
     * </ul>
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
        ret = "value = " + value + "\n";
        return ret;
    }

}