//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/csw/control/RequestFactoryFinder.java $
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

package org.deegree.portal.standard.csw.control;

import java.io.InputStream;
import java.util.Properties;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.portal.standard.csw.CatalogClientException;

/**
 * A <code>${type_name}</code> class.<br/> TODO class description
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: melmasry $
 * 
 * @version $Revision: 13988 $, $Date: 2008-09-18 14:40:03 +0200 (Do, 18. Sep 2008) $
 */
public class RequestFactoryFinder {

    private static final ILogger LOG = LoggerFactory.getLogger( RequestFactoryFinder.class );

    private static Properties props = new Properties();

    static {
        try {
            InputStream is = RequestFactoryFinder.class.getResourceAsStream( "requestfactories.properties" );
            props.load( is );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage() );
        }
    }

    /**
     * @param profile
     * @return Returns a new <code>CSWRequestFactory</code> object
     * @throws CatalogClientException
     */
    public static CSWRequestFactory findFactory( String profile )
                            throws CatalogClientException {
        String className = props.getProperty( profile );
        LOG.logDebug( "class name for profile: ", profile, " is: ", className );
        try {
            Class clss = Class.forName( className );
            return (CSWRequestFactory) clss.newInstance();
        } catch ( ClassNotFoundException e ) {
            throw new CatalogClientException( Messages.getMessage( "IGEO_STD_CSW_CLASS_NOT_FOUND", className ) );
        } catch ( Exception e ) {
            throw new CatalogClientException( Messages.getMessage( "IGEO_STD_CSW_FAIL_INIT", className ) );
        }
    }

}
