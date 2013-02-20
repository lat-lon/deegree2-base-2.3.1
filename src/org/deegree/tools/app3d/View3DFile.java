//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/base/trunk/resources/eclipse/svn_classfile_header_template.xml $
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

package org.deegree.tools.app3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfigTemplate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.Light;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.PointArray;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.xml.transform.TransformerException;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XSLTDocument;
import org.deegree.io.dbaseapi.DBaseException;
import org.deegree.io.shpapi.shape_new.ShapeFile;
import org.deegree.io.shpapi.shape_new.ShapeFileReader;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.GMLFeatureCollectionDocument;
import org.deegree.model.spatialschema.Curve;
import org.deegree.model.spatialschema.CurveSegment;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.model.spatialschema.GeometryException;
import org.deegree.model.spatialschema.MultiSurface;
import org.deegree.model.spatialschema.Point;
import org.deegree.model.spatialschema.Position;
import org.deegree.model.spatialschema.Ring;
import org.deegree.model.spatialschema.Surface;
import org.deegree.ogcbase.CommonNamespaces;
import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * The <code>View3DFile</code> class can display shape and gml/citygml file in 3D.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */

public class View3DFile extends JFrame implements ActionListener, KeyListener {
    /**
     * 
     */
    private static final long serialVersionUID = 7698388852544865855L;

    private static ILogger LOG = LoggerFactory.getLogger( View3DFile.class );

    private SimpleUniverse simpleUniverse;

    // private JCanvas3D canvas;
    // private TrackBall trackBall;

    private Canvas3D canvas;

    private MouseRotate trackBall;

    private BranchGroup scene;

    private TransformGroup rotationGroup;

    private Light firstLight, secondLight, thirdLight;

    private Point3d centroid;

    private JFileChooser fileChooser;

    private Preferences prefs;

    private final static String PREF_KEY = "lastlocation";

    private final static String WIN_TITLE = "Deegree 3D Object viewer: ";

    private Background backGround;

    /**
     * Creates a frame with the menus and the canvas3d set and tries to load the file from given
     * location.
     * 
     * @param fileName
     *            to be loaded.
     */
    public View3DFile( String fileName ) {
        this( false );
        readFile( fileName );
    }

    /**
     * Creates a new frame with the menus and the canvas3d set.
     * 
     * @param testSphere
     *            true if a sphere should be displayed.
     */
    public View3DFile( boolean testSphere ) {
        super( WIN_TITLE );

        setupGUI();

        setupFileChooser();

        setupJava3D( testSphere );

        pack();
    }

