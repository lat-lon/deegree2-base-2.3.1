//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/alltests/AllTests.java $
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
package alltests;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;

/**
 * Main test class for project deegree.
 * <p>
 * Uses the list of test classes from property "test.classfiles" in file "test.properties".
 * </p>
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 */
public final class AllTests {

    private static final int DOT_CLASS_LENGTH = ".class".length();

    /**
     * Logger for test cases
     */
    private static final ILogger LOG = LoggerFactory.getLogger( AllTests.class );

    /**
     * @return list of tests
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        try {
            Class<?> test = Class.forName( AllTests.class.getName() );

            URL resource = test.getResource( "configuration.properties" );
            if ( resource == null ) {
                LOG.logError( "Could not load resource: configuration.properties this is akward" );
            } else {
                File f = new File( resource.getFile() );
                String parent = new File( f.getParent() ).getParent();
                final String prefix = "/org/deegree/";
                f = new File( parent + prefix );
                if ( f.exists() ) {
                    LOG.logInfo( f.getAbsolutePath() + " exists trying to load classes from: " + parent );
                    findAndAddClasses( parent, suite, f, new CustomFileFilter() );
                } else {
                    LOG.logError( f.getAbsolutePath() + " does not denote the root directory of deegree test suite." );
                }
            }
        } catch ( ClassNotFoundException e ) {
            LOG.logError( e.getMessage(), e );
        }
        return suite;
    }

    /**
     * @param suite
     * @param parent
     */
    private static void findAndAddClasses( final String prefix, TestSuite suite, File parent, CustomFileFilter filter ) {
        if ( parent != null ) {
            File[] sons = parent.listFiles( filter );
            for ( File tmp : sons ) {
                if ( tmp.isDirectory() ) {
                    findAndAddClasses( prefix, suite, tmp, filter );
                } else {
                    String className = tmp.getAbsoluteFile().toString().substring( prefix.length() + 1 );
                    className = className.substring( 0, className.length() - DOT_CLASS_LENGTH );
                    className = className.replace( File.pathSeparatorChar, '.' );
                    // sometimes on windows this is the default behavior
                    className = className.replace( '/', '.' );
                    Class<?> testClass = null;
                    try {
                        testClass = Class.forName( className );
                    } catch ( ClassNotFoundException cnfe ) {
                        LOG.logError( cnfe.getLocalizedMessage(), cnfe );
                    }
                    if ( testClass == null ) {
                        LOG.logError( "Could not load class: " + className );
                    } else {
                        boolean loadClass = true;
                        try {
                            testClass.asSubclass( TestCase.class );
                        } catch ( ClassCastException cce ) {
                            loadClass = false;
                            LOG.logDebug( "Not adding class: " + className
                                          + " because it is not a sub class of junit.framework.TestCase" );
                        }
                        if ( loadClass ) {
                            Method[] methods = testClass.getMethods();
                            boolean hasTestMethod = false;
                            for ( Method m : methods ) {
                                if ( m != null ) {
                                    if ( m.getName() != null && ( m.getModifiers() == Modifier.PUBLIC ) ) {
                                        if ( m.getName().startsWith( "test" ) ) {
                                            hasTestMethod = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if ( hasTestMethod ) {
                                LOG.logInfo( "adding class: " + className );
                                suite.addTestSuite( testClass );
                            } else {
                                LOG.logDebug( "Not adding class: " + className
                                              + " because it is does not have a test method." );
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * @param args
     */
    public static void main( String[] args ) {
        TestRunner runner = new TestRunner();
        if ( args.length == 0 ) {
            args = new String[] { "-noloading", "alltests.AllTests" };
        }
        runner.start( args );
    }

    /**
     * 
     * The <code>CustomFileFilter</code> class adds functionality to the filefilter mechanism of the JFileChooser.
     * 
     * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
     * 
     * @author last edited by: $Author: rbezema $
     * 
     * @version $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
     * 
     */
    static class CustomFileFilter implements java.io.FileFilter {

        public boolean accept( File pathname ) {
            if ( pathname.isDirectory() ) {
                return true;
            }

            String extension = getExtension( pathname );
            if ( extension != null ) {
                if ( "class".equals( extension.trim() ) ) {
                    return true;
                }
            }
            return false;
        }

        private String getExtension( File f ) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf( '.' );

            if ( i > 0 && i < s.length() - 1 ) {
                ext = s.substring( i + 1 ).toLowerCase();
            }
            return ext;
        }
    }

}
