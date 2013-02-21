// $HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/configuration/WCSDeegreeParams.java $
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
package org.deegree.ogcwebservices.wcs.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deegree.enterprise.DeegreeParams;
import org.deegree.model.metadata.iso19115.OnlineResource;

/**
 * 
 * 
 * @version $Revision: 9345 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */
public class WCSDeegreeParams extends DeegreeParams {

    private List<String> directoryList = new ArrayList<String>();

    /**
     * creates an instance of a class containing the deegree specific global configuration
     * parameters for a WCS
     * 
     * @param defaultOnlineResource
     *            URL/URI used a default if not specified in the Request section of the capabilities
     * @param cacheSize
     *            max size of the used cache
     * @param requestTimeLimit
     *            maximum time limit (minutes) of request processing
     * @param directoryList
     *            list of directories that are scanned for data-configuration files for the WCS
     */
    public WCSDeegreeParams( OnlineResource defaultOnlineResource, int cacheSize, int requestTimeLimit,
                             String[] directoryList ) {
        super( defaultOnlineResource, cacheSize, requestTimeLimit );
        setDirectoryList( directoryList );
    }

    /**
     * @return Returns the directoryList.
     * 
     */
    public String[] getDirectoryList() {
        String[] s = new String[directoryList.size()];
        return directoryList.toArray( s );
    }

    /**
     * @param directoryList
     *            The directoryList to set.
     */
    public void setDirectoryList( String[] directoryList ) {
        this.directoryList = Arrays.asList( directoryList );
    }

    /**
     * adds a directory to the directory list
     * 
     * @param directory
     */
    public void addDirectory( String directory ) {
        directoryList.add( directory );
    }

    /**
     * removes a directory from the directory list
     * 
     * @param directory
     */
    public void removeDirectory( String directory ) {
        directoryList.remove( directory );
    }

}
