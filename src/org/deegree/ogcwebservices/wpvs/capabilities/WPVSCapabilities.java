//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/capabilities/WPVSCapabilities.java $
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
package org.deegree.ogcwebservices.wpvs.capabilities;

import org.deegree.ogcwebservices.getcapabilities.Contents;
import org.deegree.ogcwebservices.getcapabilities.OperationsMetadata;
import org.deegree.ogcwebservices.getcapabilities.ServiceIdentification;
import org.deegree.ogcwebservices.getcapabilities.ServiceProvider;
import org.deegree.owscommon.OWSCommonCapabilities;

/**
 * This class represents a <code>WPVSCapabilities</code> object.
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */
public class WPVSCapabilities extends OWSCommonCapabilities {

    /**
     * 
     */
    private static final long serialVersionUID = 957878718619030101L;
    private Dataset dataset;

    /**
     * Creates a new wpvsCapabilities object from the given parameters.
     * 
     * @param version
     * @param updateSequence
     * @param serviceIdentification
     * @param serviceProvider
     * @param operationsMetadata
     * @param contents
     *            TODO field not verified! Check spec.
     * @param dataset
     */
    public WPVSCapabilities( String version, String updateSequence,
                            ServiceIdentification serviceIdentification,
                            ServiceProvider serviceProvider, OperationsMetadata operationsMetadata,
                            Contents contents, Dataset dataset ) {

        super( version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata,
               contents );
        this.dataset = dataset;

    }

    /**
     * @return the root dataset of this wpvs
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * recursion over all layers to find the layer that matches the submitted name. If no layer can
     * be found that fullfills the condition <code>null</code> will be returned.
     * 
     * @param name
     *            name of the layer to be found
     * @param datasets
     *            list of searchable layers
     * 
     * @return a layer object or <code>null</code>
     */
    private Dataset findDataset( String name, Dataset[] datasets ) {
        Dataset dset = null;

        if ( datasets != null ) {
            for ( Dataset set : datasets ) {
                dset = findDataset( name, set.getDatasets() );
                if ( dset != null )
                    return dset;
                if ( name.equals( set.getName() ) ) {
                    return set;
                }
            }
        }
        return dset;
    }

    /**
     * returns a DataSet provided by a WPVS with the given name 
     * @param name the name of the DataSet
     * 
     * @return a DataSet with a given Name or <code>null</code> if no such DataSet exists.
     */
    public Dataset findDataset( String name ) {
        if ( name == null )
            return null;
        if ( dataset.getName() != null && name.equals( dataset.getName() ) ) {
            return dataset;
        }
        return findDataset( name, dataset.getDatasets() );
    }

    /**
     * Finds an <code>ElevationModel</code> in this dataset or in its children. This method return
     * the first dataset it finds or null, otherwise.
     * 
     * @param elevationModelName
     *            the name identifying the <code>ElevationModel</code>
     * @return the <code>ElevationModel</code> with the given name or null
     */
    public ElevationModel findElevationModel( String elevationModelName ) {

        ElevationModel elevModel = null;
        Dataset ds = dataset;

        if ( ds != null ) {

            ElevationModel em = ds.getElevationModel();
            if ( elevationModelName == null )
                return null;
            if ( em != null && elevationModelName.equals( em.getName() ) ) {
                return em;
            }
            Dataset[] datasets = ds.getDatasets();
            elevModel = findElevationModel( elevationModelName, datasets );
        }
        return elevModel;
    }

    /**
     * @param elevationModelName
     * @param datasets
     */
    private ElevationModel findElevationModel( String elevationModelName, Dataset[] datasets ) {

        if ( elevationModelName == null || datasets == null )
            return null;
        for ( Dataset dset : datasets ) {
            ElevationModel elevModel = findElevationModel( elevationModelName, dset.getDatasets() );
            if ( elevModel != null )
                return elevModel;

            elevModel = dset.getElevationModel();
            if ( elevModel != null && elevationModelName.equals( elevModel.getName() ) ) {
                return elevModel;
            }
        }
        return null;
    }

}