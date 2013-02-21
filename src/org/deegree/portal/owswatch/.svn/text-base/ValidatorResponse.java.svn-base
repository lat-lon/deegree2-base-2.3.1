//$$Header: $$
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

package org.deegree.portal.owswatch;

import java.io.Serializable;
import java.util.Date;

/**
 * Data class that holds the response of execute test that tests a certain service.
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.1 $, $Date: 2008-03-07 14:49:52 $
 */
public class ValidatorResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7602599272924961670L;

    private String message = null;

    private Date lastTest = null;

    private long lastLapse = -1;

    private Status status = null;

    /**
     * Constructor
     * 
     * @param message
     * @param status
     */
    public ValidatorResponse( String message, Status status ) {
        this.message = message;
        this.status = status;
    }

    /**
     * @return lastTest Lapse
     */
    public long getLastLapse() {
        return lastLapse;
    }

    /**
     * @param lastLapse
     */
    public void setLastLapse( long lastLapse ) {
        this.lastLapse = lastLapse;
    }

    /**
     * @return last test Date
     */
    public Date getLastTest() {
        return lastTest;
    }

    /**
     * @param lastTest
     */
    public void setLastTest( Date lastTest ) {
        this.lastTest = lastTest;
    }

    /**
     * @return last message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage( String message ) {
        this.message = message;
    }

    /**
     * @return status of the last test
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus( Status status ) {
        this.status = status;
    }

}