    /**
     * GUI stuff
     */
    private void setupGUI() {
        // add listener for closing the frame/application
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent evt ) {
                System.exit( 0 );
            }
        } );
        setLayout( new BorderLayout() );
        setMinimumSize( new Dimension( 400, 400 ) );
        setPreferredSize( new Dimension( 400, 400 ) );
        setVisible( true );
        // Adding the button panel
        JPanel buttonPanel = new JPanel( new FlowLayout() );
        createButtons( buttonPanel );
        getContentPane().add( buttonPanel, BorderLayout.SOUTH );
        addKeyListener( this );

    }

    private void setupFileChooser() {
        // Setting up the fileChooser.
        prefs = Preferences.userNodeForPackage( View3DFile.class );
        String lastLoc = prefs.get( PREF_KEY, System.getProperty( "user.home" ) );
        File lastFile = new File( lastLoc );
        if ( !lastFile.exists() ) {
            lastFile = new File( System.getProperty( "user.home" ) );
        }
        fileChooser = new JFileChooser( lastFile );

        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add( "gml" );
        extensions.add( "xml" );
        CustomFileFilter fileFilter = new CustomFileFilter( extensions, "(*.gml, *.xml) GML or CityGML-Files" );
        fileChooser.setFileFilter( fileFilter );

        extensions.clear();
        extensions.add( "shp" );
        fileFilter = new CustomFileFilter( extensions, "(*.shp) Esri ShapeFiles" );
        fileChooser.setFileFilter( fileFilter );

        extensions.clear();
        extensions.add( "vrml" );
        extensions.add( "wrl" );
        fileFilter = new CustomFileFilter( extensions, "(*.vrml, *.wrl) VRML97 - Virtual Reality Modelling Language" );
        fileChooser.setFileFilter( fileFilter );

        // The *.* filter is off.
        fileChooser.setAcceptAllFileFilterUsed( false );

        fileChooser.setMultiSelectionEnabled( false );
    }

    private void setupJava3D( boolean testSphere ) {
        // setting up Java3D
        GraphicsConfigTemplate3D configTemplate = new GraphicsConfigTemplate3D();
        configTemplate.setSceneAntialiasing( GraphicsConfigTemplate.PREFERRED );
        configTemplate.setDoubleBuffer( GraphicsConfigTemplate.REQUIRED );
        canvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
        simpleUniverse = new SimpleUniverse( canvas );
        if ( canvas != null ) {
            getContentPane().add( canvas, BorderLayout.CENTER );
        }

        View view = simpleUniverse.getViewer().getView();

        // view parameters
        view.setBackClipDistance( 10000 );
        view.setFrontClipDistance( 0.1 );
        view.setWindowEyepointPolicy( View.RELATIVE_TO_FIELD_OF_VIEW );
        view.setSceneAntialiasingEnable( true );

        centroid = new Point3d();
        scene = new BranchGroup();
        firstLight = createDirectionalLight( new Vector3f( 0, 0, 1 ) );
        secondLight = createDirectionalLight( new Vector3f( 0, -1, -1 ) );
        thirdLight = createDirectionalLight( new Vector3f( -1, 0, 0 ) );

        scene.addChild( firstLight );
        scene.addChild( secondLight );
        scene.addChild( thirdLight );

        // is handled by the mouse rotater, all objects will be added to it.
        rotationGroup = new TransformGroup();
        rotationGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
        rotationGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );
        rotationGroup.setCapability( BranchGroup.ALLOW_DETACH );
        rotationGroup.setCapability( Group.ALLOW_CHILDREN_EXTEND );
        rotationGroup.setCapability( Group.ALLOW_CHILDREN_READ );
        rotationGroup.setCapability( Group.ALLOW_CHILDREN_WRITE );
        scene.addChild( rotationGroup );

        backGround = new Background( new Color3f( Color.LIGHT_GRAY ) );
        backGround.setCapability( Background.ALLOW_BOUNDS_WRITE );
        backGround.setCapability( Background.ALLOW_BOUNDS_READ );
        backGround.setCapability( Background.ALLOW_APPLICATION_BOUNDS_READ );
        backGround.setCapability( Background.ALLOW_APPLICATION_BOUNDS_WRITE );

        scene.addChild( backGround );

        trackBall = new MouseRotate();
        trackBall.setTransformGroup( rotationGroup );
        trackBall.setSchedulingBounds( new BoundingSphere() );
        scene.addChild( trackBall );

        simpleUniverse.addBranchGraph( scene );

        // adding the key listeners
        canvas.addKeyListener( this );

        if ( testSphere ) {
            BranchGroup sphere = createJ3DSphere( new Point3d( 0, 0, 0 ) );
            addBranchGroupToScene( sphere );
        }
    }

    /**
     * Create a directional light, with color.WHITE.
     */
    private DirectionalLight createDirectionalLight( Vector3f lightDir ) {
        // create the color for the light
        Color3f color = new Color3f( Color.WHITE );
        // create the directional light with the color and direction
        DirectionalLight light = new DirectionalLight( color, lightDir );
        light.setCapability( Light.ALLOW_INFLUENCING_BOUNDS_READ );
        light.setCapability( Light.ALLOW_INFLUENCING_BOUNDS_WRITE );
        return light;
    }

    private void addBranchGroupToScene( BranchGroup b ) {
        LOG.logInfo( "Setting the branchgroup to : " + b.getName() );
        rotationGroup.removeAllChildren();
        // translationGroup.removeAllChildren();
        // System.out.println( b.getBounds() );
        Bounds bounds = b.getBounds();
        if ( bounds != null ) {

            LOG.logDebug( "Old centroid: " + centroid );
            BoundingSphere bs = new BoundingSphere( bounds );
            bs.getCenter( centroid );
            LOG.logDebug( "New centroid: " + centroid );

            double radius = bs.getRadius();

            View view = simpleUniverse.getViewer().getView();
            // view parameters
            view.setBackClipDistance( radius * 10 );
            // the near clippingplane is one hundereth of the far.
            view.setFrontClipDistance( ( radius * 4 ) * 0.001 );

            TransformGroup viewToWorld = simpleUniverse.getViewingPlatform().getViewPlatformTransform();
            Transform3D trans = new Transform3D();
            trans.lookAt( new Point3d( centroid.x, centroid.y, centroid.z - ( radius * 2 ) ), centroid,
                          new Vector3d( 0, 1, 0 ) );
            LOG.logDebug( "Trans Matrix after lookat:\n" + trans );
            viewToWorld.setTransform( trans );

            LOG.logDebug( "Center: " + centroid );
            LOG.logDebug( "radius: " + bs.getRadius() );

            firstLight.setInfluencingBounds( new BoundingSphere( centroid, radius * 100 ) );
            secondLight.setInfluencingBounds( new BoundingSphere( centroid, radius * 100 ) );
            thirdLight.setInfluencingBounds( new BoundingSphere( centroid, radius * 100 ) );
            trackBall.setSchedulingBounds( new BoundingSphere( centroid, radius * 60 ) );
            backGround.setApplicationBounds( new BoundingSphere( centroid, radius * 100 ) );
        }
        rotationGroup.addChild( b );
    }

    /**
     * @return a brand new sphere
     */
    private BranchGroup createJ3DSphere( Point3d translation ) {
        Appearance app = new Appearance();
        RenderingAttributes ra = new RenderingAttributes();
        ra.setDepthBufferEnable( true );
        ra.setDepthBufferWriteEnable( true );
        app.setRenderingAttributes( ra );

        ColoringAttributes ca = new ColoringAttributes();
        ca.setShadeModel( ColoringAttributes.SHADE_GOURAUD );
        ca.setCapability( ColoringAttributes.NICEST );
        app.setColoringAttributes( ca );

        Material material = new Material();
        material.setAmbientColor( new Color3f( Color.WHITE ) );
        material.setDiffuseColor( new Color3f( Color.RED ) );
        material.setSpecularColor( new Color3f( Color.BLUE ) );
        app.setMaterial( material );
        TransformGroup tg = new TransformGroup();
        Transform3D trans = new Transform3D();
        if ( translation != null ) {
            trans.setTranslation( new Vector3d( translation ) );
            tg.setTransform( trans );
        }

        BranchGroup b = new BranchGroup();
        b.setCapability( BranchGroup.ALLOW_DETACH );
        b.addChild( tg );

        // tg.addChild( new Sphere( 0.2f, app ) );
        tg.addChild( new ColorCube( 0.2f ) );
        return b;
    }

    private void createButtons( JPanel buttonPanel ) {
        JButton button = new JButton( "Open File" );
        button.setMnemonic( KeyEvent.VK_O );
        button.addActionListener( this );
        buttonPanel.add( button, FlowLayout.LEFT );
    }

    private void readFile( String fileName ) {

        if ( fileName == null || "".equals( fileName.trim() ) ) {
            throw new InvalidParameterException( "the file name may not be null or empty" );
        }
        fileName = fileName.trim();

        FeatureCollection fc = null;
        if ( fileName.toUpperCase().endsWith( ".SHP" ) ) {
            // File f = new File( fileName );
            try {
                ShapeFile file = new ShapeFileReader( fileName ).read();
                fc = file.getFeatureCollection();
            } catch ( IOException e ) {
                LOG.logError( "Could not open shape file: " + fileName + " because: " + e.getMessage() );
                return;
            } catch ( DBaseException e ) {
                LOG.logError( "Could not open shape file: " + fileName + " because: " + e.getMessage() );
                return;
            }
        } else if ( fileName.toUpperCase().endsWith( ".XML" ) || fileName.toUpperCase().endsWith( ".GML" ) ) {
            try {
                XMLFragment doc = new XMLFragment( new File( fileName ) );
                boolean isCityGML = ( doc.getRootElement().getOwnerDocument().lookupPrefix(
                                                                                            CommonNamespaces.CITYGMLNS.toASCIIString() ) != null )
                                    || CommonNamespaces.CITYGMLNS.toASCIIString().equals(
                                                                                          doc.getRootElement().getOwnerDocument().lookupNamespaceURI(
                                                                                                                                                      "" ) );

                if ( !isCityGML ) {
                    isCityGML = isCityGMLDefined( doc.getRootElement() );
                }
                LOG.logInfo( "The xmlfile " + ( ( isCityGML ) ? "contains" : "does not contain" ) + " Citygml" );
                if ( isCityGML ) {
                    // convert to gml
                    XSLTDocument transformer = new XSLTDocument( View3DFile.class.getResource( "toShape.xsl" ) );
                    doc = transformer.transform( doc );
                    System.out.println( "outgoingdoc: \n " + doc.getAsPrettyString() );
                }
                GMLFeatureCollectionDocument gmlDoc = new GMLFeatureCollectionDocument();
                gmlDoc.setRootElement( doc.getRootElement() );
                fc = gmlDoc.parse();
            } catch ( SAXException e ) {
                LOG.logError( "Could not open gml file: " + fileName + " because: " + e.getMessage() );
                return;
            } catch ( TransformerException e ) {
                LOG.logError( "Could not open gml file: " + fileName + " because: " + e.getMessage() );
                return;
            } catch ( XMLParsingException e ) {
                LOG.logError( "Could not open gml file: " + fileName + " because: " + e.getMessage() );
                return;
            } catch ( MalformedURLException e ) {
                LOG.logError( "Could not open gml file: " + fileName + " because: " + e.getMessage() );
                return;
            } catch ( IOException e ) {
                LOG.logError( "Could not open gml file: " + fileName + " because: " + e.getMessage() );
                return;
            }
        } else if ( fileName.toUpperCase().endsWith( ".WRL" ) || fileName.toUpperCase().endsWith( ".VRML" ) ) {
            VrmlLoader loader = new VrmlLoader();
            try {
                Scene scene = loader.load( fileName );
                if ( scene != null ) {
                    BranchGroup bg = scene.getSceneGroup();
                    BranchGroup result = new BranchGroup();
                    result.setCapability( BranchGroup.ALLOW_DETACH );
                    for ( int i = 0; i < bg.numChildren(); ++i ) {
                        removeTransformGroup( bg.getChild( i ), result );
                    }
                    LOG.logDebug( "Loaded branchgroup: " + bg.getName() );
                    addBranchGroupToScene( result );
                } else {
                    JOptionPane.showMessageDialog( this, "Could not create scene from file: " + fileName );
                }
                // } catch ( FileNotFoundException e ) {
                // LOG.logError( "Error while loading vrml from file: " + fileName, e );
                // JOptionPane.showMessageDialog( this, "Could not create scene from file: " +
                // fileName
                // + " because: "
                // + e.getMessage() );
            } catch ( IncorrectFormatException e ) {
                LOG.logError( "Error while loading vrml from file: " + fileName, e );
                JOptionPane.showMessageDialog( this, "Could not create scene from file: " + fileName + " because: "
                                                     + e.getMessage() );
            } catch ( ParsingErrorException e ) {
                LOG.logError( "Error while loading vrml from file: " + fileName, e );
                JOptionPane.showMessageDialog( this, "Could not create scene from file: " + fileName + " because: "
                                                     + e.getMessage() );
            } catch ( Exception e ) {
                LOG.logError( "Error while loading vrml from file: " + fileName, e );
                JOptionPane.showMessageDialog( this, "Could not create scene from file: " + fileName + " because: "
                                                     + e.getMessage() );
            } catch ( Throwable e ) {
                LOG.logError( "Error while loading vrml from file: " + fileName, e );
                JOptionPane.showMessageDialog( this, "Could not create scene from file: " + fileName + " because: "
                                                     + e.getMessage() );
            }
        }
        if ( fc != null ) {
            BranchGroup bg = new BranchGroup();
            bg.setCapability( BranchGroup.ALLOW_DETACH );

            Appearance app = new Appearance();
            RenderingAttributes ra = new RenderingAttributes();
            ra.setDepthBufferEnable( true );
            app.setRenderingAttributes( ra );
            Material material = new Material();
            material.setAmbientColor( new Color3f( Color.WHITE ) );
            material.setDiffuseColor( new Color3f( Color.RED ) );
            material.setSpecularColor( new Color3f( Color.BLUE ) );
            PolygonAttributes pa = new PolygonAttributes();
            pa.setCullFace( PolygonAttributes.CULL_NONE );
            pa.setBackFaceNormalFlip( true );
            pa.setPolygonMode( PolygonAttributes.POLYGON_FILL );
            app.setPolygonAttributes( pa );
            app.setMaterial( material );
            Envelope bbox = null;
            for ( int i = 0; i < fc.size(); ++i ) {
                Feature f = fc.getFeature( i );
                Geometry geom = f.getDefaultGeometryPropertyValue();
                if ( bbox == null ) {
                    bbox = geom.getEnvelope();
                } else {
                    try {
                        bbox = bbox.merge( geom.getEnvelope() );
                        LOG.logDebug( "merging the bboxes resulted in: " + bbox );
                    } catch ( GeometryException e ) {
                        LOG.logError( "Couldn't merge the bboxes" );
                        e.printStackTrace();
                    }
                }
            }
            Point3d centroid = new Point3d( 0, 0, 0 );
            if ( bbox != null ) {
                Point p = bbox.getCentroid();
                double zValue = p.getZ();
                if ( Double.isInfinite( zValue ) || Double.isNaN( zValue ) ) {
                    zValue = 0;
                }
                centroid.set( -p.getX(), -p.getY(), -zValue );
            }
            for ( int i = 0; i < fc.size(); ++i ) {
                Feature f = fc.getFeature( i );
                Geometry geom = f.getDefaultGeometryPropertyValue();
                Shape3D shape = mapGeometryToShape3D( geom, centroid );
                if ( shape != null ) {
                    shape.setAppearance( app );
                    bg.addChild( shape );
                } else {
                    System.out.println( "ERRORORORORORORO" );
                }
            }

            if ( bg.getAllChildren().hasMoreElements() ) {
                addBranchGroupToScene( bg );
            } else {
                LOG.logError( "Could not read any 3D-Info from the location: " + fileName );
            }
        }
        File f = new File( fileName );
        setTitle( WIN_TITLE + f.getName() );
    }

    private void removeTransformGroup( javax.media.j3d.Node trans, BranchGroup result ) {
        if ( trans instanceof Group ) {
            for ( int i = 0; i < ( (Group) trans ).numChildren(); ++i ) {
                javax.media.j3d.Node n = ( (Group) trans ).getChild( i );
                if ( n instanceof TransformGroup ) {
                    Transform3D t = new Transform3D();
                    ( (TransformGroup) n ).getTransform( t );
                    LOG.logDebug( "Setting old transform: " + t );
                    Matrix3d id = new Matrix3d();
                    id.setIdentity();
                    t.setRotation( id );
                    ( (TransformGroup) n ).setTransform( t );
                    LOG.logDebug( "To new transform: " + t );
                }
                removeTransformGroup( n, result );
            }
        } else {
            result.addChild( trans.cloneNode( true ) );
        }
    }

    /**
     * @param contextNode
     * @return true if the namespace "http://www.citygml.org/citygml/1/0/0" was found in one of the
     *         nodes of the dom-tree.
     */
    private boolean isCityGMLDefined( Node contextNode ) {

        boolean isCityGML = ( contextNode.lookupPrefix( CommonNamespaces.CITYGMLNS.toASCIIString() ) != null )
                            || CommonNamespaces.CITYGMLNS.toASCIIString().equals( contextNode.lookupNamespaceURI( null ) );
        if ( !isCityGML ) {
            NodeList nl = contextNode.getChildNodes();
            for ( int i = 0; i < nl.getLength(); ++i ) {
                isCityGML = isCityGMLDefined( nl.item( i ) );
                if ( isCityGML ) {
                    return true;
                }
            }
        }
        return isCityGML;
    }

    private Shape3D mapGeometryToShape3D( Geometry geom, Point3d translation ) {
        if ( geom instanceof Point ) {
            return createShape3D( (Point) geom, translation );
        } else if ( geom instanceof Curve ) {
            return createShape3D( (Curve) geom, translation );
        } else if ( geom instanceof Surface ) {
            return createShape3D( (Surface) geom, translation );
        } else if ( geom instanceof MultiSurface ) {
            return createShape3D( (MultiSurface) geom, translation );
        } else {
            if ( geom == null ) {
                LOG.logError( "Could not map the geometry which was not instantiated" );
            } else {
                LOG.logError( "Could not map the geometry: " + geom.getClass().getName() );
            }
            return null;
        }

        // if ( geom instanceof MultiPoint ) {
        // return new ShapeMultiPoint( (MultiPoint) g );
        // }
        //
        // if ( geom instanceof MultiCurve ) {
        // List<Curve> cs = Arrays.asList( ( (MultiCurve) g ).getAllCurves() );
        // return new ShapePolyline( cs );
        // }
        //

    }

    private Shape3D createShape3D( Point p, Point3d translation ) {
        GeometryArray geomArray = new PointArray( 1, GeometryArray.COORDINATES );
        double z = p.getZ();
        if ( Double.isInfinite( z ) || Double.isNaN( z ) ) {
            z = 0;
        }
        geomArray.setCoordinate( 0, new Point3d( p.getX() + translation.x, p.getY() + translation.y, z + translation.z ) );
        Shape3D result = new Shape3D( geomArray );
        result.setAppearanceOverrideEnable( true );
        return result;
    }

    private Shape3D createShape3D( Curve c, Point3d translation ) {
        int totalPoints = 0;
        List<Integer> failSegments = new ArrayList<Integer>();
        for ( int i = 0; i < c.getNumberOfCurveSegments(); ++i ) {
            try {
                totalPoints += c.getCurveSegmentAt( i ).getNumberOfPoints();
            } catch ( GeometryException e ) {
                LOG.logError( "Could not get CurveSegment at position: " + i );
                failSegments.add( new Integer( i ) );
            }
        }

        LineArray geomArray = new LineArray( totalPoints, GeometryArray.COORDINATES );
        for ( int i = 0, pointCounter = 0; i < c.getNumberOfCurveSegments(); ++i ) {
            if ( !failSegments.contains( new Integer( i ) ) ) {
                CurveSegment segment = null;
                try {
                    segment = c.getCurveSegmentAt( i );
                } catch ( GeometryException e ) {
                    // cannot happen.
                }
                for ( int k = 0; k < segment.getNumberOfPoints(); ++k ) {
                    Position p = segment.getPositionAt( k );
                    double z = p.getZ();
                    if ( Double.isInfinite( z ) || Double.isNaN( z ) ) {
                        z = 0;
                    }
                    geomArray.setCoordinate( pointCounter++, new Point3d( p.getX() + translation.x, p.getY()
                                                                                                    + translation.y,
                                                                          z + translation.z ) );
                }
            }
        }
        Shape3D result = new Shape3D( geomArray );
        result.setAppearanceOverrideEnable( true );
        return result;
    }

    /**
     * 
     * @param surface
     *            to be created
     * @param translation
     *            to origin of the scene
     * @return a Shape3D created from the surface.
     */
    private Shape3D createShape3D( Surface surface, Point3d translation ) {
        GeometryInfo geometryInfo = new GeometryInfo( GeometryInfo.POLYGON_ARRAY );

        Position[] pos = surface.getSurfaceBoundary().getExteriorRing().getPositions();
        Ring[] innerRings = surface.getSurfaceBoundary().getInteriorRings();
        int numberOfRings = 1;
        int numberOfCoordinates = 3 * ( pos.length  );
        if ( innerRings != null ) {
            for ( int i = 0; i < innerRings.length; i++ ) {
                numberOfRings++;
                numberOfCoordinates += ( 3 * innerRings[i].getPositions().length );
            }
        }

        float[] coords = new float[numberOfCoordinates];
        int contourCounts[] = { numberOfRings };
        int[] stripCounts = new int[numberOfRings];
        numberOfRings = 0;
        stripCounts[numberOfRings++] = pos.length;

        int z = 0;
        for ( int i = 0; i < pos.length; i++ ) {
            double zValue = pos[i].getZ();
            if ( Double.isInfinite( zValue ) || Double.isNaN( zValue ) ) {
                zValue = 0;
            }
            // LOG.logDebug( "Found a point in a surface: " + pos[i] );
            coords[z++] = (float) ( pos[i].getX() + translation.x );
            coords[z++] = (float) ( pos[i].getY() + translation.y );
            coords[z++] = (float) ( zValue + translation.z );
        }

        if ( innerRings != null ) {
            for ( int j = 0; j < innerRings.length; j++ ) {
                pos = innerRings[j].getPositions();
                stripCounts[numberOfRings++] = pos.length;
                for ( int i = 0; i < pos.length; i++ ) {
                    double zValue = pos[i].getZ();
                    if ( Double.isInfinite( zValue ) || Double.isNaN( zValue ) ) {
                        zValue = 0;
                    }
                    coords[z++] = (float) ( pos[i].getX() + translation.x );
                    coords[z++] = (float) ( pos[i].getY() + translation.y );
                    coords[z++] = (float) ( zValue + translation.z );
                }
            }
        }

        geometryInfo.setCoordinates( coords );
        geometryInfo.setStripCounts( stripCounts );
        geometryInfo.setContourCounts( contourCounts );
        geometryInfo.recomputeIndices();

        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals( geometryInfo );
        Shape3D result = new Shape3D( geometryInfo.getGeometryArray() );
        result.setCapability( Shape3D.ALLOW_GEOMETRY_READ );
        result.setCapability( Shape3D.ALLOW_GEOMETRY_WRITE );
        result.setAppearanceOverrideEnable( true );
        return result;
    }

    /**
     * @param multiSurface
     * @param translation
     * @return a Shape3D created from the multisurfaces.
     */
    private Shape3D createShape3D( MultiSurface multiSurface, Point3d translation ) {
        Shape3D result = new Shape3D();
        result.setCapability( Shape3D.ALLOW_GEOMETRY_READ );
        result.setCapability( Shape3D.ALLOW_GEOMETRY_WRITE );
        Surface[] allSurfaces = multiSurface.getAllSurfaces();
        for ( int surfaceCount = 0; surfaceCount < allSurfaces.length; ++surfaceCount ) {
            Surface surface = multiSurface.getSurfaceAt( surfaceCount );
            Shape3D s3D = createShape3D( surface, translation );
            result.addGeometry( s3D.getGeometry() );
        }
        result.setAppearanceOverrideEnable( true );
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e ) {
        Object source = e.getSource();
        if ( source instanceof JButton ) {
            // JFileChooser filechooser = new JFileChooser();
            // filechooser.setVisible( true );
            int result = fileChooser.showOpenDialog( this );
            if ( JFileChooser.APPROVE_OPTION == result ) {
                File f = fileChooser.getSelectedFile();
                if ( f != null ) {
                    String path = f.getAbsolutePath();
                    String dirpath = f.getParent();
                    System.out.println( "absolute Path: " + dirpath );
                    prefs.put( PREF_KEY, dirpath );
                    readFile( path );
                }

            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed( KeyEvent arg0 ) {
        // nottin
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased( KeyEvent arg0 ) {
        // nottin
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped( KeyEvent e ) {
        double x = 0;
        double y = 0;
        double z = 0;
        if ( e.getKeyChar() == 'q' ) {
            System.exit( 0 );
        } else if ( e.getKeyChar() == 'x' ) {
            x = 1;
        } else if ( e.getKeyChar() == 'X' ) {
            x = -1;
        } else if ( e.getKeyChar() == 'y' ) {
            y = 1;
        } else if ( e.getKeyChar() == 'Y' ) {
            y = -1;
        } else if ( e.getKeyChar() == 'z' ) {
            z = 1;
        } else if ( e.getKeyChar() == 'Z' ) {
            z = -1;
        }

        TransformGroup viewToWorld = simpleUniverse.getViewingPlatform().getViewPlatformTransform();
        Transform3D trans = new Transform3D();
        viewToWorld.getTransform( trans );
        trans.invert();
        Vector3d translation = new Vector3d();
        trans.get( translation );

        x += translation.x;
        y += translation.y;
        z += translation.z;
        Point3d eye = new Point3d( x, y, z );
        trans.lookAt( eye, centroid, new Vector3d( 0, 1, 0 ) );
        LOG.logDebug( "Trans after:\n" + trans + "\ncentroid: " + centroid );
        Vector3d dist = new Vector3d( centroid );
        dist.sub( eye );
        trackBall.setSchedulingBounds( new BoundingSphere( centroid, dist.length() ) );
        viewToWorld.setTransform( trans );

    }

    /**
     * @param args
     */
    public static void main( String[] args ) {

        View3DFile viewer = new View3DFile( "/tmp/test.xml" );
        viewer.toFront();
        // View3DFile viewer = new View3DFile( true );
        // viewer.toFront();

    }

    /**
     * 
     * The <code>CustomFileFilter</code> class adds functionality to the filefilter mechanism of
     * the JFileChooser.
     * 
     * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
     * 
     * @author last edited by: $Author:$
     * 
     * @version $Revision:$, $Date:$
     * 
     */
    private class CustomFileFilter extends FileFilter {

        private List<String> acceptedExtensions;

        private String desc;

        /**
         * @param acceptedExtensions
         *            list of extensions this filter accepts.
         */
        CustomFileFilter( List<String> acceptedExtensions, String description ) {
            this.acceptedExtensions = new ArrayList<String>( acceptedExtensions.size() );
            for ( String s : acceptedExtensions ) {
                if ( s.startsWith( "." ) ) {
                    s = s.substring( 1 );
                }
                this.acceptedExtensions.add( s.trim().toUpperCase() );
            }
            desc = description;
        }

        @Override
        public boolean accept( File pathname ) {
            if ( pathname.isDirectory() ) {
                return true;
            }

            String extension = getExtension( pathname );
            if ( extension != null ) {
                if ( acceptedExtensions.contains( extension.trim().toUpperCase() ) ) {
                    return true;
                }
            }
            return false;
        }

        private String getExtension( File f ) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf( '.' );

            if ( i > 0 && i < s.length() - 1 ) {
                ext = s.substring( i + 1 ).toLowerCase();
            }
            return ext;
        }

        @Override
        public String getDescription() {
            return desc;
        }
    }
}
