//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/processing/raster/converter/RawData2Image.java $
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
package org.deegree.processing.raster.converter;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

/**
 * Offeres methods to wrap raw number data into a <code>BufferedImage</code>
 * 
 * 
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 */
public class RawData2Image {

    /**
     * 
     * @param type
     *            the desired DataBuffer type
     * @param width
     * @param height
     * @return a new Image
     */
    private static BufferedImage createImage( int type, int width, int height ) {

        ColorModel ccm = new ComponentColorModel( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null, false, false,
                                                  BufferedImage.OPAQUE, type );

        WritableRaster wr = ccm.createCompatibleWritableRaster( width, height );

        return new BufferedImage( ccm, wr, false, new Hashtable<Object, Object>() );

    }

    /**
     * @return result will be a <code>BufferedImage</code> with a GRAY colorspace and
     *         <code>DataBuffer.TYPE_BYTE</code>
     * 
     * @param data
     *            data to wrap
     */
    public static BufferedImage rawData2Image( byte[][] data ) {

        BufferedImage img = createImage( DataBuffer.TYPE_BYTE, data[0].length, data.length );
        Raster raster = img.getData();
        DataBuffer buffer = raster.getDataBuffer();

        for ( int i = 0; i < data.length; i++ ) {
            for ( int j = 0; j < data[0].length; j++ ) {
                int pos = data[0].length * i + j;
                buffer.setElem( pos, data[i][j] );
            }
        }
        img.setData( Raster.createRaster( img.getSampleModel(), buffer, null ) );
        return img;
    }

    /**
     * @return result will be a <code>BufferedImage</code> with a GRAY colorspace and
     *         <code>DataBuffer.TYPE_USHORT</code>
     * 
     * @param data
     *            data to wrap
     */
    public static BufferedImage rawData2Image( short[][] data ) {
        BufferedImage img = createImage( DataBuffer.TYPE_USHORT, data[0].length, data.length );
        Raster raster = img.getData();
        DataBuffer buffer = raster.getDataBuffer();

        for ( int i = 0; i < data.length; i++ ) {
            for ( int j = 0; j < data[0].length; j++ ) {
                int pos = data[0].length * i + j;
                buffer.setElem( pos, data[i][j] );
            }
        }
        img.setData( Raster.createRaster( img.getSampleModel(), buffer, null ) );
        return img;
    }

    /**
     * @return result will be a <code>BufferedImage</code> with a GRAY colorspace and
     *         <code>DataBuffer.TYPE_INT</code>
     * 
     * @param data
     *            data to wrap
     */
    public static BufferedImage rawData2Image( int[][] data ) {
        BufferedImage img = createImage( DataBuffer.TYPE_INT, data[0].length, data.length );
        Raster raster = img.getData();
        DataBuffer buffer = raster.getDataBuffer();

        for ( int i = 0; i < data.length; i++ ) {
            for ( int j = 0; j < data[0].length; j++ ) {
                int pos = data[0].length * i + j;
                buffer.setElem( pos, data[i][j] );
            }
        }
        img.setData( Raster.createRaster( img.getSampleModel(), buffer, null ) );
        return img;
    }

    /**
     * Float data requires 4 Byte (32Bit) per data cell. It is common to reduce data depth by
     * multiplying float values with 10 and map the rounded result to an unsigned short value
     * (16Bit). This behavior can be controlled by the second parameter passed to this method. If
     * set to <code>true</code> a image with 32Bit data depth and INT Databuffer will be created.
     * Otherwise a 16Bit Image with an USHORT Databuffer will be created.<br>
     * A default scale of 10 and no offset will be used
     * 
     * @return result will be a <code>BufferedImage</code> with a GRAY colorspace.
     * 
     * @param data
     *            data to wrap
     * @param use32Bits
     */
    public static BufferedImage rawData2Image( float[][] data, boolean use32Bits ) {
        return rawData2Image( data, use32Bits, 10, 0 );
    }

    /**
     * Float data requires 4 Byte (32Bit) per data cell. It is common to reduce data depth by
     * multiplying float values with 10 and map the rounded result to an unsigned short value
     * (16Bit). This behavior can be controlled by the second parameter passed to this method. If
     * set to <code>true</code> a image with 32Bit data depth and INT Databuffer will be created.
     * Otherwise a 16Bit Image with an USHORT Databuffer will be created.
     * 
     * @param data
     * @param use32Bits
     * @param scale
     * @param offset
     * @return result will be a <code>BufferedImage</code> with a GRAY colorspace.
     */
    public static BufferedImage rawData2Image( float[][] data, boolean use32Bits, float scale, float offset ) {
        BufferedImage img = null;
        if ( use32Bits ) {
            img = new BufferedImage( data[0].length, data.length, BufferedImage.TYPE_INT_ARGB );
            Raster raster = img.getData();
            DataBuffer buffer = raster.getDataBuffer();
            for ( int i = 0; i < data.length; i++ ) {
                for ( int j = 0; j < data[0].length; j++ ) {
                    int pos = data[0].length * i + j;
                    buffer.setElem( pos, Float.floatToIntBits( data[i][j] ) );
                }
            }
            img.setData( Raster.createRaster( img.getSampleModel(), buffer, null ) );
        } else {
            img = createImage( DataBuffer.TYPE_USHORT, data[0].length, data.length );
            Raster raster = img.getData();
            DataBuffer buffer = raster.getDataBuffer();
            for ( int i = 0; i < data.length; i++ ) {
                for ( int j = 0; j < data[0].length; j++ ) {
                    int pos = data[0].length * i + j;
                    buffer.setElem( pos, Math.round( ( data[i][j] + offset ) * scale ) );
                }
            }
            img.setData( Raster.createRaster( img.getSampleModel(), buffer, null ) );
        }

        return img;
    }

    /**
     * Special version of the method which creates a new BufferedImage according to the models
     * given.
     * 
     * @return result will be a <code>BufferedImage</code> with the given color model
     * 
     * @param data
     *            data to wrap
     * @param use32Bits
     * @param colorModel
     * @param sampleModel
     */
    public static BufferedImage rawData2Image( float[][] data, boolean use32Bits, ColorModel colorModel,
                                               SampleModel sampleModel ) {

        BufferedImage img = null;
        if ( use32Bits ) {
            SampleModel sm = sampleModel.createCompatibleSampleModel( data[0].length, data.length );

            WritableRaster raster = Raster.createWritableRaster( sm, null );

            img = new BufferedImage( colorModel, raster, true, new Hashtable<Object, Object>() );

            for ( int i = 0; i < data.length; i++ ) {
                for ( int j = 0; j < data[i].length; j++ ) {
                    img.setRGB( j, i, Float.floatToRawIntBits( data[i][j] ) );
                }
            }

        } else {
            img = createImage( DataBuffer.TYPE_USHORT, data[0].length, data.length );
            Raster raster = img.getData();
            DataBuffer buffer = raster.getDataBuffer();
            for ( int i = 0; i < data.length; i++ ) {
                for ( int j = 0; j < data[i].length; j++ ) {
                    int pos = data[i].length * i + j;
                    buffer.setElem( pos, Math.round( data[i][j] * 10 ) );
                }
            }
            img.setData( Raster.createRaster( img.getSampleModel(), buffer, null ) );
        }

        return img;
    }
}
