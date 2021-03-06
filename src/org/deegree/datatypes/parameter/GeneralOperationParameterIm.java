// $HeadURL:
// /cvsroot/deegree/src/org/deegree/datatypes/parameter/GeneralOperationParameterIm.java,v
// 1.2 2004/08/16 06:23:33 ap Exp $
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
package org.deegree.datatypes.parameter;

import java.io.Serializable;

/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 * @since 2.0
 */
public class GeneralOperationParameterIm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name = null;

    private String remarks = null;

    private int maximumOccurs = 0;

    private int minimumOccurs = 0;

    /**
     * @param name
     * @param remarks
     * @param maximumOccurs
     * @param minimumOccurs
     */
    public GeneralOperationParameterIm( String name, String remarks, int maximumOccurs, int minimumOccurs ) {
        this.name = name;
        this.remarks = remarks;
        this.maximumOccurs = maximumOccurs;
        this.minimumOccurs = minimumOccurs;
    }

    /**
     * @return Returns the maximumOccurs.
     * 
     */
    public int getMaximumOccurs() {
        return maximumOccurs;
    }

    /**
     * @param maximumOccurs
     *            The maximumOccurs to set.
     * 
     */
    public void setMaximumOccurs( int maximumOccurs ) {
        this.maximumOccurs = maximumOccurs;
    }

    /**
     * @return Returns the minimumOccurs.
     * 
     */
    public int getMinimumOccurs() {
        return minimumOccurs;
    }

    /**
     * @param minimumOccurs
     *            The minimumOccurs to set.
     * 
     */
    public void setMinimumOccurs( int minimumOccurs ) {
        this.minimumOccurs = minimumOccurs;
    }

    /**
     * @return Returns the name.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     * 
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * @return Returns the remarks.
     * 
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     *            The remarks to set.
     * 
     */
    public void setRemarks( String remarks ) {
        this.remarks = remarks;
    }

}
