//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/gridprocessing/ImgResize.java $
/*
 * ImgResize.java
 *
 * Created on 23. Januar 2003, 12:22
 */
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
package org.deegree.model.gridprocessing;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 10547 $, $Date: 2008-03-11 09:40:28 +0100 (Di, 11. Mär 2008) $
 */
public class ImgResize {

    private float[][] inData = null;

    private float[][] outData = null;

    public ImgResize( float[][] data, int newWidth, int newHeight ) {
        this.inData = data;
        outData = new float[newWidth][newHeight];
    }

    private int sign( int x ) {
        int k = ( ( x ) > 0 ? 1 : -1 );
        return k;
    }

    /*
     * Stretches a horizontal source line onto a horizontal destination line. Used by RectStretch.
     * Entry: x1,x2 - x-coordinates of the destination line y1,y2 - x-coordinates of the source line
     * yr - y-coordinate of source line yw - y-coordinate of destination line
     */
    private void stretch( int x1, int x2, int y1, int y2, int yr, int yw ) {
        int dx, dy, e, d, dx2;
        int sx, sy;
        float value = 0;
        dx = Math.abs( x2 - x1 );
        dy = Math.abs( y2 - y1 );
        sx = sign( x2 - x1 );
        sy = sign( y2 - y1 );
        e = ( dy << 1 ) - dx;
        dx2 = dx << 1;
        dy = dy << 1;
        for ( d = 0; d <= dx; d++ ) {
            value = inData[yr][y1];
            outData[yw][x1] = value;
            while ( e >= 0 ) {
                y1 += sy;
                e -= dx2;
            }
            x1 += sx;
            e += dy;
        }
    }

    /**
     * RectStretch enlarges or diminishes a source rectangle of a bitmap to a destination rectangle.
     * The source rectangle is selected by the two points (xs1,ys1) and (xs2,ys2), and the
     * destination rectangle by (xd1,yd1) and (xd2,yd2). Since readability of source-code is wanted,
     * some optimizations have been left out for the reader: It�s possible to read one line at a
     * time, by first stretching in x-direction and then stretching that bitmap in y-direction.
     * Entry: xs1,ys1 - first point of source rectangle xs2,ys2 - second point of source rectangle
     * xd1,yd1 - first point of destination rectangle xd2,yd2 - second point of destination
     * rectangle
     */
    public float[][] rectStretch() {

        int xs1 = 0;
        int ys1 = 0;
        int xs2 = inData[0].length - 1;
        int ys2 = inData.length - 1;
        int xd1 = 0;
        int yd1 = 0;
        int xd2 = outData[0].length - 1;
        int yd2 = outData.length - 1;

        int dx, dy, e, d, dx2;
        int sx, sy;
        dx = Math.abs( yd2 - yd1 );
        dy = Math.abs( ys2 - ys1 );
        sx = sign( yd2 - yd1 );
        sy = sign( ys2 - ys1 );
        e = ( dy << 1 ) - dx;
        dx2 = dx << 1;
        dy = dy << 1;
        for ( d = 0; d <= dx; d++ ) {
            stretch( xd1, xd2, xs1, xs2, ys1, yd1 );
            while ( e >= 0 ) {
                ys1 += sy;
                e -= dx2;
            }
            yd1 += sx;
            e += dy;
        }
        return outData;
    }

    public float[][] simpleStretch() {
        double dx = inData[0].length / (double) outData[0].length;
        double dy = inData.length / (double) outData.length;

        double py = 0.0;
        for ( int y = 0; y < outData.length; y++ ) {
            double px = 0.0;
            for ( int x = 0; x < outData[0].length; x++ ) {
                float v = inData[(int) py][(int) py];
                outData[y][x] = v;
                px += dx;
            }
            py += dy;
        }
        return outData;
    }

}
