// $HeadURL:
// /cvsroot/deegree/src/org/deegree/ogcwebservices/getcapabilities/Contents.java,v
// 1.1 2004/06/23 11:55:40 mschneider Exp $
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
package org.deegree.model.filterencoding.capabilities;

import java.util.HashMap;
import java.util.Map;

/**
 * ScalarCapabilitiesBean
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 13704 $, $Date: 2008-08-26 11:02:40 +0200 (Di, 26. Aug 2008) $
 */
public class ScalarCapabilities {

    private boolean supportsLogicalOperators;

    /**
     * keys are Strings (operator names), values are Operator-instances
     */
    private Map<String, Operator> comparisonOperators;

    /**
     * keys are Strings (operator names), values are Operator-instances
     */
    private Map<String, Operator> arithmeticOperators;

    /**
     * Creates a new <code>ScalarCapabilities</code> instance.
     * 
     * @param supportsLogicalOperators
     * @param comparisonOperators
     *            may be null
     * @param arithmeticOperators
     *            may be null
     */
    public ScalarCapabilities( boolean supportsLogicalOperators, Operator[] comparisonOperators,
                               Operator[] arithmeticOperators ) {
        this.supportsLogicalOperators = supportsLogicalOperators;
        setComparisonOperators( comparisonOperators );
        setArithmeticOperators( arithmeticOperators );
    }

    /**
     * 
     * @return true, if it supports them
     */
    public boolean hasLogicalOperatorsSupport() {
        return supportsLogicalOperators;
    }

    /**
     * 
     * @param supportsLogicalOperators
     */
    public void setLogicalOperatorsSupport( boolean supportsLogicalOperators ) {
        this.supportsLogicalOperators = supportsLogicalOperators;
    }

    /**
     * 
     * @param comparisonOperators
     */
    public void setComparisonOperators( Operator[] comparisonOperators ) {
        this.comparisonOperators = new HashMap<String, Operator>();
        if ( comparisonOperators != null ) {
            for ( int i = 0; i < comparisonOperators.length; i++ ) {
                this.comparisonOperators.put( comparisonOperators[i].getName(), comparisonOperators[i] );
            }
        }
    }

    /**
     * 
     * @return Comparison Operators
     */
    public Operator[] getComparisonOperators() {
        return comparisonOperators.values().toArray( new Operator[comparisonOperators.values().size()] );
    }

    /**
     * 
     * @param arithmeticOperators
     *            may be null
     */
    public void setArithmeticOperators( Operator[] arithmeticOperators ) {
        this.arithmeticOperators = new HashMap<String, Operator>();
        if ( arithmeticOperators != null ) {
            for ( int i = 0; i < arithmeticOperators.length; i++ ) {
                this.arithmeticOperators.put( arithmeticOperators[i].getName(), arithmeticOperators[i] );
            }
        }
    }

    /**
     * 
     * @return Arithmetic Operators
     */
    public Operator[] getArithmeticOperators() {
        return arithmeticOperators.values().toArray( new Operator[arithmeticOperators.values().size()] );
    }

    /**
     * Returns if the given operator is supported.
     * 
     * @param operatorName
     * @return if the given operator is supported.
     */
    public boolean hasComparisonOperator( String operatorName ) {
        return comparisonOperators.get( operatorName ) != null ? true : false;
    }

    /**
     * Returns if the given operator is supported.
     * 
     * @param operatorName
     * @return if the given operator is supported.
     */
    public boolean hasArithmeticOperator( String operatorName ) {
        return arithmeticOperators.get( operatorName ) != null ? true : false;
    }
}