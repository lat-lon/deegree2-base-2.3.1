//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/ProcessBrief.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/exse/
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
 Aennchenstraße 19
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
package org.deegree.ogcwebservices.wps;

import java.util.ArrayList;
import java.util.List;

import org.deegree.datatypes.Code;
import org.deegree.ogcwebservices.MetadataType;

/**
 * ProcessBrief.java
 * 
 * Created on 09.03.2006. 14:13:37h
 * 
 * Brief description of a Process, designed for Process discovery.
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */
public class ProcessBrief extends WPSDescription {

    /**
     * Optional unordered list of additional metadata about this process. A list of optional and/or
     * required metadata elements for this process could be specified in a specific Application
     * Profile for this service.
     */
    private List<MetadataType> metadata;

    /**
     * Optional unordered list of additional metadata about this process. A list of optional and/or
     * required metadata elements for this process could be specified in a specific Application
     * Profile for this service.
     */
    private String processVersion;

    /**
     * 
     * @param identifier
     * @param title
     * @param _abstract
     * @param processVersion
     * @param metadata
     */
    public ProcessBrief( Code identifier, String title, String _abstract, String processVersion,
                         List<MetadataType> metadata ) {
        super( identifier, title, _abstract );
        this.processVersion = processVersion;
        this.metadata = metadata;
    }

    /**
     * 
     * @param identifier
     * @param title
     */
    public ProcessBrief( Code identifier, String title ) {
        super( identifier, title );
    }

    /**
     * 
     * @return metadata
     */
    public List<MetadataType> getMetadata() {
        if ( metadata == null ) {
            metadata = new ArrayList<MetadataType>();
        }
        return this.metadata;
    }

    /**
     * 
     * @param metadataType
     */
    public void setMetadata( List<MetadataType> metadataType ) {
        this.metadata = metadataType;
    }

    /**
     * 
     * @return processVersion
     */
    public String getProcessVersion() {
        return processVersion;
    }

    /**
     * 
     * @param value
     */
    public void setProcessVersion( String value ) {
        this.processVersion = value;
    }

}