//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcts/operation/TransformationReference.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2007 by:
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

package org.deegree.ogcwebservices.wcts.operation;

/**
 * The <code>TransformationReference</code> class wraps the incoming uris of a TransformOperation, which requests an
 * explicit transformation.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 15127 $, $Date: 2008-11-26 16:08:25 +0100 (Mi, 26. Nov 2008) $
 * 
 */
public class TransformationReference {

    private final String transformationID;

    private final String sourceId;

    private final String targetId;

    /**
     * Constructor using a source and target crs.
     * 
     * @param sourceId
     * @param targetId
     */
    public TransformationReference( String sourceId, String targetId ) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        transformationID = null;
    }

    /**
     * Constructor referencing a configured transformation id.
     * 
     * @param transformationID
     *            to use
     * 
     */
    public TransformationReference( String transformationID ) {
        this.transformationID = transformationID;
        sourceId = null;
        targetId = null;
    }

    /**
     * @return the id of the transformation, may be <code>null</code>
     */
    public final String gettransformationId() {
        return transformationID;
    }

    /**
     * @return the id of the requested source CRS, may be <code>null</code>
     */
    public final String getSourceId() {
        return sourceId;
    }

    /**
     * @return the id of the requested target CRS, may be <code>null</code>
     */
    public final String getTargetId() {
        return targetId;
    }

}
