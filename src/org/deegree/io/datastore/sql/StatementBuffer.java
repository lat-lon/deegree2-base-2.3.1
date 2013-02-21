//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/sql/StatementBuffer.java $
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
package org.deegree.io.datastore.sql;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class for the creation and logging of {@link PreparedStatement}s.
 * <p>
 * It allows to concatenate the query step by step and holds the arguments of the query as well.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 12032 $, $Date: 2008-05-29 18:27:00 +0200 (Do, 29. Mai 2008) $
 */
public class StatementBuffer {

    // contains the SQL-query string
    private StringBuffer queryBuffer = new StringBuffer( 500 );

    // contains the arguments of the query
    private List<StatementArgument> argumentList = new ArrayList<StatementArgument>();

    /**
     * Appends the given character to the statement.
     * 
     * @param c
     */
    public void append( char c ) {
        this.queryBuffer.append( c );
    }

    /**
     * Appends the given string to the statement.
     * 
     * @param s
     */
    public void append( String s ) {
        this.queryBuffer.append( s );
    }

    /**
     * Appends the given {@link StringBuffer} to the statement.
     * 
     * @param sb
     */
    public void append( StringBuffer sb ) {
        this.queryBuffer.append( sb );
    }

    /**
     * Appends the given argument (as the replacement value for the '?' character in the query) to the statement.
     * 
     * @param o
     * @param typeCode
     */
    public void addArgument( Object o, int typeCode ) {
        StatementArgument argument = new StatementArgument( o, typeCode );
        this.argumentList.add( argument );
    }

    /**
     * Returns the query string (without the arguments' values).
     * 
     * @return the query string (without the arguments' values)
     */
    public String getQueryString() {
        return this.queryBuffer.toString();
    }

    /**
     * Returns an {@link Iterator} over the arguments of the query.
     * 
     * @return an Iterator over the arguments of the query
     */
    public Iterator<StatementArgument> getArgumentsIterator() {
        return this.argumentList.iterator();
    }

    @Override
    public String toString() {
        return queryBuffer.toString();
    }

    /**
     * Encapsulates an argument value and the SQL type code for the target column.
     * 
     * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
     * @author last edited by: $Author: mschneider $
     * 
     * @version $Revision: 12032 $, $Date: 2008-05-29 18:27:00 +0200 (Do, 29. Mai 2008) $
     */
    public class StatementArgument {

        private Object argumentValue;

        private int typeCode;

        /**
         * Creates a new instance of {@link StatementArgument}.
         * 
         * @param argumentValue
         *            argument for a statement
         * @param typeCode
         *            SQL type code of the target column
         */
        StatementArgument( Object argumentValue, int typeCode ) {
            this.argumentValue = argumentValue;
            this.typeCode = typeCode;
        }

        /**
         * Returns the argument value.
         * 
         * @return the argument value
         */
        public Object getArgument() {
            return this.argumentValue;
        }

        /**
         * Returns the SQL type code for the column that is targeted by the argument.
         * 
         * @return the SQL type code for the column that is targeted by the argument
         */
        public int getTypeCode() {
            return this.typeCode;
        }
    }
}