//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/coverage/PaletteInterpretation.java $
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
package org.deegree.model.coverage;

/**
 * Describes the color entry in a color table.
 * 
 * @UML codelist CV_PaletteInterpretation
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * 
 * @see ColorInterpretation
 * @see SampleDimension
 */
public final class PaletteInterpretation extends CodeList {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -7387623392932592485L;

    /**
     * Gray Scale color palette.
     * 
     * @UML conditional CV_Gray
     * @see java.awt.color.ColorSpace#TYPE_GRAY
     */
    public static final PaletteInterpretation GRAY = new PaletteInterpretation( "GRAY", 0 );

    /**
     * RGB (Red Green Blue) color palette.
     * 
     * @UML conditional CV_RGB
     * @see java.awt.color.ColorSpace#TYPE_RGB
     */
    public static final PaletteInterpretation RGB = new PaletteInterpretation( "RGB", 1 );

    /**
     * CYMK (Cyan Yellow Magenta blacK) color palette.
     * 
     * @UML conditional CV_CMYK
     * @see java.awt.color.ColorSpace#TYPE_CMYK
     */
    public static final PaletteInterpretation CMYK = new PaletteInterpretation( "CMYK", 2 );

    /**
     * HSL (Hue Saturation Lightness) color palette.
     * 
     * @UML conditional CV_HLS
     * @see java.awt.color.ColorSpace#TYPE_HLS
     */
    public static final PaletteInterpretation HLS = new PaletteInterpretation( "HLS", 3 );

    /**
     * List of all enumerations of this type.
     */
    private static final PaletteInterpretation[] VALUES = new PaletteInterpretation[] { GRAY, RGB, CMYK, HLS };

    /**
     * Constructs an enum with the given name.
     */
    private PaletteInterpretation( final String name, final int ordinal ) {
        super( name, ordinal );
    }

    /**
     * Returns the list of <code>PaletteInterpretation</code>s.
     */
    public static PaletteInterpretation[] values() {
        return VALUES.clone();
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public CodeList[] family() {
        return values();
    }
}
