//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/InvalidParameterValueException.java $
/*$************************************************************************************************
 **
 ** $Id: InvalidParameterValueException.java 9337 2007-12-27 12:31:11Z apoth $
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.deegree.datatypes.parameter;

/**
 * Thrown when an invalid value was given to a {@linkplain ParameterValueIm parameter}.
 * 
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9337 $, $Date: 2007-12-27 13:31:11 +0100 (Thu, 27 Dec 2007) $
 * 
 * @see ParameterValueIm#setValue(int)
 * @see ParameterValueIm#setValue(double)
 * @see ParameterValueIm#setValue(Object)
 */
public class InvalidParameterValueException extends IllegalArgumentException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3814037056147642789L;

    /**
     * The parameter name.
     */
    private final String parameterName;

    /**
     * The invalid parameter value.
     */
    private final Object value;

    /**
     * Creates an exception with the specified invalid value.
     * 
     * @param message
     *            The detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     * @param parameterName
     *            The parameter name.
     * @param value
     *            The invalid parameter value.
     */
    public InvalidParameterValueException( String message, String parameterName, Object value ) {
        super( message );
        this.parameterName = parameterName;
        this.value = value;
    }

    /**
     * Creates an exception with the specified invalid value as a floating point.
     * 
     * @param message
     *            The detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     * @param parameterName
     *            The parameter name.
     * @param value
     *            The invalid parameter value.
     */
    public InvalidParameterValueException( String message, String parameterName, double value ) {
        this( message, parameterName, new Double( value ) );
    }

    /**
     * Creates an exception with the specified invalid value as an integer.
     * 
     * @param message
     *            The detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     * @param parameterName
     *            The parameter name.
     * @param value
     *            The invalid parameter value.
     */
    public InvalidParameterValueException( String message, String parameterName, int value ) {
        this( message, parameterName, new Integer( value ) );
    }

    /**
     * Returns the parameter name.
     * 
     * @return the parameter name.
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Returns the invalid parameter value.
     * 
     * @return the invalid parameter value.
     */
    public Object getValue() {
        return value;
    }
}
