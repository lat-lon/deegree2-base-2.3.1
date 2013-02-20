// $HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/coverage/grid/AbstractGridCoverage.java $
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
package org.deegree.model.coverage.grid;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.BootLogger;
import org.deegree.graphics.transformation.GeoTransform;
import org.deegree.graphics.transformation.WorldToScreenTransform;
import org.deegree.model.coverage.AbstractCoverage;
import org.deegree.model.coverage.Coverage;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.ogcwebservices.wcs.describecoverage.CoverageOffering;
import org.deegree.processing.raster.converter.Image2RawData;

/**
 * Represent the basic implementation which provides access to grid coverage data. A
 * <code>GC_GridCoverage</code> implementation may provide the ability to update grid values.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version 2.11.2002
 */

public abstract class AbstractGridCoverage extends AbstractCoverage implements GridCoverage {

    private static final ILogger LOG = LoggerFactory.getLogger( AbstractGridCoverage.class );

    private GridGeometry gridGeometry = null;

    private boolean isEditable = false;

    protected static float offset;

    protected static float scaleFactor;

    static {
        // 16 bit coverage probably does not contain original values but scaled values
        // with an offset to enable handling of float values. For correct handling of
        // these coverages offset and scale factor must be known
        InputStream is = ShortGridCoverage.class.getResourceAsStream( "16bit.properties" );
        Properties props = new Properties();
        try {
            props.load( is );
        } catch ( IOException e ) {
            BootLogger.logError( e.getMessage(), e );
        }
        offset = Float.parseFloat( props.getProperty( "offset" ) );
        scaleFactor = Float.parseFloat( props.getProperty( "scaleFactor" ) );
    }

    /**
     * @param coverageOffering
     * @param envelope
     */
    public AbstractGridCoverage( CoverageOffering coverageOffering, Envelope envelope ) {
        super( coverageOffering, envelope );
    }

    /**
     * @param coverageOffering
     * @param sources
     * @param envelope
     */
    public AbstractGridCoverage( CoverageOffering coverageOffering, Envelope envelope, Coverage[] sources ) {
        super( coverageOffering, envelope, sources );
    }

    /**
     * 
     * @param coverageOffering
     * @param envelope
     * @param isEditable
     */
    public AbstractGridCoverage( CoverageOffering coverageOffering, Envelope envelope, boolean isEditable ) {
        super( coverageOffering, envelope );
        this.isEditable = isEditable;
    }

    /**
     * 
     * @param coverageOffering
     * @param envelope
     * @param isEditable
     */
    public AbstractGridCoverage( CoverageOffering coverageOffering, Envelope envelope, CoordinateSystem crs,
                                 boolean isEditable ) {
        super( coverageOffering, envelope, null, crs );
        this.isEditable = isEditable;
    }

    /**
     * 
     * @param coverageOffering
     * @param envelope
     * @param sources
     * @param isEditable
     */
    public AbstractGridCoverage( CoverageOffering coverageOffering, Envelope envelope, Coverage[] sources,
                                 boolean isEditable ) {
        super( coverageOffering, envelope, sources );
        this.isEditable = isEditable;
    }

    /**
     * 
     * @param coverageOffering
     * @param envelope
     * @param sources
     * @param crs
     * @param isEditable
     */
    public AbstractGridCoverage( CoverageOffering coverageOffering, Envelope envelope, Coverage[] sources,
                                 CoordinateSystem crs, boolean isEditable ) {
        super( coverageOffering, envelope, sources, crs );
        this.isEditable = isEditable;
    }

    /**
     * Returns <code>true</code> if grid data can be edited.
     * 
     * @return <code>true</code> if grid data can be edited.
     */
    public boolean isDataEditable() {
        return isEditable;
    }

    /**
     * Information for the grid coverage geometry. Grid geometry includes the valid range of grid
     * coordinates and the georeferencing.
     * 
     * @return the information for the grid coverage geometry.
     * 
     */
    public GridGeometry getGridGeometry() {
        return gridGeometry;
    }

    /**
     * this is a deegree convenience method which returns the source image of an
     * <tt>ImageGridCoverage</tt>. In procipal the same can be done with the
     * getRenderableImage(int xAxis, int yAxis) method. but creating a <tt>RenderableImage</tt>
     * image is very slow.
     * 
     * @param xAxis
     *            Dimension to use for the <var>x</var> axis.
     * @param yAxis
     *            Dimension to use for the <var>y</var> axis.
     * @return
     */
    abstract public BufferedImage getAsImage( int xAxis, int yAxis );

    protected BufferedImage paintImage( BufferedImage targetImg, Envelope targetEnv, BufferedImage sourceImg,
                                        Envelope sourceEnv ) {
        return this.paintImage( targetImg, null, targetEnv, sourceImg, sourceEnv );
    }

