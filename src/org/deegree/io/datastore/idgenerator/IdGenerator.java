//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/idgenerator/IdGenerator.java $
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.deegree.io.datastore.DatastoreTransaction;
import org.deegree.io.datastore.FeatureId;
import org.deegree.io.datastore.schema.MappedFeatureType;

/**
 * Abstract base class for generators that are used to create primary keys (especially
 * {@link FeatureId}s).
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Thu, 27 Dec 2007) $
 */
public abstract class IdGenerator {

    /** Default generator type based on UUIDs. */
    public static String TYPE_UUID = "UUID";

    private static final String BUNDLE_NAME = "org.deegree.io.datastore.idgenerator.idgenerator";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

    protected Properties params;

    protected MappedFeatureType ft;

    /**
     * Creates a new <code>IdGenerator</code> instance.
     * 
     * @param params
     *            configuration parameters
     */
    protected IdGenerator( Properties params ) {
        this.params = params;
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
    public abstract Object getNewId( DatastoreTransaction ta )
                            throws IdGenerationException;

    /**
     * Returns a new id for a feature of the given type.
     * 
     * @param ft
     *            feature type
     * @param ta
     *            datastore transaction (context)
     * @return a new feature id.
     * @throws IdGenerationException
     */
    public abstract FeatureId getNewId( MappedFeatureType ft, DatastoreTransaction ta )
                            throws IdGenerationException;

    /**
     * Returns a concrete <code>IdGenerator</code> instance which is identified by the given type
     * code.
     * 
     * @param type
     *            type code
     * @param params
     *            initialization parameters for the IdGenerator
     * @return concrete IdGenerator instance
     */
    public static final IdGenerator getInstance( String type, Properties params ) {
        IdGenerator generator = null;
        String className = null;
        try {
            className = RESOURCE_BUNDLE.getString( type );
            Class idGeneratorClass = Class.forName( className );

            // get constructor
            Class[] parameterTypes = new Class[] { Properties.class };
            Constructor constructor = idGeneratorClass.getConstructor( parameterTypes );

            // call constructor
            Object arglist[] = new Object[] { params };
            generator = (IdGenerator) constructor.newInstance( arglist );
        } catch ( InvocationTargetException e ) {
            String msg = "Could not instantiate IdGenerator with type '" + type + "': "
                         + e.getTargetException().getMessage();
            throw new RuntimeException( msg );
        } catch ( ClassNotFoundException e ) {
            String msg = "IdGenerator class '" + className + "' not found: " + e.getMessage();
            throw new RuntimeException( msg );
        } catch ( Exception e ) {
            String msg = "Could not instantiate IdGenerator with type '" + type + "': "
                         + e.getMessage();
            throw new RuntimeException( msg );
        }
        return generator;
    }
}
