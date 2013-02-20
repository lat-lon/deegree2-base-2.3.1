//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/csw/control/CSWRequestFactory.java $
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

package org.deegree.portal.standard.csw.control;

import org.deegree.enterprise.control.RPCStruct;
import org.deegree.portal.standard.csw.CatalogClientException;
import org.deegree.portal.standard.csw.configuration.CSWClientConfiguration;

/**
 * A <code>${type_name}</code> class.<br/>
 * 
 * This class is an abstract class for any CSW request factory. Known extensions are
 * ISO19115RequestFactory, ISO19119RequestFactory.
 * 
 * TODO
 * <ul>
 * <li>add new extensions, when they are created.</li>
 * <li>add new extensions to file requestfactories.properties</li>
 * </ul>
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public abstract class CSWRequestFactory {

    protected CSWClientConfiguration config;

    static final String CONF_DATASERIES = "CONF_DATASERIES";

    /**
     * possible values are: brief, summary, full
     */
    static final String RPC_ELEMENTSETNAME = "RPC_ELEMENTSETNAME";

    static final String RPC_KEYWORDS = "RPC_KEYWORDS";

    /**
     * spec-default is 1
     */
    static final String RPC_STARTPOSITION = "RPC_STARTPOSITION";

    static final String RPC_TOPICCATEGORY = "RPC_TOPICCATEGORY";

    /**
     * possible values are: csw:dataset, csw:dataseries, csw:service, csw:application
     */
    static final String RPC_TYPENAME = "RPC_TYPENAME";

    static final String RPC_DATASERIES = "RPC_DATASERIES";

    /**
     * @param struct
     * @param resultType
     * @return String
     * @throws CatalogClientException
     */
    public abstract String createRequest( RPCStruct struct, String resultType )
                            throws CatalogClientException;

    /**
     * @param config
     */
    public void setConfiguration( CSWClientConfiguration config ) {
        this.config = config;
    }
}
