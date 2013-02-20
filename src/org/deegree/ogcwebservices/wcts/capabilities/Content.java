//$HeadURL: $
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

package org.deegree.ogcwebservices.wcts.capabilities;

import java.util.List;

import org.deegree.framework.util.Pair;
import org.deegree.model.crs.CoordinateSystem;

/**
 * <code>Content</code> encapsulates the Content element of the WCTS_0.4.0 Capabilities document.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class Content {

    private final List<String> transformations;

    private final List<String> methods;

    private final List<CoordinateSystem> sourceCRSs;

    private final List<CoordinateSystem> targetCRSs;

    private final CoverageAbilities coverageAbilities;

    private final FeatureAbilities featureAbilities;

    private final List<Pair<String, String>> metadata;

    private final boolean userDefinedCRS;

    /**
     * @param transformations
     *            Unordered list of zero or more identifiers of well-known coordinate operations which the server can
     *            perform.
     * @param methods
     *            Unordered list of zero or more identifiers of well-known operation methods which the server can apply
     *            in user-defined coordinate Transformations and Conversions.
     * @param sourceCRSs
     *            Unordered list of one or more identifiers of well-known CRSs in which the server can accept sourceCRS
     *            values.
     * @param targetCRSs
     *            Unordered list of one or more identifiers of well-known CRSs in which the server can accept targetCRS
     *            values.
     * @param coverageAbilities
     *            Specifies coverage transformation abilities of WCTS server.
     * @param featureAbilities
     *            Specifies feature transformation abilities of WCTS server.
     * @param metadata
     *            Optional unordered list of additional metadata about the data served by this WCTS implementation. For
     *            example, this metadata could include more detailed definitions of the Methods, Transformations, and
     *            CRSs known to this server, perhaps in the form of a gml:Dictionary of such information.
     * @param userDefinedCRS
     *            Specifies if this server supports user-defined Coordinate Reference Systems (CRSs).
     */
    public Content( List<String> transformations, List<String> methods, List<CoordinateSystem> sourceCRSs,
                    List<CoordinateSystem> targetCRSs, CoverageAbilities coverageAbilities, FeatureAbilities featureAbilities,
                    List<Pair<String, String>> metadata, boolean userDefinedCRS ) {
        this.transformations = transformations;
        this.methods = methods;
        this.sourceCRSs = sourceCRSs;
        this.targetCRSs = targetCRSs;
        this.coverageAbilities = coverageAbilities;
        this.featureAbilities = featureAbilities;
        this.metadata = metadata;
        this.userDefinedCRS = userDefinedCRS;
    }

    /**
     * @return the transformations.
     */
    public final List<String> getTransformations() {
        return transformations;
    }

    /**
     * @return the methods.
     */
    public final List<String> getMethods() {
        return methods;
    }

    /**
     * @return the sourceCRSs.
     */
    public final List<CoordinateSystem> getSourceCRSs() {
        return sourceCRSs;
    }

    /**
     * @return the targetCRSs.
     */
    public final List<CoordinateSystem> getTargetCRSs() {
        return targetCRSs;
    }

    /**
     * @return the coverageAbilities.
     */
    public final CoverageAbilities getCoverageAbilities() {
        return coverageAbilities;
    }

    /**
     * @return the featureAbilities.
     */
    public final FeatureAbilities getFeatureAbilities() {
        return featureAbilities;
    }

    /**
     * @return the metadatas, a list of &lt;xlink:href, about&gt; pairs.
     */
    public final List<Pair<String, String>> getMetadata() {
        return metadata;
    }

    /**
     * @return the userDefinedCRS.
     */
    public final boolean supportsUserDefinedCRS() {
        return userDefinedCRS;
    }

}
