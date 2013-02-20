//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/getcapabilities/ContentMetadata.java $
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
package org.deegree.ogcwebservices.wcs.getcapabilities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.deegree.ogcwebservices.wcs.CoverageOfferingBrief;

/**
 * @version $Revision: 9345 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 */

public class ContentMetadata implements Serializable {

    private String version = null;

    private String updateSequence = null;

    /**
     * 
     */
    private CoverageOfferingBrief[] coverageOfferingBrief;

    private Map<String, CoverageOfferingBrief> map = new HashMap<String, CoverageOfferingBrief>( 100 );

    /**
     * @param version
     * @param updateSequence
     * @param coverageOfferingBrief
     */
    public ContentMetadata( String version, String updateSequence, CoverageOfferingBrief[] coverageOfferingBrief ) {

        this.version = version;
        this.updateSequence = updateSequence;
        setCoverageOfferingBrief( coverageOfferingBrief );
    }

    /**
     * @return Returns the coverageOfferingBrief.
     */
    public CoverageOfferingBrief[] getCoverageOfferingBrief() {
        return coverageOfferingBrief;
    }

    /**
     * returns the <tt>CoverageOfferingBrief<tt> for the coverage matching
     * the passed name. if no coverage with this name is available <tt>null</tt>
     * will be returned.
     * 
     * @param coverageName
     * @return the <tt>CoverageOfferingBrief<tt> for the coverage matching
     * the passed name. if no coverage with this name is available <tt>null</tt>
     * will be returned.
     */
    public CoverageOfferingBrief getCoverageOfferingBrief( String coverageName ) {
        return map.get( coverageName );
    }

    /**
     * @param coverageOfferingBrief
     *            The coverageOfferingBrief to set.
     */
    public void setCoverageOfferingBrief( CoverageOfferingBrief[] coverageOfferingBrief ) {
        map.clear();
        this.coverageOfferingBrief = new CoverageOfferingBrief[coverageOfferingBrief.length];

        for ( int i = 0; i < coverageOfferingBrief.length; i++ ) {
            this.coverageOfferingBrief[i] = coverageOfferingBrief[i];
            map.put( coverageOfferingBrief[i].getName(), coverageOfferingBrief[i] );
        }
    }

    /**
     * @return Returns the updateSequence.
     */
    public String getUpdateSequence() {
        return updateSequence;
    }

    /**
     * @param updateSequence
     *            The updateSequence to set.
     */
    public void setUpdateSequence( String updateSequence ) {
        this.updateSequence = updateSequence;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            The version to set.
     */
    public void setVersion( String version ) {
        this.version = version;
    }

}
