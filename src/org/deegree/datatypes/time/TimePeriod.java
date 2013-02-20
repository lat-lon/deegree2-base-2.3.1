//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/time/TimePeriod.java $
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
package org.deegree.datatypes.time;

import java.io.Serializable;
import java.net.URI;

/**
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */

public class TimePeriod implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private TimePosition beginPosition = null;

    private TimePosition endPosition = null;

    private TimeDuration timeResolution = null;

    private URI frame = null;

    /**
     * @param beginPosition
     * @param endPosition
     * @param timeResolution
     */
    public TimePeriod( TimePosition beginPosition, TimePosition endPosition, TimeDuration timeResolution ) {
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
        this.timeResolution = timeResolution;
    }

    /**
     * @param beginPosition
     * @param endPosition
     * @param timeResolution
     * @param frame
     */
    public TimePeriod( TimePosition beginPosition, TimePosition endPosition, TimeDuration timeResolution, URI frame ) {
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
        this.timeResolution = timeResolution;
        this.frame = frame;
    }

    /**
     * @return Returns the beginPosition.
     */
    public TimePosition getBeginPosition() {
        return beginPosition;
    }

    /**
     * @param beginPosition
     *            The beginPosition to set.
     */
    public void setBeginPosition( TimePosition beginPosition ) {
        this.beginPosition = beginPosition;
    }

    /**
     * @return Returns the endPosition.
     */
    public TimePosition getEndPosition() {
        return endPosition;
    }

    /**
     * @param endPosition
     *            The endPosition to set.
     */
    public void setEndPosition( TimePosition endPosition ) {
        this.endPosition = endPosition;
    }

    /**
     * @return Returns the frame.
     */
    public URI getFrame() {
        return frame;
    }

    /**
     * @param frame
     *            The frame to set.
     */
    public void setFrame( URI frame ) {
        this.frame = frame;
    }

    /**
     * @return Returns the timeResolution.
     */
    public TimeDuration getTimeResolution() {
        return timeResolution;
    }

    /**
     * @param timeResolution
     *            The timeResolution to set.
     */
    public void setTimeResolution( TimeDuration timeResolution ) {
        this.timeResolution = timeResolution;
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        URI fr = null;
        if ( frame != null ) {
            try {
                fr = new URI( frame.toString() );
            } catch ( Exception e ) {
            }
        }
        return new TimePeriod( (TimePosition) beginPosition.clone(), (TimePosition) endPosition.clone(),
                               (TimeDuration) timeResolution.clone(), fr );
    }

}
