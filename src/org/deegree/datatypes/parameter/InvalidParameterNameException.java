//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/InvalidParameterNameException.java $
/*----------------------------------------
 **
 ** $Id: InvalidParameterNameException.java 12519 2008-06-25 09:37:30Z aschmitz $
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 ----------------------------------------*/
package org.deegree.datatypes.parameter;

/**
 * Thrown when an invalid parameter name was requested in a
 *
 * @UML exception GC_InvalidParameterName
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author last edited by: $Author: aschmitz $
 *
 * @version $Revision: 12519 $, $Date: 2008-06-25 11:37:30 +0200 (Mi, 25. Jun 2008) $
 *
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
