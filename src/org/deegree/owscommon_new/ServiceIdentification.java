//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_new/ServiceIdentification.java $
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
package org.deegree.owscommon_new;

import java.util.Date;
import java.util.List;

import org.deegree.datatypes.Code;
import org.deegree.model.metadata.iso19115.Constraints;
import org.deegree.model.metadata.iso19115.Keywords;

/**
 * <code>ServiceIdentification</code> stores the contents of a ServiceIdentification element
 * according to the OWS common specification version 1.0.0.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */

public class ServiceIdentification {

    private Code serviceType = null;

    private List<String> serviceTypeVersions = null;

    private String title = null;

    private List<String> alternativeTitles = null;

    private Date date = null;

    private String identifier = null;

    private String abstractString = null;

    private List<Keywords> keywords = null;

    private List<Constraints> accessConstraints = null;

    /**
     * Standard constructor that initializes all encapsulated data.
     * 
     * @param serviceType
     * @param serviceTypeVersions
     * @param title
     * @param alternativeTitles
     * @param date
     * @param identifier
     * @param abstractString
     * @param keywords
     * @param accessConstraints
     * 
     */
    public ServiceIdentification( Code serviceType, List<String> serviceTypeVersions, String title,
                                  List<String> alternativeTitles, Date date, String identifier, String abstractString,
                                  List<Keywords> keywords, List<Constraints> accessConstraints ) {
        this.serviceType = serviceType;
        this.serviceTypeVersions = serviceTypeVersions;
        this.title = title;
        this.alternativeTitles = alternativeTitles;
        this.date = date;
        this.identifier = identifier;
        this.abstractString = abstractString;
        this.keywords = keywords;
        this.accessConstraints = accessConstraints;
    }

    /**
     * @return Returns the abstractString.
     */
    public String getAbstractString() {
        return abstractString;
    }

    /**
     * @return Returns the accessConstraints.
     */
    public List<Constraints> getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * @return Returns the alternativeTitles.
     */
    public List<String> getAlternativeTitles() {
        return alternativeTitles;
    }

    /**
     * @return Returns the date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return Returns the identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return Returns the keywords.
     */
    public List<Keywords> getKeywords() {
        return keywords;
    }

    /**
     * @return Returns the serviceType.
     */
    public Code getServiceType() {
        return serviceType;
    }

    /**
     * @return Returns the serviceTypeVersions.
     */
    public List<String> getServiceTypeVersions() {
        return serviceTypeVersions;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

}
