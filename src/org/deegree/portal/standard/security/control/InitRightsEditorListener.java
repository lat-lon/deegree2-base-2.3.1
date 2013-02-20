//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/security/control/InitRightsEditorListener.java $
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
package org.deegree.portal.standard.security.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.deegree.datatypes.QualifiedName;
import org.deegree.enterprise.control.AbstractListener;
import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCException;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCParameter;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.model.filterencoding.ComplexFilter;
import org.deegree.model.filterencoding.Filter;
import org.deegree.model.filterencoding.Literal;
import org.deegree.model.filterencoding.LogicalOperation;
import org.deegree.model.filterencoding.Operation;
import org.deegree.model.filterencoding.OperationDefines;
import org.deegree.model.filterencoding.PropertyIsCOMPOperation;
import org.deegree.model.filterencoding.PropertyName;
import org.deegree.model.filterencoding.SpatialOperation;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.Position;
import org.deegree.ogcbase.PropertyPath;
import org.deegree.ogcbase.PropertyPathFactory;
import org.deegree.security.GeneralSecurityException;
import org.deegree.security.drm.SecurityAccess;
import org.deegree.security.drm.model.Right;
import org.deegree.security.drm.model.RightType;
import org.deegree.security.drm.model.Role;
import org.deegree.security.drm.model.SecuredObject;
import org.deegree.security.drm.model.User;

