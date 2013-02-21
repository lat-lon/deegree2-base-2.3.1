// $HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/ParameterValueIm.java $
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
 * @version $Revision: 11711 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11711 $, $Date: 2008-05-14 11:15:46 +0200 (Mi, 14. Mai 2008) $
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
     * @return the value as a boolean.
     * @throws InvalidParameterTypeException 
     */
    public boolean booleanValue()
                            throws InvalidParameterTypeException {
        return ( (Boolean) value ).booleanValue();
    }

    /**
     * @return the value as a double.
     * @throws InvalidParameterTypeException 
     */
    public double doubleValue()
                            throws InvalidParameterTypeException {
        return ( (Double) value ).doubleValue();
    }

    /**
     * @return the value list as an array of doubles..
     * @throws InvalidParameterTypeException 
     */
    public double[] doubleValueList()
                            throws InvalidParameterTypeException {
        return (double[]) value;
    }

    /**
     * @return the value.
     */
    public String getUnit() {
        return (String) value;
    }

    /**
     * @return the value as an object.
     * 
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return the value as an int
     * @throws InvalidParameterTypeException 
     */
    public int intValue()
                            throws InvalidParameterTypeException {
        return ( (Integer) value ).intValue();
    }

    /**
     * @return the value as an array of integers
     * @throws InvalidParameterTypeException 
     */
    public int[] intValueList()
                            throws InvalidParameterTypeException {
        return (int[]) value;
    }

    /**
     * @param unit to set
     * @throws InvalidParameterTypeException 
     */
    public void setUnit( String unit )
                            throws InvalidParameterTypeException {
        value = unit;
    }

    /**
     * @param value to set
     * @throws InvalidParameterValueException 
     */
    public void setValue( boolean value )
                            throws InvalidParameterValueException {
        this.value = Boolean.valueOf( value );
    }

    /**
     * @param value to set
     * @throws InvalidParameterValueException 
     */
    public void setValue( double value )
                            throws InvalidParameterValueException {
        this.value = new Double( value );
    }

    /**
     * @param value to set
     * @throws InvalidParameterValueException 
     */
    public void setValue( int value )
                            throws InvalidParameterValueException {
        this.value = new Integer( value );
    }

    /**
     * @param value to set
     * @throws InvalidParameterValueException 
     * 
     */
    public void setValue( Object value )
                            throws InvalidParameterValueException {
        this.value = value;
    }

    /**
     * @return the value as a String
     * @throws InvalidParameterTypeException 
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
     * @return the location of the value file.
     * @throws InvalidParameterTypeException
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
