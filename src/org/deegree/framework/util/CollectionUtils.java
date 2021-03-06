//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/CollectionUtils.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2007 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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

package org.deegree.framework.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <code>CollectionUtils</code> contains some functionality missing in <code>Arrays</code> and <code>Collections</code>.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 13557 $, $Date: 2008-08-11 12:00:51 +0200 (Mo, 11. Aug 2008) $
 */
public class CollectionUtils {

    /**
     * @param <T>
     * @param col
     *            may not contain null values
     * @param sep
     *            the separating string
     * @return a comma separated list of #toString values
     */
    public static <T> String collectionToString( Collection<T> col, String sep ) {
        StringBuilder sb = new StringBuilder( 512 );

        Iterator<T> iter = col.iterator();

        while ( iter.hasNext() ) {
            sb.append( iter.next() );
            if ( iter.hasNext() ) {
                sb.append( sep );
            }
        }

        return sb.toString();
    }

    /**
     * Wraps a for loop and the creation of a new list.
     * 
     * @param <T>
     * @param <U>
     * @param col
     * @param mapper
     * @return a list where the mapper has been applied to each element in the map
     */
    public static <T, U> LinkedList<T> map( U[] col, Mapper<T, U> mapper ) {
        LinkedList<T> list = new LinkedList<T>();

        for ( U u : col ) {
            list.add( mapper.apply( u ) );
        }

        return list;
    }

    /**
     * Wraps a for loop and the creation of a new list.
     * 
     * @param <T>
     * @param <U>
     * @param col
     * @param mapper
     * @return a list where the mapper has been applied to each element in the map
     */
    public static <T, U> LinkedList<T> map( Collection<U> col, Mapper<T, U> mapper ) {
        LinkedList<T> list = new LinkedList<T>();

        for ( U u : col ) {
            list.add( mapper.apply( u ) );
        }

        return list;
    }

    /**
     * @param <T>
     * @param array
     * @param obj
     * @return true, if the object is contained within the array
     */
    public static <T> boolean contains( T[] array, T obj ) {
        for ( T t : array ) {
            if ( obj == t ) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param <T>
     * @param col
     * @param obj
     * @return true, if the object is contained within the collection
     */
    public static <T> boolean contains( Collection<T> col, T obj ) {
        for ( T t : col ) {
            if ( obj == t ) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param <T>
     * @param array
     * @param pred
     * @return the first object for which the predicate is true, or null
     */
    public static <T> T find( T[] array, Predicate<T> pred ) {
        for ( T t : array ) {
            if ( pred.eval( t ) ) {
                return t;
            }
        }

        return null;
    }

    /**
     * @param <T>
     * @param col
     * @param pred
     * @return the first object for which the predicate is true, or null
     */
    public static <T> T find( Collection<T> col, Predicate<T> pred ) {
        for ( T t : col ) {
            if ( pred.eval( t ) ) {
                return t;
            }
        }

        return null;
    }

    /**
     * @param <T>
     * @param col
     * @param pred
     * @return only those T, for which the pred is true
     */
    public static <T> LinkedList<T> filter( Collection<T> col, Predicate<T> pred ) {

        LinkedList<T> list = new LinkedList<T>();
        for ( T t : col ) {
            if ( pred.eval( t ) ) {
                list.add( t );
            }
        }

        return list;
    }

    /**
     * @param <T>
     * @param identity
     * @param col
     * @param folder
     * @return the folded value
     */
    public static <T> T fold( T identity, Collection<T> col, Folder<T> folder ) {
        if ( col.isEmpty() ) {
            return identity;
        }

        Iterator<T> i = col.iterator();

        T acc = i.next();

        while ( i.hasNext() ) {
            acc = folder.fold( acc, i.next() );
        }

        return acc;
    }

    /**
     * <code>Predicate</code> defines a boolean predicate function interface.
     * 
     * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
     * @author last edited by: $Author: aschmitz $
     * 
     * @version $Revision: 13557 $, $Date: 2008-08-11 12:00:51 +0200 (Mo, 11. Aug 2008) $
     * @param <T>
     *            the type of the predicate function's argument
     */
    public static interface Predicate<T> {
        /**
         * @param t
         * @return true, if the predicate is satisfied
         */
        public boolean eval( T t );
    }

    /**
     * <code>Mapper</code> gives a name to a simple function.
     * 
     * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
     * @author last edited by: $Author: aschmitz $
     * 
     * @version $Revision: 13557 $, $Date: 2008-08-11 12:00:51 +0200 (Mo, 11. Aug 2008) $
     * @param <T>
     *            the return type of the function
     * @param <U>
     *            the argument type of the function
     */
    public static interface Mapper<T, U> {
        /**
         * @param u
         * @return an implementation defined value
         */
        public T apply( U u );
    }

    /**
     * <code>Folder</code>
     * 
     * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
     * @author last edited by: $Author: aschmitz $
     * 
     * @version $Revision: 13557 $, $Date: 2008-08-11 12:00:51 +0200 (Mo, 11. Aug 2008) $
     * @param <T>
     */
    public static interface Folder<T> {
        /**
         * @param t1
         * @param t2
         * @return the folded value
         */
        public T fold( T t1, T t2 );
    }

    /**
     * 
     */
    public static final Mapper<String, Object> TOSTRINGS = new Mapper<String, Object>() {
        public String apply( Object u ) {
            return u.toString();
        }
    };

}
