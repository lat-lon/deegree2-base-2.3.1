//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/coverage/AbstractCoverage.java $
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

import java.awt.image.renderable.RenderableImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.deegree.datatypes.CodeList;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.metadata.iso19115.Keywords;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.Position;
import org.deegree.ogcwebservices.wcs.describecoverage.AxisDescription;
import org.deegree.ogcwebservices.wcs.describecoverage.CoverageOffering;
import org.opengis.pt.PT_CoordinatePoint;
import org.opengis.pt.PT_Envelope;

/**
 * Provides access to an OpenGIS coverage. The essential property of coverage is to be able to
 * generate a value for any point within its domain. How coverage is represented internally is not a
 * concern.
 * 
 * For example consider the following different internal representations of coverage:<br>
 * <OL>
 * <li>A coverage may be represented by a set of polygons which exhaustively tile a plane (that is
 * each point on the plane falls in precisely one polygon). The value returned by the coverage for a
 * point is the value of an attribute of the polygon that contains the point.</li>
 * <li>A coverage may be represented by a grid of values. The value returned by the coverage for a
 * point is that of the grid value whose location is nearest the point.</li>
 * <li>Coverage may be represented by a mathematical function. The value returned by the coverage
 * for a point is just the return value of the function when supplied the coordinates of the point
 * as arguments.</li>
 * <li>Coverage may be represented by combination of these. For example, coverage may be
 * represented by a combination of mathematical functions valid over a set of polynomials.</LI>
 * </OL>
 * 
 * A coverage has a corresponding {@link SampleDimension} for each sample dimension in the coverage.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9343 $, $Date: 2007-12-27 14:30:32 +0100 (Thu, 27 Dec 2007) $
 */
public abstract class AbstractCoverage implements Coverage, Serializable {

    protected CoverageOffering coverageOffering = null;

    private CoordinateSystem crs = null;

    private PT_Envelope envelope = null;

    protected int numSources = 0;

    protected Coverage[] sources = null;

    private String[] dimensionNames = null;

    /**
     * @param coverageOffering
     * @param env
     */
    public AbstractCoverage( CoverageOffering coverageOffering, Envelope env ) {
        this( coverageOffering, env, null );
    }

