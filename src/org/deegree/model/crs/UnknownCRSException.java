//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/crs/UnknownCRSException.java $
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
package org.deegree.model.crs;

import org.deegree.i18n.Messages;

/**
 * 
 * The <code>UnknownCRSException</code> class just wraps a message telling the user, his/her
 * requested CRS is not known to the CRSPovider.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 */
public class UnknownCRSException extends Exception {

    private static final String MSG = Messages.getMessage( "CRS_UNKNOWNCRS_DEFAULT" );

    /**
     * the serializable id.
     */
    private static final long serialVersionUID = 2343417746783122437L;

    /**
     * Creates a simple Exception telling the user that the requested CoordinateSystem is unknown.
     * 
     * @param crsName
     *            the name of the requested crs.
     */
    public UnknownCRSException( String crsName ) {
        super( MSG + crsName );
    }
}