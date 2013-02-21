//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/discovery/GetRepositoryItemResponse.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2007 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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

package org.deegree.ogcwebservices.csw.discovery;

import java.net.URI;

import org.deegree.framework.xml.XMLFragment;

/**
 * The <code>GetRepositoryItemResponse</code> wraps the result of a repository item as an xml-dom representation.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 14261 $, $Date: 2008-10-09 12:41:16 +0200 (Do, 09. Okt 2008) $
 * 
 */
public class GetRepositoryItemResponse {

    private final XMLFragment respositoryItem;

    private final URI repositoryItemID;

    private final String id;

    /**
     * @param id
     *            of the request
     * @param repositoryItemID
     *            the requested item
     * @param respositoryItem
     *            the result as an xml-dom element
     */
    public GetRepositoryItemResponse( String id, URI repositoryItemID, XMLFragment respositoryItem ) {
        this.id = id;
        this.repositoryItemID = repositoryItemID;
        this.respositoryItem = respositoryItem;

    }

    /**
     * @return the respositoryItem as an xml-dom element
     */
    public final XMLFragment getRepositoryItem() {
        return respositoryItem;
    }

    /**
     * @return the repositoryItemID
     */
    public final URI getRepositoryItemID() {
        return repositoryItemID;
    }

    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }

}
