//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wmps/operation/PrintMapResponse.java $
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

package org.deegree.ogcwebservices.wmps.operation;

import java.util.Date;

/**
 * PrintMapInitialResponse to inform the user if his request the status of his requst before
 * processing. If the request is (not) successfully recieved an appropriate message will be sent to
 * the user.
 * 
 * @author <a href="mailto:deshmukh@lat-lon.de">Anup Deshmukh</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 14304 $, $Date: 2008-10-15 09:57:43 +0200 (Mi, 15. Okt 2008) $
 */

public class PrintMapResponse {

    private String id;

    private Date timeStamp;

    private Date expectedTime;

    private String emailAddress;

    private String exception;

    private String message;

    /**
     * Create an instance of the PrintMapResponse
     * 
     * @param id
     * @param emailAddress
     * @param timeStamp
     * @param expectedTime
     * @param message
     * @param exception
     */
    public PrintMapResponse( String id, String emailAddress, Date timeStamp, Date expectedTime,
                             String message, String exception ) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.timeStamp = timeStamp;
        this.expectedTime = expectedTime;
        this.exception = exception;
        this.message = message;

    }

    /**
     * Get PrintMap Request Id
     * 
     * @return String
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get PrintMap request Email Address
     * 
     * @return String
     */
    public String getEmailAddress() {
        return this.emailAddress;

    }

    /**
     * Get PrintMap request TimeStamp
     * 
     * @return Date
     */
    public Date getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * Get Success/Failed Message for this PrintMap request.
     * 
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get ExpectedTime for the service to process the PrintMap request.
     * 
     * @return Date
     */
    public Date getExpectedTime() {
        return this.expectedTime;
    }

    /**
     * @return Returns the exception.
     */
    public String getException() {
        return this.exception;
    }


}