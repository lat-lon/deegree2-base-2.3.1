//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/wfs/configuration/DigitizerClientConfiguration.java $
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

package org.deegree.portal.standard.wfs.configuration;

import java.util.HashMap;
import java.util.Map;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;

/**
 * TODO describe function and usage of the class here.
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public class DigitizerClientConfiguration {

    private static final ILogger LOG = LoggerFactory.getLogger( DigitizerClientConfiguration.class );

    protected Map<QualifiedName, String> featureTypeToAddressMap;

    protected Map<QualifiedName, String> featureTypeToFormTemplateMap;

    protected Map<QualifiedName, String> featureTypeToInsertTemplateMap;

    protected Map<QualifiedName, String> featureTypeToUpdateTemplateMap;

    protected Map<QualifiedName, String> featureTypeToDeleteTemplateMap;

    /**
     * create a new DigitizeClientConfiguration object
     */
    public DigitizerClientConfiguration() {
        featureTypeToAddressMap = new HashMap<QualifiedName, String>( 5 );
        featureTypeToFormTemplateMap = new HashMap<QualifiedName, String>( 5 );
        featureTypeToInsertTemplateMap = new HashMap<QualifiedName, String>( 5 );
        featureTypeToUpdateTemplateMap = new HashMap<QualifiedName, String>( 5 );
        featureTypeToDeleteTemplateMap = new HashMap<QualifiedName, String>( 5 );
        LOG.logDebug( "initializing digitizer client configuration" );
    }

    /**
     * @param featureType
     *            as qualified name
     * @param wfsAddress
     */
    public void addFeatureTypeAddress( QualifiedName featureType, String wfsAddress ) {
        LOG.logDebug( "add wfsAddress: " + wfsAddress );
        this.featureTypeToAddressMap.put( featureType, wfsAddress );
    }

    /**
     * @param featureType
     *            as qualified name
     * @param formTemplate
     */
    public void addFeatureTypeFormTemplate( QualifiedName featureType, String formTemplate ) {
        LOG.logDebug( "add formTemplate: " + formTemplate );
        this.featureTypeToFormTemplateMap.put( featureType, formTemplate );
    }

    /**
     * @param featureType
     *            as qualified name
     * @param insertTemplate
     */
    public void addFeatureTypeInsertTemplate( QualifiedName featureType, String insertTemplate ) {
        LOG.logDebug( "add insertTemplate: " + insertTemplate );
        this.featureTypeToInsertTemplateMap.put( featureType, insertTemplate );
    }

    /**
     * @param featureType
     *            as qualified name
     * @param updateTemplate
     */
    public void addFeatureTypeUpdateTemplate( QualifiedName featureType, String updateTemplate ) {
        LOG.logDebug( "add updateTemplate: " + updateTemplate );
        this.featureTypeToUpdateTemplateMap.put( featureType, updateTemplate );
    }

    /**
     * @param featureType
     *            as qualified name
     * @param deleteTemplate
     */
    public void addFeatureTypeDeleteTemplate( QualifiedName featureType, String deleteTemplate ) {
        LOG.logDebug( "add deleteTemplate: " + deleteTemplate );
        this.featureTypeToDeleteTemplateMap.put( featureType, deleteTemplate );
    }

    /**
     * @param featureType
     *            the name of the featureType
     * @return Returns the wfs address for a given featureType. May be null, if the passed
     *         featureType is unknown.
     */
    public String getFeatureTypeAddress( QualifiedName featureType ) {
        return featureTypeToAddressMap.get( featureType );
    }

    /**
     * @param featureType
     *            the name of the featureType
     * @return Returns the form template for a given featureType. May be null, if the passed
     *         featureType is unknown.
     */
    public String getFeatureTypeFormTemplate( QualifiedName featureType ) {
        return featureTypeToFormTemplateMap.get( featureType );
    }

    /**
     * @param featureType
     *            the name of the featureType
     * @return Returns the insert template for a given featureType. May be null, if the passed
     *         featureType is unknown.
     */
    public String getFeatureTypeInsertTemplate( QualifiedName featureType ) {
        return featureTypeToInsertTemplateMap.get( featureType );
    }

    /**
     * @param featureType
     *            the name of the featureType
     * @return Returns the update template for a given featureType. May be null, if the passed
     *         featureType is unknown.
     */
    public String getFeatureTypeUpdateTemplate( QualifiedName featureType ) {
        return featureTypeToUpdateTemplateMap.get( featureType );
    }

    /**
     * @param featureType
     *            the name of the featureType
     * @return Returns the delete template for a given featureType. May be null, if the passed
     *         featureType is unknown.
     */
    public String getFeatureTypeDeleteTemplate( QualifiedName featureType ) {
        return featureTypeToDeleteTemplateMap.get( featureType );
    }

    /**
     * @return Returns the featureTypeToAddressMap.
     */
    public Map getFeatureTypeToAddressMap() {
        return featureTypeToAddressMap;
    }

    /**
     * @return Returns the featureTypeToFormTemplateMap.
     */
    public Map getFeatureTypeToFormTemplateMap() {
        return featureTypeToFormTemplateMap;
    }

    /**
     * @return Returns the featureTypeToInsertTemplateMap.
     */
    public Map getFeatureTypeToInsertTemplateMap() {
        return featureTypeToInsertTemplateMap;
    }

    /**
     * @return Returns the featureTypeToInsertTemplateMap.
     */
    public Map getFeatureTypeToUpdateTemplateMap() {
        return featureTypeToUpdateTemplateMap;
    }

}
