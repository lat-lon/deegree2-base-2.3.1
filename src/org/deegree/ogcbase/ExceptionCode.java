// $HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcbase/ExceptionCode.java $
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
package org.deegree.ogcbase;

/**
 * @version $Revision: 12071 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 12071 $, $Date: 2008-06-02 13:46:56 +0200 (Mo, 02. Jun 2008) $
 */

public class ExceptionCode {

    /**
     * The service is not initialized
     */
    public static final ExceptionCode INTERNAL_SERVER_ERROR = new ExceptionCode( "Internal server error" );

    /**
     * The soap element with the mustUnderstand attribute is not understood by the soap handler
     */
    public static final ExceptionCode SOAP_MUST_UNDERSTAND = new ExceptionCode( "MustUnderstand" );

    /**
     * The soap exception code which flags that the semantic of the request was not understood (i.e.
     * an OGCWebserviceException)
     */
    public static final ExceptionCode SOAP_SERVER = new ExceptionCode( "Server" );

    /**
     * The soap exception code which flags The Client class of errors indicate that the message was
     * incorrectly formed or did not contain the appropriate information in order to succeed. For
     * example, the message could lack the proper authentication or payment information.
     */
    public static final ExceptionCode SOAP_CLIENT = new ExceptionCode( "Client" );

    /**
     * 
     */
    public static final ExceptionCode INVALID_FORMAT = new ExceptionCode( "InvalidFormat" );

    /**
     * 
     */
    public static final ExceptionCode INVALID_UPDATESEQUENCE = new ExceptionCode( "InvalidUpdateSequence" );

    /**
     * 
     */
    public static final ExceptionCode CURRENT_UPDATE_SEQUENCE = new ExceptionCode( "CurrentUpdateSequence" );

    /**
     * 
     */
    public static final ExceptionCode MISSINGPARAMETERVALUE = new ExceptionCode( "MissingParameterValue" );

    /**
     * 
     */
    public static final ExceptionCode INVALIDPARAMETERVALUE = new ExceptionCode( "InvalidParameterValue" );

    /**
     * 
     */
    public static final ExceptionCode OPERATIONNOTSUPPORTED = new ExceptionCode( "OperationNotSupported" );

    /**
     * 
     */
    public static final ExceptionCode VERSIONNEGOTIATIONFAILED = new ExceptionCode( "VersionNegotiationFailed" );

    /**
     * 
     */
    public static final ExceptionCode NOAPPLICABLECODE = new ExceptionCode( "NoApplicableCode" );

    /**
     * 
     */
    public static final ExceptionCode LAYER_NOT_DEFINED = new ExceptionCode( "LayerNotDefined" );

    /**
     * 
     */
    public static final ExceptionCode STYLE_NOT_DEFINED = new ExceptionCode( "StyleNotDefined" );

    /**
     * 
     */
    public static final ExceptionCode INVALID_SRS = new ExceptionCode( "InvalidSRS" );

    /**
     * 
     */
    public static final ExceptionCode INVALID_CRS = new ExceptionCode( "InvalidCRS" );

    /**
     * 
     */
    public static final ExceptionCode LAYER_NOT_QUERYABLE = new ExceptionCode( "LayerNotQueryable" );

    /**
     * 
     */
    public static final ExceptionCode INVALID_POINT = new ExceptionCode( "InvalidPoint" );

    /**
     * 
     */
    public String value = "InvalidFormat";

    /**
     * default value = TC211, (InvalidFormat)
     */
    public ExceptionCode() {
        // nothing
    }

    /**
     * @param value
     */
    public ExceptionCode( String value ) {
        this.value = value;
    }

    /**
     * Compares the specified object with this class for equality.
     */
    @Override
    public boolean equals( Object object ) {
        if ( object != null && getClass().equals( object.getClass() ) ) {
            return ( (ExceptionCode) object ).value.equals( value );
        }
        return false;
    }
}
