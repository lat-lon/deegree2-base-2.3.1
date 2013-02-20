//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/ParameterNotFoundException.java $
/*$************************************************************************************************
 **
 ** $Id: ParameterNotFoundException.java 9337 2007-12-27 12:31:11Z apoth $
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.deegree.datatypes.parameter;

/**
 * Thrown when a required parameter was not found in a
 * {@linkplain OperationParameterGroupIm parameter group}.
 * 
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9337 $, $Date: 2007-12-27 13:31:11 +0100 (Thu, 27 Dec 2007) $
 */
public class ParameterNotFoundException extends IllegalArgumentException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8074834945993975175L;

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
     *            The required parameter name.
     */
    public ParameterNotFoundException( String message, String parameterName ) {
        super( message );
        this.parameterName = parameterName;
    }

    /**
     * Returns the required parameter name.
     * 
     * @return the required parameter name.
     */
    public String getParameterName() {
        return parameterName;
    }
}
