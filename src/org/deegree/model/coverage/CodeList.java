//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/coverage/CodeList.java $
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

// J2SE direct dependencies
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Base class for all code lists.
 * 
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 */
public abstract class CodeList implements Serializable {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 5655809691319522885L;

    /**
     * The code value. For J2SE 1.3 profile only.
     */
    private transient final int ordinal;

    /**
     * The code name. For J2SE 1.3 profile only.
     */
    private final String name;

    /**
     * Create a new code list instance.
     * 
     * @param name
     *            The code name.
     * @param ordinal
     *            The code value.
     */
    protected CodeList( final String name, final int ordinal ) {
        this.name = name;
        this.ordinal = ordinal;
    }

    /**
     * Create a new code list instance and add it to the given collection.
     * 
     * @param name
     *            The code name.
     * @param values
     *            The collection to add the enum to.
     */
    CodeList( final String name, final Collection<CodeList> values ) {
        this.name = name;
        synchronized ( values ) {
            this.ordinal = values.size();
            if ( !values.add( this ) ) {
                throw new IllegalArgumentException( String.valueOf( values ) );
            }
        }
    }

    /**
     * Returns the ordinal of this enumeration constant (its position in its enum declaration, where
     * the initial constant is assigned an ordinal of zero).
     * 
     * @return the ordinal of this enumeration constant.
     */
    public final int ordinal() {
        return ordinal;
    }

    /**
     * Returns the name of this enum constant.
     * 
     * @return the name of this enum constant.
     */
    public final String name() {
        return name;
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     * 
     * @return the list of enumerations of the same kind than this enum.
     */
    public abstract CodeList[] family();

    /**
     * Returns a string representation of this code list.
     */
    @Override
    public String toString() {
        String classname = getClass().getName();
        final int i = classname.lastIndexOf( '.' );
        if ( i >= 0 ) {
            classname = classname.substring( i + 1 );
        }
        return classname + '[' + name + ']';
    }

    /**
     * Resolve the code list to an unique instance after deserialization. The instance is resolved
     * using its {@linkplain #name() name} only (not its {@linkplain #ordinal() ordinal}).
     * 
     * @return This code list as a unique instance.
     * @throws ObjectStreamException
     *             if the deserialization failed.
     */
    protected Object readResolve()
                            throws ObjectStreamException {
        final CodeList[] codes = family();
        for ( int i = 0; i < codes.length; i++ ) {
            if ( name.equals( codes[i].name ) ) {
                return codes[i];
            }
        }
        throw new InvalidObjectException( toString() );
    }
}
