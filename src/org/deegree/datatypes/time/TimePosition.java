//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/time/TimePosition.java $
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
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */

public class TimePosition implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private TimeIndeterminateValue indeterminatePosition = null;

    private String calendarEraName = null;

    private URI frame = null;

    private Calendar time = null;

    /**
     * defaults are:
     * <ul>
     * <li>indeterminatePosition = now</li>
     * <li>calendarEraName = AC</li>
     * <li>frame = #ISO-8601</li>
     * <li>time = new GregorianCalendar()</li>
     * </ul>
     */
    public TimePosition() {
        indeterminatePosition = new TimeIndeterminateValue();
        calendarEraName = "AC";
        try {
            frame = new URI( "#ISO-8601" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        time = new GregorianCalendar();
    }

    /**
     * defaults are:
     * <ul>
     * <li>indeterminatePosition = now</li>
     * <li>calendarEraName = AC</li>
     * <li>frame = #ISO-8601</li>
     * </ul>
     * 
     * @param time
     */
    public TimePosition( Calendar time ) {
        this.time = time;
        indeterminatePosition = new TimeIndeterminateValue();
        calendarEraName = "AC";
        try {
            frame = new URI( "#ISO-8601" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * @param indeterminatePosition
     * @param calendarEraName
     * @param frame
     * @param time
     */
    public TimePosition( TimeIndeterminateValue indeterminatePosition, String calendarEraName, URI frame, Calendar time ) {
        this.indeterminatePosition = indeterminatePosition;
        this.calendarEraName = calendarEraName;
        this.frame = frame;
        this.time = time;
    }

    /**
     * @return Returns the calendarEraName.
     * 
     */
    public String getCalendarEraName() {
        return calendarEraName;
    }

    /**
     * @param calendarEraName
     *            The calendarEraName to set.
     * 
     */
    public void setCalendarEraName( String calendarEraName ) {
        this.calendarEraName = calendarEraName;
    }

    /**
     * @return Returns the frame.
     * 
     */
    public URI getFrame() {
        return frame;
    }

    /**
     * @param frame
     *            The frame to set.
     * 
     */
    public void setFrame( URI frame ) {
        this.frame = frame;
    }

    /**
     * @return Returns the indeterminatePosition.
     * 
     */
    public TimeIndeterminateValue getIndeterminatePosition() {
        return indeterminatePosition;
    }

    /**
     * @param indeterminatePosition
     *            The indeterminatePosition to set.
     * 
     */
    public void setIndeterminatePosition( TimeIndeterminateValue indeterminatePosition ) {
        this.indeterminatePosition = indeterminatePosition;
    }

    /**
     * @return Returns the time.
     * 
     */
    public Calendar getTime() {
        return time;
    }

    /**
     * @param time
     *            The time to set.
     * 
     */
    public void setTime( Calendar time ) {
        this.time = time;
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new TimePosition( indeterminatePosition, calendarEraName, frame, time );
    }

}