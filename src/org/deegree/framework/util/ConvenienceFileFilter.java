//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/ConvenienceFileFilter.java $
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
package org.deegree.framework.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to be used with
 * 
 * @see java.io.File for reading all file/directory names starting at a given root directory
 * 
 * @version $Revision: 14288 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 14288 $, $Date: 2008-10-13 15:01:59 +0200 (Mo, 13. Okt 2008) $
 * 
 * @since 2.0
 */
public class ConvenienceFileFilter implements FilenameFilter {

    private List<String> extensions = null;

    private boolean returnDirsAsTrue = false;

    /**
     * 
     * @param extensions
     *            list of considered file extensions (e.g. tif, BMP, Gif ..) The is is not case
     *            sensitive; use '*' for returning all files
     * @param returnDirsAsTrue
     *            if set too true the method also return directory names
     * @see #accept(java.io.File, String) will return 'true' if the passed
     * @see File is a directory
     */
    public ConvenienceFileFilter( List<String> extensions, boolean returnDirsAsTrue ) {
        this.extensions = new ArrayList<String>();
        for ( int i = 0; i < extensions.size(); i++ ) {
            this.extensions.add( extensions.get( i ).toUpperCase() );
        }
        this.returnDirsAsTrue = returnDirsAsTrue;
    }

    /**
     * 
     * @param returnDirsAsTrue
     *            if set too true the method also return directory names
     * @param extensions
     *            list of considered file extensions (e.g. tif, BMP, Gif ..) The is is not case
     *            sensitive; use '*' for returning all files
     */
    public ConvenienceFileFilter( boolean returnDirsAsTrue, String... extensions ) {
        this.extensions = new ArrayList<String>( extensions.length );
        for ( int i = 0; i < extensions.length; i++ ) {
            this.extensions.add( extensions[i].toUpperCase() );
        }
        this.returnDirsAsTrue = returnDirsAsTrue;
    }

    /**
     * @return *.* (all files)
     */
    public String getDescription() {
        return "*.*";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    public boolean accept( java.io.File file, String name ) {
        int pos = name.lastIndexOf( "." );
        String ext = name.substring( pos + 1 ).toUpperCase();
        String s = file.getAbsolutePath() + '/' + name;
        File tmp = new File( s );
        if ( file.isDirectory() ) {
            if ( tmp.isDirectory() && returnDirsAsTrue ) {
                return true;
            }
        }
        return ( extensions.contains( ext ) || ( extensions.contains( "*" ) ) && !tmp.isDirectory() );
    }

}