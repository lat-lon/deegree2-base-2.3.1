//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/processing/raster/BufferedImageDataMatrix.java $
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
package org.deegree.processing.raster;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10609 $, $Date: 2008-03-18 09:46:37 +0100 (Tue, 18 Mar 2008) $
 */
public class BufferedImageDataMatrix implements DataMatrix {

    private int width = 0;

    private int height = 0;

    private DataBuffer data = null;

    /**
     * 
     * @param bi
     */
    public BufferedImageDataMatrix( BufferedImage bi ) {
        data = bi.getData().getDataBuffer();
        height = bi.getHeight();
        width = bi.getWidth();
    }

    /**
     * @param x
     * @param y
     * @return the float values at x,y.
     */
    public double[] getCellAt( int x, int y ) {
        int index = ( width * y ) + x;
        double[] val = new double[data.getNumBanks()];
        for ( int i = 0; i < val.length; i++ ) {
            val[i] = data.getElemFloat( index, i );
        }
        return val;
    }

    /**
     * returns the data matrix width (number of cells in x-direction)
     * 
     * @return the data matrix width (number of cells in x-direction)
     */
    public int getHeight() {
        return height;
    }

    /**
     * returns the data matrix height (number of cells in y-direction)
     * 
     * @return the data matrix height (number of cells in y-direction)
     */
    public int getWidth() {
        return width;
    }

}
