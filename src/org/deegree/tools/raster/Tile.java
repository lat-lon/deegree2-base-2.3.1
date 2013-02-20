//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/tools/raster/Tile.java $
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

package org.deegree.tools.raster;

import org.deegree.model.spatialschema.Envelope;

/**
 * This class represents a <code>Tile</code> object, used to hold all information that is needed,
 * when drawing an image onto a tile in <code>MergeRaster</code>.
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: otonnhofer $
 * 
 * @version $Revision: 10531 $, $Date: 2008-03-10 09:45:59 +0100 (Mon, 10 Mar 2008) $
 */
class Tile {

    private Envelope tileEnvelope;

    private String postfix;

    /**
     * @param env
     *            The Envelope of the tile.
     * @param postfix
     *            Postfix of the tile file name (e.g. _3_5).
     */
    public Tile( Envelope env, String postfix ) {
        this.tileEnvelope = env;
        this.postfix = postfix;
    }

    /**
     * @return Returns the tileEnvelope.
     */
    public Envelope getTileEnvelope() {
        return tileEnvelope;
    }

    /**
     * @param tileEnvelope
     *            The tileEnvelope to set.
     */
    public void setTileEnvelope( Envelope tileEnvelope ) {
        this.tileEnvelope = tileEnvelope;
    }

    /**
     * @return Returns the postfix.
     */
    public String getPostfix() {
        return postfix;
    }

    /**
     * @param postfix
     *            The postfix to set.
     */
    public void setPostfix( String postfix ) {
        this.postfix = postfix;
    }

}
