//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/xml/XMLParsingException.java $
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
package org.deegree.framework.xml;

import org.deegree.ogcwebservices.OGCWebServiceException;

/**
 * This exception is thrown when a syntactic or semantic error has been encountered during the parsing process of an XML
 * document.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @version $Revision: 12621 $
 */
public class XMLParsingException extends Exception {

    private static final long serialVersionUID = -375766555263169888L;

    private String stackTrace = "<< is empty >>";

    private OGCWebServiceException wrapped;

    /**
     * Creates a new instance of <code>XMLParsingException</code> without detail message.
     */
    XMLParsingException() {
        super( "org.deegree.xml.XMLParsingException" );
        // package protected constructor
    }

    /**
     * Workaround to beat the "no API change" policy.
     * 
     * @param wrapException
     *            ignored parameter, to avoid clashing with the other constructor taking a Throwable.
     * @param wrapped
     */
    public XMLParsingException( boolean wrapException, OGCWebServiceException wrapped ) {
        this.wrapped = wrapped;
    }

    /**
     * Constructs an instance of <code>XMLParsingException</code> with the specified detail message.
     * 
     * @param msg
     *            the detail message
     */
    public XMLParsingException( String msg ) {
        super( msg );
    }

    /**
     * Constructs an instance of <code>XMLParsingException</code> with the specified cause.
     * 
     * @param cause
     *            the Throwable that caused this XMLParsingException
     * 
     */
    public XMLParsingException( Throwable cause ) {
        super( "org.deegree.xml.XMLParsingException", cause );
    }

    /**
     * Constructs an instance of <code>XMLParsingException</code> with the specified detail message.
     * 
     * @param msg
     *            the detail message.
     * @param e
     *            the cause of the exception to be thrown.
     */
    public XMLParsingException( String msg, Throwable e ) {
        this( msg );
        if ( e != null ) {
            StackTraceElement[] se = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            for ( int i = 0; i < se.length; i++ ) {
                sb.append( se[i].getClassName() + " " );
                sb.append( se[i].getFileName() + " " );
                sb.append( se[i].getMethodName() + "(" );
                sb.append( se[i].getLineNumber() + ")\n" );
            }
            stackTrace = e.getMessage() + sb.toString();
        }
    }

    /**
     * @return the wrapped Throwable
     */
    public OGCWebServiceException getWrapped() {
        return wrapped;
    }

    @Override
    public String toString() {
        return getMessage() + "\n" + stackTrace;
    }

}