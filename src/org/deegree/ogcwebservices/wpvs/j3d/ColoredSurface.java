//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/j3d/ColoredSurface.java $
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
package org.deegree.ogcwebservices.wpvs.j3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

import org.deegree.model.spatialschema.Geometry;
import org.deegree.ogcwebservices.wpvs.configuration.RenderingConfiguration;

/**
 * 
 * 
 * 
 * @version $Revision: 12271 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 12271 $, $Date: 2008-06-10 15:00:47 +0200 (Di, 10. Jun 2008) $
 * 
 * @since 2.0
 */
public class ColoredSurface extends DefaultSurface {

    private Appearance defaultAppearance;

    /**
     * 
     * @param objectID
     *            an Id for this Surface, for example a db primary key
     * @param parentId
     *            an Id for the parent of this Surface, for example if this is a wall the parent is the building.
     * @param geometry
     * @param red
     * @param green
     * @param blue
     * @param transparency
     */
    public ColoredSurface( String objectID, String parentId, Geometry geometry, float red, float green, float blue,
                           float transparency ) {
        super( objectID, parentId, geometry );
        Material material = createMaterial( red, green, blue );
        setAppearance( createAppearance( material, transparency ) );
    }

    /**
     * 
     * @param objectID
     *            an Id for this Surface, for example a db primary key
     * @param parentId
     *            an Id for the parent of this Surface, for example if this is a wall the parent is the building.
     * @param surface
     * @param material
     * @param transparency
     */
    public ColoredSurface( String objectID, String parentId, Geometry surface, Material material, float transparency ) {
        super( objectID, parentId, surface );
        setAppearance( createAppearance( material, transparency ) );
    }

    /**
     * 
     * @param objectID
     *            an Id for this Surface, for example a db primary key
     * @param parentId
     *            an Id for the parent of this Surface, for example if this is a wall the parent is the building.
     * @param surface
     * @param app
     */
    public ColoredSurface( String objectID, String parentId, Geometry surface, Appearance app ) {
        super( objectID, parentId, surface );
        setAppearance( app );
    }

    @Override
    public Appearance getAppearance() {
        return defaultAppearance;
    }

    @Override
    public void setAppearance( Appearance appearance ) {
        this.defaultAppearance = appearance;
        super.setAppearance( appearance );
    }

    /**
     * create a simple colored Material
     * 
     * @param red
     * @param green
     * @param blue
     * @return a new Material with specular and ambient and diffuse color.
     */
    private Material createMaterial( float red, float green, float blue ) {
        Color3f color = new Color3f( red, green, blue );
        Material targetMaterial = new Material();

        targetMaterial.setAmbientColor( color );
        targetMaterial.setDiffuseColor( color );
        targetMaterial.setSpecularColor( color );
        targetMaterial.setShininess( 75.0f );
        targetMaterial.setLightingEnable( true );
        targetMaterial.setCapability( Material.ALLOW_COMPONENT_WRITE );
        return targetMaterial;
    }

    /**
     * create Appearance from a material and a opacity value
     * 
     * @param material
     * @param transparency
     * @return a default appearance created with the material properties
     */
    private Appearance createAppearance( Material material, float transparency ) {
        // create the appearance and it's material properties
        Appearance appearance = new Appearance();
        appearance.setMaterial( material );
        RenderingConfiguration rc = RenderingConfiguration.getInstance();
        // the coloring attributes
        appearance.setColoringAttributes( rc.getColoringAttributes() );

        // the polygon attributes
        appearance.setPolygonAttributes( rc.getSurfacePolygonAttributes() );

        RenderingAttributes ra = new RenderingAttributes();
        ra.setDepthBufferEnable( true );
        appearance.setRenderingAttributes( ra );

        if ( transparency != 0f ) {
            TransparencyAttributes transpAtt = new TransparencyAttributes( TransparencyAttributes.BLENDED, transparency );
            appearance.setTransparencyAttributes( transpAtt );
        }
        return appearance;
    }

}