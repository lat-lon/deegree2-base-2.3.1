//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/CoverageOfferingBrief.java $
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
package org.deegree.ogcwebservices.wcs;

import java.net.URL;

import org.deegree.model.metadata.iso19115.Keywords;
import org.deegree.ogcbase.Description;
import org.deegree.ogcbase.OGCException;
import org.deegree.ogcwebservices.LonLatEnvelope;
import org.deegree.ogcwebservices.MetadataLink;

/**
 * @version $Revision: 9345 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */

public class CoverageOfferingBrief extends Description {

    private static final long serialVersionUID = 7109863070752388720L;

    private LonLatEnvelope lonLatEnvelope = null;

    private Keywords[] keywords = null;

    private URL configuration = null;

    /**
     * @param name
     * @param label
     * @param description
     * @param metadataLink
     * @param lonLatEnvelope
     * @param keywords
     */
    public CoverageOfferingBrief( String name, String label, String description, MetadataLink metadataLink,
                                  LonLatEnvelope lonLatEnvelope, Keywords[] keywords ) throws OGCException,
                            WCSException {
        super( name, label, description, metadataLink );
        setLonLatEnvelope( lonLatEnvelope );
        this.keywords = keywords;
    }

    /**
     * @param name
     * @param label
     * @param description
     * @param metadataLink
     * @param lonLatEnvelope
     * @param keywords
     */
    public CoverageOfferingBrief( String name, String label, String description, MetadataLink metadataLink,
                                  LonLatEnvelope lonLatEnvelope, Keywords[] keywords, URL configuration )
                            throws OGCException, WCSException {
        super( name, label, description, metadataLink );
        setLonLatEnvelope( lonLatEnvelope );
        this.keywords = keywords;
        this.configuration = configuration;
    }

    /**
     * @return Returns the keywords.
     * 
     */
    public Keywords[] getKeywords() {
        return keywords;
    }

    /**
     * @param keywords
     *            The keywords to set.
     * 
     */
    public void setKeywords( Keywords[] keywords ) {
        this.keywords = keywords;
    }

    /**
     * @return Returns the lonLatEnvelope.
     * 
     */
    public LonLatEnvelope getLonLatEnvelope() {
        return lonLatEnvelope;
    }

    /**
     * @param lonLatEnvelope
     *            The lonLatEnvelope to set.
     * 
     */
    public void setLonLatEnvelope( LonLatEnvelope lonLatEnvelope ) {
        this.lonLatEnvelope = lonLatEnvelope;
    }

    /**
     * @return Returns the configuration.
     */
    public URL getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration
     *            The configuration to set.
     * 
     */
    public void setConfiguration( URL configuration ) {
        this.configuration = configuration;
    }

}