    /**
     * @param coverageOffering
     * @param env
     * @param sources
     */
    public AbstractCoverage( CoverageOffering coverageOffering, Envelope env, Coverage[] sources ) {
        this.coverageOffering = coverageOffering;
        this.sources = sources;
        if ( sources != null ) {
            numSources = sources.length;
        }
        // set coverage envelope
        Position min = env.getMin();
        Position max = env.getMax();
        PT_CoordinatePoint minCP = new PT_CoordinatePoint( min.getX(), min.getY() );
        PT_CoordinatePoint maxCP = new PT_CoordinatePoint( max.getX(), max.getY() );
        envelope = new PT_Envelope();
        envelope.minCP = minCP;
        envelope.maxCP = maxCP;

        if ( coverageOffering != null ) {
            // set coordinate system
            try {
                CodeList[] cl = coverageOffering.getSupportedCRSs().getNativeSRSs();
                String code = cl[cl.length - 1].getCodes()[0];
                crs = CRSFactory.create( code );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            buildDimensionNames();
        }
    }

    /**
     * 
     * @param coverageOffering
     * @param env
     * @param sources
     * @param crs
     */
    public AbstractCoverage( CoverageOffering coverageOffering, Envelope env, Coverage[] sources, CoordinateSystem crs ) {
        this.coverageOffering = coverageOffering;
        this.sources = sources;
        if ( sources != null ) {
            numSources = sources.length;
        }
        // set coverage envelope
        Position min = env.getMin();
        Position max = env.getMax();
        PT_CoordinatePoint minCP = new PT_CoordinatePoint( min.getX(), min.getY() );
        PT_CoordinatePoint maxCP = new PT_CoordinatePoint( max.getX(), max.getY() );
        envelope = new PT_Envelope();
        envelope.minCP = minCP;
        envelope.maxCP = maxCP;

        this.crs = crs;
        if ( coverageOffering != null ) {
            buildDimensionNames();
        }
    }

    /**
     * private method to build the dimension names from the coverage axises (x, y, [z]) and the
     * available ranges
     */
    private void buildDimensionNames() {
        AxisDescription[] axisDescription = coverageOffering.getRangeSet().getAxisDescription();
        dimensionNames = new String[axisDescription.length + envelope.maxCP.ord.length];
        int k = 0;
        dimensionNames[k++] = "X";
        dimensionNames[k++] = "Y";
        if ( envelope.maxCP.ord.length == 3 ) {
            dimensionNames[k++] = "Z";
        }
        for ( int i = 0; i < axisDescription.length; i++ ) {
            dimensionNames[k++] = axisDescription[i].getName();
        }
    }

    /**
     * Specifies the coordinate reference system used when accessing a coverage or grid coverage
     * with the <code>evaluate(...)</code> methods. It is also the coordinate reference system of
     * the coordinates used with the math transform gridToCoordinateSystem}).
     * 
     * This coordinate reference system is usually different than coordinate system of the grid.
     * Grid coverage can be accessed (re-projected) with new coordinate reference system with the
     * {@link "org.opengis.coverage.processing.GridCoverageProcessor"} component. In this case, a
     * new instance of a grid coverage is created. <br>
     * <br>
     * Note: If a coverage does not have an associated coordinate reference system, the returned
     * value will be <code>null</code>. attribute should also be <code>null</code> if the
     * coordinate reference system is <code>null</code>.
     * 
     * @return The coordinate reference system used when accessing a coverage or grid coverage with
     *         the <code>evaluate(...)</code> methods, or <code>null</code>.
     */
    public CoordinateSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * The bounding box for the coverage domain in
     * {@linkplain #getCoordinateReferenceSystem coordinate reference system} coordinates. For grid
     * coverages, the grid cells are centered on each grid coordinate. The envelope for a 2-D grid
     * coverage includes the following corner positions.
     * 
     * <blockquote>
     * 
     * <pre>
     *   (Minimum row - 0.5, Minimum column - 0.5) for the minimum coordinates
     *   (Maximum row - 0.5, Maximum column - 0.5) for the maximum coordinates
     * </pre>
     * 
     * </blockquote>
     * 
     * If a grid coverage does not have any associated coordinate reference system, the minimum and
     * maximum coordinate points for the envelope will be empty sequences.
     * 
     * @return The bounding box for the coverage domain in coordinate system coordinates.
     */
    public PT_Envelope getEnvelope() {
        return envelope;
    }

    /**
     * The names of each dimension in the coverage. Typically these names are <var>x</var>, <var>y</var>,
     * <var>z</var> and <var>t</var>. The number of items in the sequence is the number of
     * dimensions in the coverage. Grid coverages are typically 2D (<var>x</var>, <var>y</var>)
     * while other coverages may be 3D (<var>x</var>, <var>y</var>, <var>z</var>) or 4D (<var>x</var>,
     * <var>y</var>, <var>z</var>, <var>t</var>). The number of dimensions of the coverage is the
     * number of entries in the list of dimension names.
     * 
     * @return The names of each dimension in the coverage.
     */
    public String[] getDimensionNames() {
        return dimensionNames;
    }

    /**
     * Retrieve sample dimension information for the coverage. For a grid coverage a sample
     * dimension is a band. The sample dimension information include such things as description,
     * data type of the value (bit, byte, integer...), the no data values, minimum and maximum
     * values and a color table if one is associated with the dimension. A coverage must have at
     * least one sample dimension.
     * 
     * @param index
     *            Index for sample dimension to retrieve. Indices are numbered 0 to (<var>{@linkplain #getNumSampleDimensions n}</var>-1).
     * @return Sample dimension information for the coverage.
     * @throws IndexOutOfBoundsException
     *             if <code>index</code> is out of bounds.
     */
    public SampleDimension getSampleDimension( int index )
                            throws IndexOutOfBoundsException {
        return null;
    }

    /**
     * Number of grid coverages which the grid coverage was derived from. This implementation
     * specification does not include interfaces for creating collections of coverages therefore
     * this value will usually be one indicating an adapted grid coverage, or zero indicating a raw
     * grid coverage.
     * 
     * @return The number of grid coverages which the grid coverage was derived from.
     */
    public int getNumSources() {
        return 0;
    }

    /**
     * Returns the source data for a coverage. This is intended to allow applications to establish
     * what <code>Coverage</code>s will be affected when others are updated, as well as to trace
     * back to the "raw data".
     * 
     * @param sourceDataIndex
     *            Source coverage index. Indexes start at 0.
     * @return The source data for a coverage.
     * @throws IndexOutOfBoundsException
     *             if <code>sourceDataIndex</code> is out of bounds.
     * 
     * @see #getNumSources
     * @see "org.opengis.coverage.grid.GridCoverage#getSource"
     */
    public Coverage getSource( int sourceDataIndex )
                            throws IndexOutOfBoundsException {
        if ( sources != null && sources.length >= sourceDataIndex - 1 ) {
            return sources[sourceDataIndex];
        }
        return null;
    }

    /**
     * List of metadata keywords for a coverage. If no metadata is available, the sequence will be
     * empty.
     * 
     * @return the list of metadata keywords for a coverage.
     * 
     * @see #getMetadataValue
     * @see javax.media.jai.PropertySource#getPropertyNames()
     */
    public String[] getMetadataNames() {
        String[] keyw = new String[0];
        Keywords[] keywords = coverageOffering.getKeywords();
        if ( keywords != null ) {
            List<String> list = new ArrayList<String>( 100 );
            for ( int i = 0; i < keywords.length; i++ ) {
                String[] kw = keywords[i].getKeywords();
                for ( int k = 0; k < kw.length; k++ ) {
                    list.add( kw[k] );
                }
            }
            keyw = list.toArray( new String[list.size()] );
        }
        return keyw;
    }

    /**
     * Retrieve the metadata value for a given metadata name.
     * 
     * @param name
     *            Metadata keyword for which to retrieve data.
     * @return the metadata value for a given metadata name.
     * @throws MetadataNameNotFoundException
     *             if there is no value for the specified metadata name.
     * 
     * @see #getMetadataNames
     * @see javax.media.jai.PropertySource#getProperty
     */
    public String getMetadataValue( String name )
                            throws MetadataNameNotFoundException {
        return null;
    }

    /**
     * Returns 2D view of this coverage as a renderable image. This optional operation allows
     * interoperability with <A HREF="http://java.sun.com/products/java-media/2D/">Java2D</A>. If
     * this coverage is a {@link "org.opengis.coverage.grid.GridCoverage"} backed by a
     * {@link java.awt.image.RenderedImage}, the underlying image can be obtained with:
     * 
     * <code>getRenderableImage(0,1).{@linkplain RenderableImage#createDefaultRendering()
     * createDefaultRendering()}</code>
     * 
     * @param xAxis
     *            Dimension to use for the <var>x</var> axis.
     * @param yAxis
     *            Dimension to use for the <var>y</var> axis.
     * @return A 2D view of this coverage as a renderable image.
     * @throws UnsupportedOperationException
     *             if this optional operation is not supported.
     * @throws IndexOutOfBoundsException
     *             if <code>xAxis</code> or <code>yAxis</code> is out of bounds.
     */
    public abstract RenderableImage getRenderableImage( int xAxis, int yAxis )
                            throws UnsupportedOperationException, IndexOutOfBoundsException;

    /**
     * returns the {@link CoverageOffering} describing a coverage
     * 
     * @see CoverageOffering
     * @return the CoverageOffering describing a coverage
     */
    public CoverageOffering getCoverageOffering() {
        return coverageOffering;
    }

}
