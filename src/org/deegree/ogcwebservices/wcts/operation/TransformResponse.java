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

package org.deegree.ogcwebservices.wcts.operation;

import org.deegree.i18n.Messages;
import org.deegree.ogcwebservices.wcts.data.TransformableData;
import org.deegree.owscommon_1_1_0.Manifest;

/**
 * <code>TransformResponse</code> wraps the requested inputdata (e.g. the metadata) and the transformed feature
 * collections.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class TransformResponse {

    private final boolean store;

    private final Manifest inputData;

    private final TransformableData<?> transformableData;

    private final int dataPresentation;

    /**
     * @param dataPresentation
     *            a flag signaling the way the data was presented to the wcts. Valid values are {@link Transform#INLINE}
     *            and {@link Transform#MULTIPART}. If another value is given, {@link Transform#MULTIPART} is assumed.
     * @param store
     *            true if the transformableData should be stored, false otherwise.
     * 
     * @param inputData
     *            may be <code>null</code>. Meaning the request did not provide any inputdata or it was a kvp
     *            request.
     * @param transformableData
     *            the transformed data.
     * @throws IllegalArgumentException
     *             if the transformableData object is <code>null</code>.
     */
    public TransformResponse( int dataPresentation, boolean store, Manifest inputData,
                              TransformableData<?> transformableData ) throws IllegalArgumentException {
        if ( dataPresentation != Transform.INLINE && dataPresentation != Transform.MULTIPART ) {
            dataPresentation = Transform.MULTIPART;
        }
        this.dataPresentation = dataPresentation;
        this.store = store;
        this.inputData = inputData;
        if ( transformableData == null ) {
            throw new IllegalArgumentException( Messages.getMessage( "WCTS_MISSING_ARGUMENT", transformableData ) );
        }
        this.transformableData = transformableData;
    }

    /**
     * @return true if the data should be stored.
     */
    public final boolean shouldStore() {
        return store;
    }

    /**
     * @return the inputData, may be <code>null</code>
     */
    public final Manifest getInputData() {
        return inputData;
    }

    /**
     * @return the transformableData, will never be <code>null</code>
     */
    public final TransformableData<?> getTransformableData() {
        return transformableData;
    }

    /**
     * @return a flag signaling the way the data was presented to the wcts. Valid values are {@link Transform#INLINE}
     *         and {@link Transform#MULTIPART}.
     */
    public final int getDataPresentation() {
        return dataPresentation;
    }

}
