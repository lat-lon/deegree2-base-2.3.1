//$HeadURL: $
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

package org.deegree.ogcwebservices.wcts;

import org.deegree.ogcbase.ExceptionCode;

/**
 * <code>WCTSExceptionCode</code> all wcts specific exception codes.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class WCTSExceptionCode extends ExceptionCode {

    /**
     * No inputdata was available from a specified source for input data.
     */
    public static final ExceptionCode NO_INPUT_DATA = new WCTSExceptionCode(
                                                                             "No inputdata was available from a specified source for input data." );

    /**
     * One or more points in InputData are outside the domainOfValidity of the transformation
     */
    public static final ExceptionCode INVALID_AREA = new WCTSExceptionCode(
                                                                            "One or more points in InputData are outside the domainOfValidity of the transformation." );

    /**
     * Operation request contains output CRS that can not be used within output format.
     */
    public static final ExceptionCode UNSUPPORTED_COMBINATION = new WCTSExceptionCode(
                                                                                       "Operation request contains output CRS that can not be used within output format." );

    /**
     * Operation request specifies 'store' result, but not enough storage is available to do this.
     */
    public static final ExceptionCode NOT_ENOUGH_STORAGE = new WCTSExceptionCode(
                                                                                  "Operation request specifies 'store' result, but not enough storage is available to do this." );

    /**
     * @param message
     *            to present to the user.
     */
    public WCTSExceptionCode( String message ) {
        super( message );
    }
}
