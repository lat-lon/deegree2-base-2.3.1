//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/coverage/grid/GridCoverageWriter.java $
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

import org.deegree.datatypes.parameter.GeneralParameterValueIm;
import org.deegree.datatypes.parameter.InvalidParameterNameException;
import org.deegree.datatypes.parameter.InvalidParameterValueException;
import org.deegree.datatypes.parameter.ParameterNotFoundException;

/**
 * Support for writing grid coverages into a persistent store. Instance of
 * <code>GridCoverageWriter</code> are obtained through a call to
 * {@link GridCoverageExchange#getWriter}. Grid coverages are usually added to the output stream in
 * a sequential order.
 * 
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 12140 $, $Date: 2008-06-04 10:54:18 +0200 (Mi, 04. Jun 2008) $
 * 
 * @see GridCoverageExchange#getWriter
 * @see javax.imageio.ImageWriter
 */
public interface GridCoverageWriter {
    /**
     * Returns the format handled by this <code>GridCoverageWriter</code>.
     * 
     * @return the format handled by this <code>GridCoverageWriter</code>.
     */
    Format getFormat();

    /**
     * Returns the output destination. This is the object passed to the
     * {@link GridCoverageExchange#getWriter} method. It can be a {@link java.lang.String}, an
     * {@link java.io.OutputStream}, a {@link java.nio.channels.FileChannel}, etc.
     * 
     * @return the output destination
     */
    Object getDestination();

    /**
     * Returns the list of metadata keywords associated with the {@linkplain #getDestination output
     * destination} as a whole (not associated with any particular grid coverage). If no metadata is
     * allowed, the array will be empty.
     * 
     * @return The list of metadata keywords for the output destination.
     * 
     * @revisit This javadoc may not apply thats well in the iterator scheme.
     */
    String[] getMetadataNames();

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
    Object getMetadataValue( String name )
                            throws IOException, MetadataNameNotFoundException;

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
    void setMetadataValue( String name, String value )
                            throws IOException, MetadataNameNotFoundException;

    /**
     * Retrieve the list of grid coverages contained within the {@linkplain #getDestination() input
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
    String[] listSubNames()
                            throws IOException;

    /**
     * Returns the name for the next grid coverage to be
     * {@linkplain #write(GridCoverage, GeneralParameterValueIm[]) write} to the
     * {@linkplain #getDestination() output destination}.
     * 
     * @return the name for the next grid coverage to be
     * 
     * @throws IOException
     *             if an error occurs during reading.
     * @revisit Do we need a special method for that, or should it be a metadata?
     */
    String getCurrentSubname()
                            throws IOException;

    /**
     * Set the name for the next grid coverage to
     * {@linkplain #write(GridCoverage, GeneralParameterValueIm[]) write} within the
     * {@linkplain #getDestination output destination}. The subname can been fetch later at reading
     * time.
     * 
     * @param name
     * 
     * @throws IOException
     *             if an error occurs during writing.
     * @revisit Do we need a special method for that, or should it be a metadata?
     */
    void setCurrentSubname( String name )
                            throws IOException;

    /**
     * Writes the specified grid coverage.
     * 
     * @param coverage
     *            The {@linkplain GridCoverage grid coverage} to write.
     * @param parameters
     *            An optional set of parameters. Should be any or all of the parameters returned by
     *            {@link Format#getWriteParameters}.
     * @throws InvalidParameterNameException
     *             if a parameter in <code>parameters</code> doesn't have a recognized name.
     * @throws InvalidParameterValueException
     *             if a parameter in <code>parameters</code> doesn't have a valid value.
     * @throws ParameterNotFoundException
     *             if a parameter was required for the operation but was not provided in the
     *             <code>parameters</code> list.
     * @throws IOException
     *             if the export failed for some other input/output reason, including
     *             {@link javax.imageio.IIOException} if an error was thrown by the underlying image
     *             library.
     */
    void write( GridCoverage coverage, GeneralParameterValueIm[] parameters )
                            throws InvalidParameterNameException, InvalidParameterValueException,
                            ParameterNotFoundException, IOException;

    /**
     * Allows any resources held by this object to be released. The result of calling any other
     * method subsequent to a call to this method is undefined. It is important for applications to
     * call this method when they know they will no longer be using this
     * <code>GridCoverageWriter</code>. Otherwise, the writer may continue to hold on to
     * resources indefinitely.
     * 
     * @throws IOException
     *             if an error occured while disposing resources (for example while flushing data
     *             and closing a file).
     */
    void dispose()
                            throws IOException;
}
