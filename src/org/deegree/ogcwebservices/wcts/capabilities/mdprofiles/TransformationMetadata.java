//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcts/capabilities/mdprofiles/TransformationMetadata.java $
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

package org.deegree.ogcwebservices.wcts.capabilities.mdprofiles;

import org.deegree.crs.transformations.Transformation;
import org.deegree.model.crs.CoordinateSystem;

/**
 * The <code>TransformationMetadata</code> class implements the MetadataProfile for the description of a
 * Transformation.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 15127 $, $Date: 2008-11-26 16:08:25 +0100 (Mi, 26. Nov 2008) $
 * 
 */
public class TransformationMetadata implements MetadataProfile<Transformation> {

    private final CoordinateSystem sourceCRS;

    private final CoordinateSystem targetCRS;

    private final Transformation transformation;

    private final String description;

    private final String transformID;

    /**
     * @param transformation
     *            described by this metadata
     * @param transformID
     * @param sourceCRS
     *            the source crs
     * @param targetCRS
     *            the target crs
     * @param description
     *            the metadata
     */
    public TransformationMetadata( Transformation transformation, String transformID, CoordinateSystem sourceCRS,
                                   CoordinateSystem targetCRS, String description ) {
        this.transformation = transformation;
        this.transformID = transformID;
        this.sourceCRS = sourceCRS;
        this.targetCRS = targetCRS;
        this.description = description;
    }

    /**
     * The result may be <code>null</code> in which case the 'default' transformation chain will be used.
     * 
     * @return the {@link Transformation} described by this metadata, if <code>null</code> the default transformation
     *         chain should be used.
     */
    public Transformation getParsedMetadataType() {
        return transformation;
    }

    /**
     * @return the sourceCRS
     */
    public final CoordinateSystem getSourceCRS() {
        return sourceCRS;
    }

    /**
     * @return the targetCRS
     */
    public final CoordinateSystem getTargetCRS() {
        return targetCRS;
    }

    /**
     * @return the description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @return the id of the transform as it was configured.
     */
    public final String getTransformID() {
        return transformID;
    }

}
