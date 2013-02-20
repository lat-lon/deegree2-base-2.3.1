//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_1_1_0/Manifest.java $
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

package org.deegree.owscommon_1_1_0;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>Manifest</code> is a bean representation of a Manifest type defined in ows 1.1.0.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10830 $, $Date: 2008-03-31 11:33:56 +0200 (Mon, 31 Mar 2008) $
 * 
 */
public class Manifest {

    private final BasicIdentification basicManifestIdentification;

    private final List<ReferenceGroup> referenceGroup;

    /**
     * @param basicManifestIdentification
     * @param referenceGroup
     */
    public Manifest( BasicIdentification basicManifestIdentification, List<ReferenceGroup> referenceGroup ) {
        this.basicManifestIdentification = basicManifestIdentification;
        if ( referenceGroup == null ) {
            referenceGroup = new ArrayList<ReferenceGroup>();
        }
        this.referenceGroup = referenceGroup;

    }

    /**
     * @return the basicManifestIdentification.
     */
    public final BasicIdentification getBasicManifestIdentification() {
        return basicManifestIdentification;
    }

    /**
     * @return the referenceGroup, which may be empty but never <code>null</code>
     */
    public final List<ReferenceGroup> getReferenceGroups() {
        return referenceGroup;
    }

}
