// $HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/PortalException.java $
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
package org.deegree.portal;


/**
 * 
 * 
 * @version $Revision: 11011 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 11011 $, $Date: 2008-04-10 09:52:41 +0200 (Thu, 10 Apr 2008) $
 * 
 * @since 1.1
 */
public class PortalException extends java.lang.Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 2653650363758544846L;
    
    protected String st = "ClientException";

    /**
     * Creates a new instance of <code>ClientException</code> without detail message.
     */
    public PortalException() {
        //nottin
    }

    /**
     * Constructs an instance of <code>ClientException</code> with the specified detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public PortalException( String msg ) {
        super( msg );
    }

    /**
     * Constructs an instance of <code>ClientException</code> with the specified detail message.
     * 
     * @param msg
     *            the detail message.
     * @param e the cause
     */
    public PortalException( String msg, Exception e ) {
        //this( msg );
        super( msg, e );
        //st = StringTools.stackTraceToString( e.getStackTrace() );
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + getMessage();
    }
}