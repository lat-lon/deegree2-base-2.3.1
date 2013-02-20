//$HeadURL$
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
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

package org.deegree.io.datastore.wfs;

import java.net.URL;

import org.deegree.framework.xml.XSLTDocument;

/**
 * Describing class for conecting a WFS from a cascading WFS datastore
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class WFSDescription {

    private URL url;

    private XSLTDocument inFilter;

    private XSLTDocument outFilter;

    private int timeout;

    /**
     * 
     * @param url
     *            base URL of cascaded WFS
     * @param inFilter
     *            XSLT script for transforming incoming request; may be <code>null</code>
     * @param outFilter
     *            XSLT script for transforming outgoing response; may be <code>null</code>
     * @param timeout
     *            timeout for request processing
     */
    WFSDescription( URL url, XSLTDocument inFilter, XSLTDocument outFilter, int timeout ) {
        this.url = url;
        this.inFilter = inFilter;
        this.outFilter = outFilter;
        this.timeout = timeout;
    }

    /**
     * @return the inFilter
     */
    public XSLTDocument getInFilter() {
        return inFilter;
    }

    /**
     * @return the outFilter
     */
    public XSLTDocument getOutFilter() {
        return outFilter;
    }

    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

}