    /**
     * renders a source image onto the correct position of a target image according to threir
     * geographic extends (Envelopes).
     * 
     * @param targetImg
     * @param targetEnv
     * @param sourceImg
     * @param sourceEnv
     * @return targetImg with sourceImg rendered on
     */
    protected BufferedImage paintImage( BufferedImage targetImg, float[][] data, Envelope targetEnv,
                                        BufferedImage sourceImg, Envelope sourceEnv ) {

        int w = targetImg.getWidth();
        int h = targetImg.getHeight();

        GeoTransform gt = new WorldToScreenTransform( targetEnv.getMin().getX(), targetEnv.getMin().getY(),
                                                      targetEnv.getMax().getX(), targetEnv.getMax().getY(), 0, 0,
                                                      w - 1, h - 1 );
        int x1 = (int) Math.round( gt.getDestX( sourceEnv.getMin().getX() ) );
        int y1 = (int) Math.round( gt.getDestY( sourceEnv.getMax().getY() ) );
        int x2 = (int) Math.round( gt.getDestX( sourceEnv.getMax().getX() ) );
        int y2 = (int) Math.round( gt.getDestY( sourceEnv.getMin().getY() ) );

        if ( Math.abs( x2 - x1 ) > 0 && Math.abs( y2 - y1 ) > 0 ) {

            sourceImg = scale( sourceImg, targetImg, sourceEnv, targetEnv );

            Raster raster = targetImg.getData();
            DataBuffer targetBuffer = raster.getDataBuffer();
            raster = sourceImg.getData();
            DataBuffer srcBuffer = raster.getDataBuffer();
            int srcPs = sourceImg.getColorModel().getPixelSize();
            int targetPs = targetImg.getColorModel().getPixelSize();
            float[][] newData = null;
            if ( srcPs == 16 && targetPs == 16 ) {
                Image2RawData i2r = new Image2RawData( sourceImg, 1f / scaleFactor, -1 * offset );
                // do not use image api if target bitDepth = 16
                newData = i2r.parse();
            }
            for ( int i = 0; i < sourceImg.getWidth(); i++ ) {
                for ( int j = 0; j < sourceImg.getHeight(); j++ ) {
                    if ( x1 + i < targetImg.getWidth() && y1 + j < targetImg.getHeight() ) {
                        int srcPos = sourceImg.getWidth() * j + i;
                        int targetPos = targetImg.getWidth() * ( y1 + j ) + ( i + x1 );
                        if ( targetPs == 16 && srcPs == 16 ) {
                            // int v = srcBuffer.getElem( srcPos );
                            // targetBuffer.setElem( targetPos, v );
                            data[y1 + j][x1 + i] = newData[j][i];
                        } else if ( targetPs == 16 && srcPs == 32 ) {
                            int v = sourceImg.getRGB( i, j );
                            float f = Float.intBitsToFloat( v ) * 10f;
                            targetBuffer.setElem( targetPos, Math.round( f ) );
                        } else if ( targetPs == 32 && srcPs == 16 ) {
                            float f = srcBuffer.getElem( srcPos ) / 10f;
                            targetBuffer.setElem( targetPos, Float.floatToIntBits( f ) );
                            // TODO
                            // data[y1 + j][x1 + i] = f;
                        } else {
                            targetImg.setRGB( x1 + i, y1 + j, sourceImg.getRGB( i, j ) );
                        }
                    }
                }
            }
            if ( ( targetPs == 16 && srcPs == 16 ) || ( targetPs == 16 && srcPs == 32 )
                 || ( targetPs == 32 && srcPs == 16 ) ) {
                targetImg.setData( Raster.createRaster( targetImg.getSampleModel(), targetBuffer, null ) );
            }
        }

        return targetImg;
    }

    /**
     * 
     * @param sourceImg
     * @param targetImg
     * @param srcEnv
     * @param trgEnv
     * @return
     */
    private BufferedImage scale( BufferedImage sourceImg, BufferedImage targetImg, Envelope srcEnv, Envelope trgEnv ) {

        double srcXres = srcEnv.getWidth() / ( sourceImg.getWidth() - 1f );
        double srcYres = srcEnv.getHeight() / ( sourceImg.getHeight() - 1f );

        double trgXres = trgEnv.getWidth() / ( targetImg.getWidth() - 1f );
        double trgYres = trgEnv.getHeight() / ( targetImg.getHeight() - 1f );

        float sx = (float) ( srcXres / trgXres );
        float sy = (float) ( srcYres / trgYres );

        if ( ( sy < 0.9999 ) || ( sy > 1.0001 ) || ( sx < 0.9999 ) || ( sx > 1.0001 ) ) {
            try {
                ParameterBlock pb = new ParameterBlock();
                pb.addSource( sourceImg );

                LOG.logDebug( "Scale image: by factors: " + sx + ' ' + sy );
                pb.add( sx ); // The xScale
                pb.add( sy ); // The yScale
                pb.add( 0.0F ); // The x translation
                pb.add( 0.0F ); // The y translation
                pb.add( new InterpolationNearest() ); // The interpolation
                // pb.add( new InterpolationBilinear() ); // The interpolation
                // Create the scale operation
                RenderedOp ro = JAI.create( "scale", pb, null );
                sourceImg = ro.getAsBufferedImage();
            } catch ( Exception e ) {
                LOG.logError( e.getMessage(), e );
            }
        }
        return sourceImg;
    }
}
