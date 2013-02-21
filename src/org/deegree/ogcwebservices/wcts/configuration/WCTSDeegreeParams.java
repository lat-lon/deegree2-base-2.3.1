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

package org.deegree.ogcwebservices.wcts.configuration;

/**
 * <code>WCTSDeegreeParams</code> encapsulates all deegree specific configuration.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class WCTSDeegreeParams {

    private final String configuredCRSProvider;

    private final boolean useDeegreeTransformType;

    private final static String DEFAULT_TRANSFORM_ID = "urn:deegree:wcts:transformation:";

    private final String transformationPrefix;

    private final boolean createDefaultTransformationsDescription;

    /**
     * Instantiate the deegree params with given configured crs provider, full qualified name.
     * 
     * @param configuredCRSProvider
     * @param transformationPrefix
     * @param useDeegreeTransformType
     *            true if transform response elements should use the deegree inline/multipart elements. If false results
     *            will be put beneath the wcts:OperationResponse element.
     * @param createDefaultTransformationsDescription
     */
    public WCTSDeegreeParams( String configuredCRSProvider, String transformationPrefix,
                              boolean useDeegreeTransformType, boolean createDefaultTransformationsDescription ) {
        this.configuredCRSProvider = configuredCRSProvider;
        this.createDefaultTransformationsDescription = createDefaultTransformationsDescription;
        if ( transformationPrefix == null || "".equals( transformationPrefix ) ) {
            this.transformationPrefix = DEFAULT_TRANSFORM_ID;
        } else {
            this.transformationPrefix = transformationPrefix;
        }
        this.useDeegreeTransformType = useDeegreeTransformType;
    }

    /**
     * Instantiate the deegree params with given configured crs provider, full qualified name.
     * 
     * @param configuredCRSProvider
     * @param useDeegreeTransformType
     *            true if transform response elements should use the deegree inline/multipart elements. If false results
     *            will be put beneath the wcts:OperationResponse element.
     */
    public WCTSDeegreeParams( String configuredCRSProvider, boolean useDeegreeTransformType ) {
        this( configuredCRSProvider, DEFAULT_TRANSFORM_ID, useDeegreeTransformType, false );
    }

    /**
     * @param useDeegreeTransformType
     *            true if transform response elements should use the deegree inline/multipart elements. If false results
     *            will be put beneath the wcts:OperationResponse element.
     */
    public WCTSDeegreeParams( boolean useDeegreeTransformType ) {
        this( null, DEFAULT_TRANSFORM_ID, useDeegreeTransformType, false );
    }

    /**
     * Using standard provider, the default transformation prefix and the deegree transform response.
     */
    public WCTSDeegreeParams() {
        this( null, DEFAULT_TRANSFORM_ID, true, false );
    }

    /**
     * @return the configuredCRSProvider might be <code>null</code> if no provider was configured.
     */
    public final String getConfiguredCRSProvider() {
        return configuredCRSProvider;
    }

    /**
     * @return true if the transform response should be inside deegree:inline/deegree:multipart elements or if false
     *         they will be put directly beneath the operationresponse element.
     */
    public final boolean useDeegreeTransformType() {
        return useDeegreeTransformType;
    }

    /**
     * @return the transformationPrefix will never be <code>null</code> the default is
     *         "urn:deegree:wcts:transformation:".
     */
    public final String getTransformationPrefix() {
        return transformationPrefix;
    }

    /**
     * @return true if the descriptions for the default transformations should be created.
     */
    public final boolean createDefaultTransformationsDescription() {
        return createDefaultTransformationsDescription;
    }
}
