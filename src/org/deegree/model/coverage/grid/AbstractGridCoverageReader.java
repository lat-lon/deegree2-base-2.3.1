// $HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/coverage/grid/AbstractGridCoverageReader.java $
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.ogcwebservices.LonLatEnvelope;
import org.deegree.ogcwebservices.wcs.describecoverage.CoverageOffering;

/**
 * @version $Revision: 9437 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 9437 $, $Date: 2008-01-08 11:50:52 +0100 (Di, 08. Jan 2008) $
 */

public abstract class AbstractGridCoverageReader implements GridCoverageReader {

    protected CoverageOffering description = null;

    protected Object source = null;

    protected Envelope envelope = null;

    private Map<String, String> metadata = new HashMap<String, String>();

    private String[] subNames = null;

    private String currentSubname = null;

    protected Format format = null;

    /**
     * @param source
     * @param description
     * @param envelope
     * @param format
     */
    public AbstractGridCoverageReader( Object source, CoverageOffering description, Envelope envelope, Format format ) {
        this.description = description;
        this.source = source;
        this.envelope = envelope;
        this.format = format;
    }

    /**
     * Returns the input source. This is the object passed to the
     * {@link "org.opengis.coverage.grid.GridCoverageExchange#getReader(Object)"} method. It can be
     * a {@link java.lang.String}, an {@link java.io.InputStream}, a
     * {@link java.nio.channels.FileChannel}, whatever.
     * 
     * @return the input source.
     */
    public Object getSource() {
        return source;
    }

    /**
     * Returns the list of metadata keywords associated with the {@linkplain #getSource input
     * source} as a whole (not associated with any particular grid coverage). If no metadata is
     * available, the array will be empty.
     * 
     * @return The list of metadata keywords for the input source.
     * @throws IOException
     *             if an error occurs during reading.
     * 
     * @revisit This javadoc may not apply thats well in the iterator scheme.
     */
    public String[] getMetadataNames()
                            throws IOException {
        return metadata.keySet().toArray( new String[metadata.size()] );
    }

    /**
     * Retrieve the metadata value for a given metadata name.
     * 
     * @param name
     *            Metadata keyword for which to retrieve metadata.
     * @return The metadata value for the given metadata name. Should be one of the name returned by
     *         {@link #getMetadataNames}.
     * @throws IOException
     *             if an error occurs during reading.
     * @throws MetadataNameNotFoundException
     *             if there is no value for the specified metadata name.
     * 
     * @revisit This javadoc may not apply thats well in the iterator scheme.
     */
    public String getMetadataValue( String name )
                            throws IOException, MetadataNameNotFoundException {
        return metadata.get( name );
    }

    /**
     * Sets the metadata value for a given metadata name.
     * 
     * @param name
     *            Metadata keyword for which to set the metadata.
     * @param value
     *            The metadata value for the given metadata name.
     * @throws IOException
     *             if an error occurs during writing.
     * @throws MetadataNameNotFoundException
     *             if the specified metadata name is not handled for this format.
     * 
     * @revisit This javadoc may not apply thats well in the iterator scheme.
     */
    public void setMetadataValue( String name, String value )
                            throws IOException, MetadataNameNotFoundException {
        metadata.put( name, value );
    }

    /**
     * Set the name for the next grid coverage to GridCoverageWriter#write within the{@linkplain #getSource() input}.
     * The subname can been fetch later at reading time.
     * 
     * @param name
     * 
     * @throws IOException
     *             if an error occurs during writing.
     * @revisit Do we need a special method for that, or should it be a metadata?
     * 
     */
    public void setCurrentSubname( String name )
                            throws IOException {
        currentSubname = name;
    }

    /**
     * Retrieve the list of grid coverages contained within the {@linkplain #getSource input
     * source}. Each grid can have a different coordinate system, number of dimensions and grid
     * geometry. For example, a HDF-EOS file (GRID.HDF) contains 6 grid coverages each having a
     * different projection. An empty array will be returned if no sub names exist.
     * 
     * @return The list of grid coverages contained within the input source.
     * @throws IOException
     *             if an error occurs during reading.
     * 
     * @revisit The javadoc should also be more explicit about hierarchical format. Should the names
     *          be returned as paths? Explain what to return if the GridCoverage are accessible by
     *          index only. A proposal is to name them "grid1", "grid2", etc.
     */
    public String[] listSubNames()
                            throws IOException {
        return subNames;
    }

    /**
     * Returns the name for the next grid coverage to be read from the
     * {@linkplain #getSource input source}.
     * 
     * @return the name for the next grid coverage to be read from the input source.
     * 
     * @throws IOException
     *             if an error occurs during reading.
     * @revisit Do we need a special method for that, or should it be a metadata?
     * 
     */
    public String getCurrentSubname()
                            throws IOException {
        return currentSubname;
    }

    /**
     * Returns the format handled by this <code>GridCoverageReader</code>.
     * 
     * @return the format handled by this <code>GridCoverageReader</code>.
     * 
     */
    public Format getFormat() {
        return format;
    }

    /**
     * transforms the passed <tt>Envelope</tt> to a <tt>LonLatEnvelope</tt> If the passed source
     * CRS isn't equal to "EPSG:4326" the <tt>Envelope</tt> will be transformed to "EPSG:4326"
     * first.
     * 
     * @param env
     * @param sourceCRS
     * @return LatLonEnvelope in "EPSG:4326"
     */
    protected LonLatEnvelope calcLonLatEnvelope( Envelope env, String sourceCRS ) {
        LonLatEnvelope lle = null;
        if ( sourceCRS.equalsIgnoreCase( "EPSG:4326" ) ) {
            lle = new LonLatEnvelope( env );
        } else {
            try {
                GeoTransformer tr = new GeoTransformer( "EPSG:4326" );
                env = tr.transform( env, sourceCRS );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            lle = new LonLatEnvelope( env );
        }
        return lle;
    }

}
