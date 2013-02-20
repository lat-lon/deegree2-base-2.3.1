//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/context/LayerExtension.java $
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
package org.deegree.portal.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * provides additional information about a layer described in a web map context document. Additional description is not
 * requiered so an instance of <tt>org.deegree_impl.clients.context.Layer</tt> may doesn't provide an instance of this
 * class.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10906 $, $Date: 2008-04-03 09:50:02 +0200 (Thu, 03 Apr 2008) $
 */
public class LayerExtension {

    /**
     * No authentication type
     */
    public static final int NONE = -1;

    /**
     * The session id authentication type
     */
    public static final int SESSIONID = 0;

    /**
     * The user password authentication type
     */
    public static final int USERPASSWORD = 1;

    private DataService dataService = null;

    private boolean masterLayer = false;

    private double minScaleHint = 0;

    private double maxScaleHint = 9E99;

    private boolean selectedForQuery = false;

    private int authentication = NONE;

    private int parentNodeId = NONE;

    private boolean showLegendGraphic = false;

    private Map<String, String> vendorspecificParams = new HashMap<String, String>();

    /**
     * default constructor
     * 
     */
    public LayerExtension() {
        // default constructor.
    }

    /**
     * Creates a new LayerExtension object.
     * 
     * @param dataService
     *            description of the service/server behind a WMS layer
     * @param masterLayer
     *            true if a layer is one of the main layers of an application; false if it just provides background or
     *            additional informations.
     * @param minScaleHint
     * @param maxScaleHint
     * @param selectedForQuery
     * @param authentication
     * @param parentNodeId
     * @param showLegendGraphic
     */
    public LayerExtension( DataService dataService, boolean masterLayer, double minScaleHint, double maxScaleHint,
                           boolean selectedForQuery, int authentication, int parentNodeId, boolean showLegendGraphic ) {
        setDataService( dataService );
        setMasterLayer( masterLayer );
        setMinScaleHint( minScaleHint );
        setMaxScaleHint( maxScaleHint );
        setSelectedForQuery( selectedForQuery );
        setAuthentication( authentication );
        setParentNodeId( parentNodeId );
        setShowLegendGraphic( showLegendGraphic );

    }

    /**
     * returns a description of the service/server behind a WMS layer. The returned value will be <tt>null</tt> if the
     * WMS uses an internal mechanism to access a layers data.
     * 
     * @return instance of <tt>DataService</tt>
     */
    public DataService getDataService() {
        return this.dataService;
        // return null;
    }

    /**
     * sets a description of the service/server behind a WMS layer. The returned value will be <tt>null</tt> if the
     * WMS uses an internal mechanism to access a layers data.
     * 
     * @param dataService
     */
    public void setDataService( DataService dataService ) {
        this.dataService = dataService;
    }

    /**
     * @return true if a layer is one of the main layers of an application; returns false if it just provides background
     *         or additional informations.
     * 
     */
    public boolean isMasterLayer() {
        return masterLayer;
    }

    /**
     * set to true if a layer is one of the main layers of an application; set to false if it just provides background
     * or additional informations.
     * 
     * @param masterLayer
     */
    public void setMasterLayer( boolean masterLayer ) {
        this.masterLayer = masterLayer;
    }

    /**
     * returns the maximum sclae the layer is valid
     * 
     * @return maximum scale hint
     */
    public double getMaxScaleHint() {
        return maxScaleHint;
    }

    /**
     * sets the maximum scale the layer is valid for
     * 
     * @param maxScaleHint
     */
    public void setMaxScaleHint( double maxScaleHint ) {
        this.maxScaleHint = maxScaleHint;
    }

    /**
     * returns the minimum sclae the layer is valid
     * 
     * @return minimum scale hint
     */
    public double getMinScaleHint() {
        return minScaleHint;
    }

    /**
     * sets the minimum scale the layer is valid for
     * 
     * @param minScaleHint
     */
    public void setMinScaleHint( double minScaleHint ) {
        this.minScaleHint = minScaleHint;
    }

    /**
     * returns true if a layer is currently selected for being active for feature info requests
     * 
     * @return <code>true</code> if a layer is currently selected for being active for feature info requests
     */
    public boolean isSelectedForQuery() {
        return selectedForQuery;
    }

    /**
     * sets a layer to active for feature info requests
     * 
     * @param selectedForFI
     */
    public void setSelectedForQuery( boolean selectedForFI ) {
        this.selectedForQuery = selectedForFI;
    }

    /**
     * returns a code for authentication to be used for service requests
     * 
     * @return a code for authentication to be used for service requests
     */
    public int getAuthentication() {
        return authentication;
    }

    /**
     * @see #getAuthentication()
     * @param authentication
     */
    public void setAuthentication( int authentication ) {
        this.authentication = authentication;
    }

    /**
     * returns true if the legendGraphic of the layer should be drawn in the layerlistview
     * 
     * @return <code>true</code> if the legendGraphic of the layer should be drawn in the layerlistview
     */
    public boolean getShowLegendGraphic() {
        return showLegendGraphic;
    }

    /**
     * returns true the id of the node to which the layer belongs in the layertree
     * 
     * @return <code>true</code> the id of the node to which the layer belongs in the layertree
     */
    public int getParentNodeId() {
        return parentNodeId;
    }

    /**
     * 
     * @param showLegendGraphic
     */
    public void setShowLegendGraphic( boolean showLegendGraphic ) {
        this.showLegendGraphic = showLegendGraphic;
    }

    /**
     * 
     * @param parentNodeId
     */
    public void setParentNodeId( int parentNodeId ) {
        this.parentNodeId = parentNodeId;
    }

    /**
     * 
     * @param name
     * @param value
     */
    public void addVendorspecificParameter( String name, String value ) {
        vendorspecificParams.put( name, value );
    }

    /**
     * 
     * @param name
     * @return the vendorspecific parameter by given name or <code>null</code> if no such parameter exists.
     */
    public String getVendorspecificParameter( String name ) {
        return vendorspecificParams.get( name );
    }

    /**
     * 
     * @return an iterator over all vendor specific keys.
     */
    public Iterator<String> getVendorspecificParameterNames() {
        return vendorspecificParams.keySet().iterator();
    }

}
