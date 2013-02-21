//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/idgenerator/UUIDGenerator.java $
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
package org.deegree.io.datastore.idgenerator;

import java.util.Properties;
import java.util.UUID;

import org.deegree.datatypes.Types;
import org.deegree.io.datastore.DatastoreTransaction;
import org.deegree.io.datastore.FeatureId;
import org.deegree.io.datastore.schema.MappedFeatureType;
import org.deegree.io.datastore.schema.MappedGMLId;

/**
 * Primary key and {@link FeatureId} generator that is based on UUIDs.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Do, 27. Dez 2007) $
 */
public class UUIDGenerator extends IdGenerator {

    private long mac;

    private boolean macSet;

    /**
     * Creates a new <code>UUIDGenerator</code> instance.
     * <p>
     * Supported configuration parameters: <table>
     * <tr>
     * <th>Name</th>
     * <th>optional?</th>
     * <th>Usage</th>
     * </tr>
     * <tr>
     * <td>macAddress</td>
     * <td>yes</td>
     * <td>specify MAC address component of UUID</td>
     * </tr>
     * </table>
     * 
     * @param params
     *            configuration parameters
     * @throws IdGenerationException
     */
    public UUIDGenerator( Properties params ) throws IdGenerationException {
        super( params );
        String macAddress = params.getProperty( "macAddress" );
        if ( macAddress != null ) {
            long decoded;
            try {
                decoded = Long.decode( macAddress );
            } catch ( NumberFormatException e ) {
                throw new IdGenerationException( "Value for parameter 'macAddress': '" + macAddress
                                                 + "' can not be decoded as Long." );
            }
            mac = decoded;
            macSet = true;
        }
    }

    /**
     * Returns a new primary key.
     * 
     * @param ta
     *            datastore transaction (context)
     * @return a new primary key.
     * @throws IdGenerationException
     *             if the generation of the id could not be performed
     */
    @Override
    public String getNewId( DatastoreTransaction ta )
                            throws IdGenerationException {
        return "UUID_" + ( macSet ? new UUID( mac, (long) ( Math.random() * Long.MAX_VALUE ) ) : UUID.randomUUID() );
    }

    /**
     * Returns a new id for a feature of the given type.
     * 
     * @param ft
     *            (mapped) feature type (irrelevant for this generator)
     * @param ta
     *            datastore transaction (context)
     * @return a new feature id.
     * @throws IdGenerationException
     */
    @Override
    public FeatureId getNewId( MappedFeatureType ft, DatastoreTransaction ta )
                            throws IdGenerationException {

        FeatureId fid = null;

        MappedGMLId fidDefinition = ft.getGMLId();
        if ( fidDefinition.getKeySize() != 1 ) {
            throw new IdGenerationException( "Cannot generate feature ids that are mapped to "
                                             + fidDefinition.getKeySize() + " columns." );
        }
        if ( fidDefinition.getIdFields()[0].getType() == Types.VARCHAR
             || fidDefinition.getIdFields()[0].getType() == Types.CHAR ) {
            String uuidString = "UUID_"
                                + ( macSet ? new UUID( mac, (long) ( Math.random() * Long.MAX_VALUE ) )
                                          : UUID.randomUUID() );
            fid = new FeatureId( ft, new Object[] { uuidString } );
        } else {
            // TODO add handling of NUMERIC columns (>= 128 bit)
            throw new IdGenerationException( "UUIDGenerator may currently only be used for VARCHAR fields." );
        }
        return fid;
    }
}
