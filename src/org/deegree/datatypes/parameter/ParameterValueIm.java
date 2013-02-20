// $HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/ParameterValueIm.java $
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
package org.deegree.datatypes.parameter;

import java.io.Serializable;
import java.net.URL;

/**
 * 
 * 
 * @version $Revision: 9337 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9337 $, $Date: 2007-12-27 13:31:11 +0100 (Thu, 27 Dec 2007) $
 */
public class ParameterValueIm extends GeneralParameterValueIm implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object value = null;

    /**
     * @param descriptor
     */
    public ParameterValueIm( GeneralOperationParameterIm descriptor ) {
        super( descriptor );
    }

    /**
     * @param descriptor
     * @param value
     */
    public ParameterValueIm( GeneralOperationParameterIm descriptor, Object value ) {
        super( descriptor );
        setValue( value );
    }

    /**
     * @param descriptor
     * @param value
     */
    public ParameterValueIm( GeneralOperationParameterIm descriptor, String value ) {
        super( descriptor );
        setValue( value );
    }

    /**
     * @param descriptor
     * @param value
     */
    public ParameterValueIm( GeneralOperationParameterIm descriptor, URL value ) {
        super( descriptor );
        setValue( value );
    }

    /**
     * @param descriptor
     * @param value
     */
    public ParameterValueIm( GeneralOperationParameterIm descriptor, int value ) {
        super( descriptor );
        setValue( value );
    }

    /**
     * @param descriptor
     * @param value
     */
    public ParameterValueIm( GeneralOperationParameterIm descriptor, double value ) {
        super( descriptor );
        setValue( value );
    }

    /**
     * @param descriptor
     * @param value
     */
    public ParameterValueIm( GeneralOperationParameterIm descriptor, boolean value ) {
        super( descriptor );
        setValue( value );
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#booleanValue()"
     */
    public boolean booleanValue()
                            throws InvalidParameterTypeException {
        return ( (Boolean) value ).booleanValue();
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#doubleValue()"
     */
    public double doubleValue()
                            throws InvalidParameterTypeException {
        return ( (Double) value ).doubleValue();
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#doubleValueList()"
     */
    public double[] doubleValueList()
                            throws InvalidParameterTypeException {
        return (double[]) value;
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#getUnit()"
     */
    public String getUnit() {
        return (String) value;
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#getValue()"
     * 
     */
    public Object getValue() {
        return value;
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#intValue()"
     */
    public int intValue()
                            throws InvalidParameterTypeException {
        return ( (Integer) value ).intValue();
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#intValueList()"
     */
    public int[] intValueList()
                            throws InvalidParameterTypeException {
        return (int[]) value;
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#setUnit(javax.units.Unit)"
     */
    public void setUnit( String unit )
                            throws InvalidParameterTypeException {
        value = unit;
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#setValue(boolean)"
     */
    public void setValue( boolean value )
                            throws InvalidParameterValueException {
        this.value = Boolean.valueOf( value );
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#setValue(double)"
     */
    public void setValue( double value )
                            throws InvalidParameterValueException {
        this.value = new Double( value );
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#setValue(int)"
     */
    public void setValue( int value )
                            throws InvalidParameterValueException {
        this.value = new Integer( value );
    }

    /**
     * "@see org.opengis.parameter.ParameterValue#setValue(java.lang.Object)"
     * 
     */
    public void setValue( Object value )
                            throws InvalidParameterValueException {
        this.value = value;
    }

    /**
     * @see "org.opengis.parameter.ParameterValue#stringValue()"
     */
    public String stringValue()
                            throws InvalidParameterTypeException {
        return (String) value;
    }

    /**
     * @param value
     */
    public void setValue( String value ) {
        this.value = value;
    }

    /**
     * @throws InvalidParameterTypeException
     * @see "org.opengis.parameter.ParameterValue#valueFile()"
     */
    public URL valueFile()
                            throws InvalidParameterTypeException {
        return (URL) value;
    }

    /**
     * @param value
     */
    public void setValue( URL value ) {
        this.value = value;
    }

}