/**
 * This <code>Listener</code> reacts on RPC-EditRole events, extracts the submitted role-id and
 * passes the role + known securable objects on the JSP.
 * <p>
 * Access constraints:
 * <ul>
 * <li>only users that have the 'SEC_ADMIN'-role are allowed</li>
 * </ul>
 * 
 * @author <a href="mschneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public class InitRightsEditorListener extends AbstractListener {

    private static final ILogger LOG = LoggerFactory.getLogger( InitRightsEditorListener.class );

    @Override
    public void actionPerformed( FormEvent event ) {

        try {
            // perform access check
            SecurityAccess access = SecurityHelper.acquireAccess( this );
            SecurityHelper.checkForAdminRole( access );
            User user = access.getUser();

            RPCWebEvent ev = (RPCWebEvent) event;
            RPCMethodCall rpcCall = ev.getRPCMethodCall();
            RPCParameter[] params = rpcCall.getParameters();

            if ( params.length != 1 ) {
                throw new RPCException( Messages.getMessage( "IGEO_STD_SEC_WRONG_PARAM_NUM" ) );
            }
            if ( params[0].getType() != String.class ) {
                throw new RPCException( Messages.getMessage( "IGEO_STD_SEC_MISSING_STRING" ) );
            }
            int roleId = -1;
            try {
                roleId = Integer.parseInt( (String) params[0].getValue() );
            } catch ( NumberFormatException e ) {
                throw new RPCException( Messages.getMessage( "IGEO_STD_SEC_WRONG_ROLE_VALUE" ) );
            }

            // get Role to be edited
            Role role = access.getRoleById( roleId );

            // check if user has the right to update the role
            if ( !user.hasRight( access, RightType.UPDATE, role ) ) {
                throw new GeneralSecurityException( Messages.getMessage( "IGEO_STD_SEC_MISSING_RIGHT", role.getName() ) );
            }

            SecuredObject[] layers = access.getAllSecuredObjects( ClientHelper.TYPE_LAYER );
            SecuredObjectRight[] getMapRights = new SecuredObjectRight[layers.length];
            for ( int i = 0; i < layers.length; i++ ) {
                Right right = role.getRights( access, layers[i] ).getRight( layers[i], RightType.GETMAP );
                boolean isAccessible = right != null ? true : false;
                Map constraintsMap = new HashMap();
                if ( right != null && right.getConstraints() != null ) {
                    constraintsMap = buildConstraintsMap( right.getConstraints() );
                }
                getMapRights[i] = new SecuredObjectRight( isAccessible, layers[i], constraintsMap );
            }

            SecuredObject[] featureTypes = access.getAllSecuredObjects( ClientHelper.TYPE_FEATURETYPE );
            SecuredObjectRight[] getFeatureRights = new SecuredObjectRight[featureTypes.length];
            boolean[] deleteRights = new boolean[featureTypes.length];
            boolean[] insertRights = new boolean[featureTypes.length];
            boolean[] updateRights = new boolean[featureTypes.length];
            for ( int i = 0; i < featureTypes.length; i++ ) {
                Right right = role.getRights( access, featureTypes[i] ).getRight( featureTypes[i], RightType.GETFEATURE );
                boolean isAccessible = right != null ? true : false;
                Map constraints = new HashMap();
                getFeatureRights[i] = new SecuredObjectRight( isAccessible, featureTypes[i], constraints );
                right = role.getRights( access, featureTypes[i] ).getRight( featureTypes[i], RightType.INSERT );
                insertRights[i] = right != null ? true : false;
                right = role.getRights( access, featureTypes[i] ).getRight( featureTypes[i], RightType.UPDATE );
                updateRights[i] = right != null ? true : false;
                right = role.getRights( access, featureTypes[i] ).getRight( featureTypes[i], RightType.DELETE );
                deleteRights[i] = right != null ? true : false;
            }

            getRequest().setAttribute( "ROLE", role );
            getRequest().setAttribute( "RIGHTS_GET_MAP", getMapRights );
            getRequest().setAttribute( "RIGHTS_GET_FEATURE", getFeatureRights );
            getRequest().setAttribute( "RIGHTS_DELETE_FEATURE", deleteRights );
            getRequest().setAttribute( "RIGHTS_INSERT_FEATURE", insertRights );
            getRequest().setAttribute( "RIGHTS_UPDATE_FEATURE", updateRights );
        } catch ( RPCException e ) {
            LOG.logError( e.getMessage(), e );
            getRequest().setAttribute( "SOURCE", this.getClass().getName() );
            getRequest().setAttribute( "MESSAGE",
                                       Messages.getMessage( "IGEO_STD_SEC_ERROR_RIGHTSEDITOR_REQUEST", e.getMessage() ) );
            setNextPage( "error.jsp" );
        } catch ( GeneralSecurityException e ) {
            LOG.logError( e.getMessage(), e );
            getRequest().setAttribute( "SOURCE", this.getClass().getName() );
            getRequest().setAttribute( "MESSAGE",
                                       Messages.getMessage( "IGEO_STD_SEC_ERROR_RIGHTSEDITOR", e.getMessage() ) );
            setNextPage( "error.jsp" );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
        }

    }

    /**
     * Reconstructs the constraints map (keys are Strings, values are arrays of Strings) from the
     * given <code>Filter</code> expression. This only works if the expression meets the very
     * format used by the <code>StoreRightsListener</code>.
     * 
     * @param filter
     * @return Map
     * @throws SecurityException
     */
    private Map buildConstraintsMap( Filter filter )
                            throws SecurityException {
        Map<String, String[]> constraintsMap = new HashMap<String, String[]>();
        if ( filter instanceof ComplexFilter ) {
            Operation operation = ( (ComplexFilter) filter ).getOperation();
            if ( operation.getOperatorId() == OperationDefines.AND ) {
                LogicalOperation andOperation = (LogicalOperation) operation;
                Iterator it = andOperation.getArguments().iterator();
                while ( it.hasNext() ) {
                    addConstraintToMap( (Operation) it.next(), constraintsMap );
                }
            } else {
                addConstraintToMap( operation, constraintsMap );
            }
        }
        return constraintsMap;
    }

    /**
     * Extracts the constraint in the given <code>Operation</code> and adds it to the also
     * supplied <code>Map</code>. The <code>Operation</code> must be of type
     * OperationDefines.OR (with children that are all of type <code>PropertyIsCOMPOperations</code>
     * or <code>BBOX</code>) or of type <code>PropertyIsCOMPOperation</code>(<code>BBOX</code>),
     * in any other case this method will fail.
     * 
     * @param operation
     * @param map
     * @throws SecurityException
     */
    private void addConstraintToMap( Operation operation, Map<String, String[]> map )
                            throws SecurityException {

        PropertyPath constraintName = null;
        String[] parameters = new String[1];

        if ( operation instanceof PropertyIsCOMPOperation ) {
            PropertyIsCOMPOperation comparison = (PropertyIsCOMPOperation) operation;
            try {
                constraintName = ( (PropertyName) comparison.getFirstExpression() ).getValue();
                parameters[0] = ( (Literal) comparison.getSecondExpression() ).getValue();
            } catch ( ClassCastException e ) {
                LOG.logDebug( e.getMessage(), e );
                throw new SecurityException( "Unable to reconstruct constraint map from stored filter expression." );
            }
        } else if ( operation.getOperatorId() == OperationDefines.BBOX
                    || operation.getOperatorId() == OperationDefines.WITHIN
                    || operation.getOperatorId() == OperationDefines.CONTAINS ) {
            constraintName = PropertyPathFactory.createPropertyPath( new QualifiedName( "bbox" ) );
            SpatialOperation spatialOperation = (SpatialOperation) operation;
            Envelope envelope = spatialOperation.getGeometry().getEnvelope();
            try {
                Position max = envelope.getMax();
                Position min = envelope.getMin();
                parameters = new String[4];
                parameters[0] = "" + min.getX();
                parameters[1] = "" + min.getY();
                parameters[2] = "" + max.getX();
                parameters[3] = "" + max.getY();
            } catch ( ClassCastException e ) {
                LOG.logDebug( e.getMessage(), e );
                throw new SecurityException( "Unable to reconstruct constraint map from stored filter expression." );
            }
        } else if ( operation.getOperatorId() == OperationDefines.OR ) {
            LogicalOperation logical = (LogicalOperation) operation;
            Iterator it = logical.getArguments().iterator();
            ArrayList<String> parameterList = new ArrayList<String>( 10 );
            while ( it.hasNext() ) {
                try {
                    PropertyIsCOMPOperation argument = (PropertyIsCOMPOperation) it.next();
                    PropertyName propertyName = (PropertyName) argument.getFirstExpression();
                    if ( constraintName != null && ( !constraintName.equals( propertyName.getValue() ) ) ) {
                        throw new SecurityException(
                                                     "Unable to reconstruct constraint map from stored filter expression." );
                    }
                    constraintName = propertyName.getValue();
                    parameterList.add( ( (Literal) argument.getSecondExpression() ).getValue() );
                } catch ( ClassCastException e ) {
                    LOG.logDebug( e.getMessage(), e );
                    throw new SecurityException( "Unable to reconstruct constraint map from stored filter expression. "
                                                 + "Invalid filter format." );
                }
            }
            parameters = parameterList.toArray( new String[parameterList.size()] );
        } else {
            LOG.logDebug( "OperatorId = " + operation.getOperatorId() );
            throw new SecurityException( "Unable to reconstruct constraint map from stored filter expression: "
                                         + operation );
        }
        map.put( constraintName.getAsString(), parameters );
    }
}
