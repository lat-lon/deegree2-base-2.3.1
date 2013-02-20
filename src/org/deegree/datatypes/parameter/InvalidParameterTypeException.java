//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/InvalidParameterTypeException.java $
/*$************************************************************************************************
 **
 ** $Id: InvalidParameterTypeException.java 9337 2007-12-27 12:31:11Z apoth $
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.deegree.datatypes.parameter;

/**
 * Thrown when a parameter can't be cast to the requested type. For example this exception is thrown
 * when {@link ParameterValueIm#doubleValue} is invoked but the value is not convertible to a
 * <code>double</code>.
 * 
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9337 $, $Date: 2007-12-27 13:31:11 +0100 (Thu, 27 Dec 2007) $
 * 
 * @see ParameterValueIm#intValue
 * @see ParameterValueIm#doubleValue
 * 
 */
public class InvalidParameterTypeException extends IllegalStateException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 2740762597003093176L;

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
     *            The parameter name.
     */
    public InvalidParameterTypeException( String message, String parameterName ) {
        super( message );
        this.parameterName = parameterName;
    }

    /**
     * Returns the parameter name.
     * 
     * @return the parameter name.
     */
    public String getParameterName() {
        return parameterName;
    }
}
