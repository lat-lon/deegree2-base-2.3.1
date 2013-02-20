// $HeadURL: /cvsroot/deegree/src/org/deegree/ogcbase/OGCDocument.java,v 1.4
// 2004/06/21 08:05:49 ap Exp $
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

/**
 * XmlException
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </A>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9339 $, $Date: 2007-12-27 13:31:52 +0100 (Thu, 27 Dec 2007) $
 */
public class XMLException extends RuntimeException {

    protected java.lang.Exception innerException;

    protected String message;

    /**
     * @param text
     * 
     */
    public XMLException( String text ) {
        innerException = null;
        message = text;
    }

    /**
     * @param other
     * 
     */
    public XMLException( java.lang.Exception other ) {
        innerException = other;
        message = other.getMessage();
    }

    /**
     * @see java.lang.Throwable#getMessage()
     * 
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return innerException
     * 
     */
    public java.lang.Exception getInnerException() {
        return innerException;
    }

}
