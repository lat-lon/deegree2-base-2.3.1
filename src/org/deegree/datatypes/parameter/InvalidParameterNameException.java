//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/InvalidParameterNameException.java $
/*$************************************************************************************************
 **
 ** $Id: InvalidParameterNameException.java 9337 2007-12-27 12:31:11Z apoth $
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.deegree.datatypes.parameter;

/**
 * Thrown when an invalid parameter name was requested in a
 * {@linkplain OperationParameterGroupIm parameter group}.
 * 
 * @UML exception GC_InvalidParameterName
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9337 $, $Date: 2007-12-27 13:31:11 +0100 (Thu, 27 Dec 2007) $
 * 
 * @see OperationParameterGroupIm#getParameter
 * @see ParameterValueGroupIm#getValue
 */
public class InvalidParameterNameException extends IllegalArgumentException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8473266898408204803L;

    /**
     * The invalid parameter name.
     */
    private final String parameterName;

    /**
     * Creates an exception with the specified message and parameter name.
     * 
     * @param message
     *            The detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     * @param parameterName
     *            The invalid parameter name.
     */
    public InvalidParameterNameException( String message, String parameterName ) {
        super( message );
        this.parameterName = parameterName;
    }

    /**
     * Returns the invalid parameter name.
     * 
     * @return the invalid parameter name.
     */
    public String getParameterName() {
        return parameterName;
    }
}
