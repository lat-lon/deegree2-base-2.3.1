//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/filterencoding/Function.java $
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
package org.deegree.model.filterencoding;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.deegree.framework.util.BootLogger;
import org.deegree.framework.xml.ElementList;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.model.feature.Feature;
import org.w3c.dom.Element;

/**
 * Encapsulates the information of a <code>Function</code>element as defined in the Expression
 * DTD.
 * 
 * @author Markus Schneider
 * @version 07.08.2002
 */
public abstract class Function extends Expression {

    /** The Function's name (as specified in it's name attribute). */
    protected String name;

    /** The Function's arguments. */
    protected List<Expression> args;

    private static Map<String, Class> functions;

    static {
        if ( Function.functions == null ) {
            Function.initialize();
        }
    }

    private static void initialize() {
        Function.functions = new HashMap<String, Class>( 50 );
        InputStream is = Function.class.getResourceAsStream( "function.properties" );
        Properties props = new Properties();
        try {
            props.load( is );
        } catch ( IOException e ) {
            BootLogger.logError( e.getMessage(), e );
        }
        Iterator iter = props.keySet().iterator();
        while ( iter.hasNext() ) {
            String key = (String) iter.next();
            try {
                String className = props.getProperty( key );
                if ( "org.deegree.model.filterencoding.DBFunction".equals( className ) ) {
                    throw new Exception( Messages.getMessage( "FILTER_INVALID_NAME" ) );
                }
                Function.functions.put( key, Class.forName( className ) );
            } catch ( Exception e ) {
                BootLogger.logError( e.getMessage(), e );
                break;
            }
        }
    }

    protected Function() {

    }

    /** Constructs a new Function. */
    public Function( String name, List<Expression> args ) {
        this();
        this.id = ExpressionDefines.FUNCTION;
        this.name = name;
        this.args = args;
    }

    /**
     * 
     * @param args
     */
    public void setArguments( List<Expression> args ) {
        this.args = args;
    }

    /**
     * Given a DOM-fragment, a corresponding Expression-object is built. This method recursively
     * calls other buildFromDOM () - methods to validate the structure of the DOM-fragment.
     * 
     * @throws FilterConstructionException
     *             if the structure of the DOM-fragment is invalid
     */
    public static Expression buildFromDOM( Element element )
                            throws FilterConstructionException {

        // check if root element's name equals 'Function'
        if ( !element.getLocalName().toLowerCase().equals( "function" ) ) {
            throw new FilterConstructionException( Messages.getMessage( "FILTER_WRONG_ROOTELEMENT" ) );
        }

        // determine the name of the Function
        String name = element.getAttribute( "name" );
        if ( name == null ) {
            throw new FilterConstructionException( Messages.getMessage( "FILTER_MISSING_NAME" ) );
        }

        // determine the arguments of the Function
        ElementList children = XMLTools.getChildElements( element );
        if ( children.getLength() < 1 ) {
            throw new FilterConstructionException( Messages.getMessage( "FILTER_MISSING_ELEMENT", name ) );
        }

        ArrayList<Expression> args = new ArrayList<Expression>( children.getLength() );
        for ( int i = 0; i < children.getLength(); i++ ) {
            args.add( Expression.buildFromDOM( children.item( i ) ) );
        }

        Class function = Function.functions.get( name );
        if ( function == null ) {
            throw new FilterConstructionException( Messages.getMessage( "FILTER_UNKNOWN_FUNCTION", name ) );
        }
        Function func;
        try {
            func = (Function) function.newInstance();
        } catch ( InstantiationException e ) {
            throw new FilterConstructionException( e.getMessage() );
        } catch ( IllegalAccessException e ) {
            throw new FilterConstructionException( e.getMessage() );
        }
        func.setName( name );
        func.setArguments( args );

        return func;
    }

    /**
     * Returns the Function's name.
     * 
     * @return functions name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @see org.deegree.model.filterencoding.Function#getName()
     * 
     * @param name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * returns the arguments of the function
     * 
     * @return arguments of the function
     */
    public List<Expression> getArguments() {
        return this.args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.model.filterencoding.Expression#getExpressionId()
     */
    @Override
    public int getExpressionId() {
        return ExpressionDefines.FUNCTION;
    }

    /**
     * Returns the <tt>Function</tt>'s value (to be used in the evaluation of a complexer
     * <tt>Expression</tt>).
     * 
     * @param feature
     *            that determines the concrete values of <tt>PropertyNames</tt> found in the
     *            expression
     * @return the resulting value
     */
    @Override
    public abstract Object evaluate( Feature feature )
                            throws FilterEvaluationException;

    /**
     * Produces an indented XML representation of this object.
     * 
     * @return xml representation
     */
    @Override
    public StringBuffer toXML() {
        StringBuffer sb = new StringBuffer( 1000 );
        sb.append( "<ogc:Function name=\"" ).append( this.name ).append( "\">" );
        for ( int i = 0; i < this.args.size(); i++ ) {
            sb.append( this.args.get( i ).toXML() );
        }
        sb.append( "</ogc:Function>" );
        return sb;
    }
}