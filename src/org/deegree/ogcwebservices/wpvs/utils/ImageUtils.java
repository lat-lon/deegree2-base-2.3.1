//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/utils/ImageUtils.java $
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

package org.deegree.ogcwebservices.wpvs.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

/**
 * Little utility class responsible for filtering an image and making it 
 * transparent based on an array of colors considered to be transparent.
 * Users of this class initalize an object with a non-null <code>Color</code> array
 * that represents colors, which are supposed to be completely transparent.
 * By calling <code>#filter( Image )</code>, the colors found in image are substituted
 * by transparent pixels.
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: rbezema $
 * 
 * $Revision: 12269 $, $Date: 2008-06-10 14:59:03 +0200 (Di, 10. Jun 2008) $
 */
public class ImageUtils {
	
	
	private ImageFilter filter;

	/**
	 *	Creates a new <code>ImageUtil</code> object. 
	 * @param transparentColors the colors that will be substituted by a completely 
	 * transparent color ('0x00FFFFFF'). transparentColors cannot be null. 
	 */
	public ImageUtils( Color[] transparentColors ){
		
		if ( transparentColors == null ){
			throw new NullPointerException( "transparentColors cannot be null!" );
		}
		
		int[] intColors = new int[ transparentColors.length ];
        for ( int j = 0; j < intColors.length; j++ ) {
            intColors[j] = transparentColors[j].getRGB();
        }
		filter = new ImageUtils.ColorsToTransparentFilter( intColors );

	}
	
	/**
	 * Creates a Imagefilter which makes an image transparent.
	 */
	public ImageUtils( ){
		filter = new ImageUtils.TransparentImageFilter();

	}	
	
	/**
	 * Filters an image and return a new partially transparent image. 
	 * @param image the image that is to be filtered.
	 * @return a new image whose colors are substituted accordign to the 
	 * input traparent colors. The input image cannot be null.
	 */
	public Image filterImage( BufferedImage image ){
		
		if ( image == null ){
			throw new NullPointerException( "Image cannot be null!" );
		}
		image = ensureRGBAImage( image );
		ImageProducer imgProducer = 
            new FilteredImageSource( image.getSource(), filter );
		
		return java.awt.Toolkit.getDefaultToolkit().createImage( imgProducer );
	}
	
	/**
	 * Checks if the type of <code>img</code> is <code>BufferedImage.TYPE_INT_ARGB</code>
	 * and if is not, create a new one, just like <code>img</code> but with transparency
	 * @param image the image to be checked. Cannot be null.
	 * @return the same image, if its type is <code>BufferedImage.TYPE_INT_ARGB</code>, or a 
	 * new transparent one. 
	 */
	public BufferedImage ensureRGBAImage( BufferedImage image ) {
		
		if ( image == null ){
			throw new NullPointerException( "Image cannot be null!" );
		}
		
	    if ( image.getType() != BufferedImage.TYPE_INT_ARGB ) {
	        BufferedImage tmp = new BufferedImage( image.getWidth(), image.getHeight(), 
	                                               BufferedImage.TYPE_INT_ARGB );                
	        Graphics g = tmp.getGraphics();
	        g.drawImage( image, 0, 0, null );
	        g.dispose();
	        image = tmp;
	    }
	    return image;
	}
	
	/**
	 * An <code>RGBImageFilter</code> to substitute all input colors by a completely
	 * transparent one. 
	 * 
	 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
	 * @author last edited by: $Author: rbezema $
	 * 
	 * $Revision: 12269 $, $Date: 2008-06-10 14:59:03 +0200 (Di, 10. Jun 2008) $
	 */
	public class ColorsToTransparentFilter extends RGBImageFilter {
	    
		private static final int TRANSPARENT_COLOR = 0x00FFFFFF;  
		
		private final int[] colors;

		/**
		 * 0.975f
		 */
		float alphaPercent = 0.975f;
		
	    /**
	     * @param colors the Colors which should be transparent
	     */
	    public ColorsToTransparentFilter( int[] colors ) { 
	    	if ( colors == null || colors.length == 0){
				throw new NullPointerException( "colors cannot be null!" );
			}
	        this.colors = colors;
	        canFilterIndexColorModel = true; 
	    }
	    
	    /**
	     * @see java.awt.image.RGBImageFilter
	     */
	    @Override
        public int filterRGB(int x, int y, int argb) {
	        if( shouldBeTransparent( argb ) ) {
	           return TRANSPARENT_COLOR; // mask alpha bits to zero
//	        	argb = TRANSPARENT_COLOR;
	        }
	        return argb;
	        /*int a = ( argb >> 24) & 0xff;
	        a *= alphaPercent;
	        return ( ( argb & 0x00ffffff) | (a << 24));*/
	    }
	    
	    /**
	     * Compares <code>color</code> with TRANSPARENT_COLOR
	     * @param color color to be compared to TRANSPARENT_COLOR 
	     * @return true if color = TRANSPARENT_COLOR
	     */
	    private boolean shouldBeTransparent( int color ) {
	        for ( int i = 0; i < colors.length; i++ ) {
	            if ( colors[i] == color ) {
	                return true;
	            }
	        }
	        return false;
	    }
	}
	
	/* from Java AWT reference, chap. 12*/
	/**
	 * The <code>TransparentImageFilter</code> class filters an RGB-Pixel with a transparency.
	 *
     * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
	 *
	 * @author last edited by: $Author: rbezema $
	 *
	 * @version $Revision: 12269 $, $Date: 2008-06-10 14:59:03 +0200 (Di, 10. Jun 2008) $
	 *
	 */
	class TransparentImageFilter extends RGBImageFilter {
	    /**
	     * the alpha chanel.
	     */
	    float alphaPercent;
	    /**
	     * A TransparentImageFilter with no transparency
	     */
	    public TransparentImageFilter () {
	        this (1f);
	    }
	    /**
	     * @param aPercent of the transparency
	     */
	    public TransparentImageFilter (float aPercent) {
	        if ((aPercent < 0.0) || (aPercent > 1.0))
	            aPercent = 1;
	        alphaPercent = aPercent;
	        canFilterIndexColorModel = true;
	    }
	    @Override
        public int filterRGB (int x, int y, int rgb) {
	        int a = (rgb >> 24) & 0xff;
	        a *= alphaPercent;
	        
	        return ((rgb & 0x00ffffff) | (a << 24));
	    }
	}	
}

