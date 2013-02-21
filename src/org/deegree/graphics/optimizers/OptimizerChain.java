//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/optimizers/OptimizerChain.java $
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
package org.deegree.graphics.optimizers;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Allows the chaining of {@link Optimizer}s. Implements the {@link Optimizer} interface as well.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 12150 $, $Date: 2008-06-04 13:44:43 +0200 (Mi, 04. Jun 2008) $
 */
public class OptimizerChain extends AbstractOptimizer {

    // stores the Optimizers in the chain
    private ArrayList<Optimizer> optimizers = new ArrayList<Optimizer>();

    /**
     * Creates an empty instance of {@link OptimizerChain}.
     */
    public OptimizerChain() {
        // nothing to do
    }

    /**
     * Constructs a new {@link OptimizerChain} that contains the given {@link Optimizer} instances.
     * 
     * @param optimizers
     */
    public OptimizerChain( Optimizer[] optimizers ) {
        for ( int i = 0; i < optimizers.length; i++ ) {
            this.optimizers.add( optimizers[i] );
        }
    }

    /**
     * Appends an {@link Optimizer} to the end of the processing chain.
     * 
     * @param optimizer
     *            {@link Optimizer} to be added
     */
    public void addOptimizer( Optimizer optimizer ) {
        optimizers.add( optimizer );
    }

    /**
     * Performs the optimization for all contained {@link Optimizer} instances. Calls
     * {@link Optimizer#optimize(Graphics2D)} for all contained {@link Optimizer} instances. subsequently.
     * 
     * @param g
     */
    public void optimize( Graphics2D g )
                            throws Exception {
        for ( Optimizer optimizer : optimizers ) {
            optimizer.optimize( g );
        }
    }
}