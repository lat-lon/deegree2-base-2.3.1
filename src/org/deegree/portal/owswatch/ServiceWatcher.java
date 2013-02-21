//$$Header: $$
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

package org.deegree.portal.owswatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Mail class of this framework. Its called upon starting tomcat to start watching services and test them regularly
 * according to their test intervals and logs the result through its service invoker
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.4 $, $Date: 2008-03-20 16:33:27 $
 */
public class ServiceWatcher extends Thread implements Serializable {

    private static final long serialVersionUID = -5247964268533099679L;

    private volatile int minTestInterval = 0;

    private boolean threadsuspended = true;

    /**
     * This hash table holds all ServiceMonitors in the form <serviceHashcode,ServiceMonitor>
     */
    static private TreeMap<Integer, ServiceConfiguration> services = new TreeMap<Integer, ServiceConfiguration>();

    /**
     * Integer Service id ServiceLog Logger class
     */
    private Map<ServiceConfiguration, ServiceLog> serviceLogs = new HashMap<ServiceConfiguration, ServiceLog>();

    /**
     * This hash table holds the time of the next run for the given Services in the form <ServiceHashcode,Time remaining
     * for the next run>
     */
    private Hashtable<Integer, Integer> activeServices = new Hashtable<Integer, Integer>( 30 );

    /**
     * This function will be called at the begining of the run() method
     */
    private void init() {

        Iterator<ServiceConfiguration> it = services.values().iterator();
        if ( it.hasNext() ) {
            threadsuspended = false;
        }
        while ( it.hasNext() ) {
            ServiceConfiguration service = it.next();
            int serviceId = service.getServiceid();

            // If this is an active service add it to the list of active services
            if ( service.isActive() ) {
                // Use the minimum refreshRate as a refreshRate for the watcher thread
                if ( minTestInterval == 0 || service.getRefreshRate() < minTestInterval ) {
                    minTestInterval = service.getRefreshRate();
                }
                activeServices.put( Integer.valueOf( serviceId ), Integer.valueOf( 0 ) );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {

        init();
        // This will cause the function to start at hours:minutes:00 seconds
        try {
            int seconds = Calendar.getInstance().get( Calendar.SECOND );
            sleep( ( 60 - seconds ) * 1000 );
        } catch ( InterruptedException e ) {
            // Nothing. Its possible to be interrupted
        }
        Calendar nextCall = Calendar.getInstance();
        while ( true ) {
            try {
                synchronized ( this ) {
                    while ( threadsuspended ) {
                        wait( 1000 );
                    }
                    // In case the wait is interrupted, we guarantee that the test will not be
                    // executed until its 00 seconds
                    if ( Calendar.getInstance().get( Calendar.SECOND ) != 0 ) {
                        int seconds = Calendar.getInstance().get( Calendar.SECOND );
                        sleep( ( 60 - seconds ) * 1000 );
                    }
                    refreshAllServices();
                    // if true then we have to shift the time system less than one minute forward
                    // to make the seconds 00 again
                    nextCall.add( Calendar.MINUTE, minTestInterval );
                    long sleepTime = nextCall.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                    wait( sleepTime );
                }
            } catch ( InterruptedException e ) {
                // Nothing. Its possible to be interrupted
            }
        }
    }

    /**
     * adds a new Service to the ServiceList
     * 
     * @param service
     * @param servicelog
     */
    public synchronized void addService( ServiceConfiguration service, ServiceLog servicelog ) {

        if ( !services.containsKey( Integer.valueOf( service.getServiceid() ) ) ) {
            serviceLogs.put( service, servicelog );
        } else {
            ServiceLog log = serviceLogs.remove( services.get( Integer.valueOf( service.getServiceid() ) ) );
            serviceLogs.put( service, log );
        }
        services.put( Integer.valueOf( service.getServiceid() ), service );

        if ( service.isActive() ) {
            if ( minTestInterval == 0 || service.getRefreshRate() < minTestInterval ) {
                minTestInterval = service.getRefreshRate();
                threadsuspended = false;
                this.notifyAll();
            }
            activeServices.put( Integer.valueOf( service.getServiceid() ), Integer.valueOf( 0 ) );
        }
    }

    /**
     * returns the service identified by its Id in the serviceList
     * 
     * @param serviceId
     * @return ServiceConfiguration
     */
    public ServiceConfiguration getService( int serviceId ) {
        return services.get( Integer.valueOf( serviceId ) );
    }

    /**
     * removes the service identify by -id- from the ServiceList
     * 
     * @param serviceId
     * @return new ServiceConfiguration
     * 
     */
    public ServiceConfiguration removeService( int serviceId ) {
        if ( activeServices.containsKey( Integer.valueOf( serviceId ) ) ) {
            ServiceConfiguration service = services.get( Integer.valueOf( serviceId ) );
            activeServices.remove( Integer.valueOf( serviceId ) );
            serviceLogs.remove( service );
            if ( service.getRefreshRate() == minTestInterval ) {
                minTestInterval = findMinTestInterval();
            }
            if ( minTestInterval == 0 ) {
                threadsuspended = true;
            }
        }

        return services.remove( serviceId );
    }

    /**
     * Finds the minimum test interval from the active Services
     */
    private int findMinTestInterval() {
        int minInterval = 0;
        Iterator<Integer> it = activeServices.keySet().iterator();
        while ( it.hasNext() ) {
            int serviceRefreshRate = services.get( it.next() ).getRefreshRate();
            if ( minInterval == 0 || serviceRefreshRate < minInterval ) {
                minInterval = serviceRefreshRate;
            }
        }
        return minInterval;
    }

    /**
     * This method will set the threadSuspended vaiable to true in order for the id-th service to stop sending
     * GetCapabilities requests
     * 
     * @param serviceId
     * 
     */
    public void stopServiceConfiguration( int serviceId ) {
        activeServices.remove( serviceId );
    }

    /**
     * This method will set the threadSuspended vaiable to false in order for the id-th service to restart sending
     * requests
     * 
     * @param serviceId
     * 
     */
    public void startServiceConfiguration( int serviceId ) {
        activeServices.put( serviceId, Integer.valueOf( 0 ) );
    }

    /**
     * @param serviceId
     * @return true if the currrent Service id at all exists in watcher, false otherwise
     */
    public boolean containsService( int serviceId ) {
        return services.containsKey( serviceId );
    }

    /**
     * returns list of services being watched
     * 
     * @return the serviceList
     */
    public List<ServiceConfiguration> getServiceList() {
        return new ArrayList<ServiceConfiguration>( services.values() );
    }

    /**
     * RefreshAllServices executes all the requests in the active monitors, and subtract the time from those who are
     * waiting in line and restarts the time for those who had a turn This is the default to be called during the
     * regular tests. If you want to execute tests in the main thread and without affecting the regular tests
     * executeTest(int) and executeall()
     */
    public void refreshAllServices() {

        Set<Integer> keys = activeServices.keySet();
        Iterator<Integer> it = keys.iterator();
        while ( it.hasNext() ) {
            Integer key = it.next();
            int value = activeServices.get( key );
            value -= minTestInterval;
            ServiceConfiguration service = services.get( key );
            // if the waiting time for this service has elapsed, and now its time to execute a new request
            if ( value <= 0 ) {

                // resets the timing
                // -value adjusts the timing exactly to the subtracted sum. For example if a service refreshs
                // every 3 minutes and value is -1, that means this service has to wait 2 minutes this time not 3
                // activeServices.put( key, (Integer) service.getRefreshRate() - value );
                activeServices.put( key, (Integer) service.getRefreshRate() );
                ServiceLog serviceLog = serviceLogs.get( services.get( key ) );
                new ServiceInvoker( service, serviceLog ).executeTestThreaded();
            } else {
                // else subtract the refresh rate
                activeServices.put( key, (Integer) value );
            }
        }
    }

    /**
     * executes the given service one time in the main thread
     * 
     * @param serviceId
     */
    public void execute( int serviceId ) {

        ServiceConfiguration service = services.get( serviceId );
        if ( service == null ) {
            return;
        }
        ServiceLog serviceLog = this.serviceLogs.get( service );
        new ServiceInvoker( service, serviceLog ).executeTest();
    }

    /**
     * @return A map of all services
     */
    public Map<Integer, ServiceConfiguration> getServices() {
        return services;
    }

    /**
     * @return Map<Integer, ServiceLog> key = ServiceConfiguration id
     */
    public Map<ServiceConfiguration, ServiceLog> getServiceLogs() {
        return serviceLogs;
    }

    /**
     * @return true if thread is suspended, false otherwise
     */
    public boolean isThreadsuspended() {
        return threadsuspended;
    }

    /**
     * @param threadsuspended
     */
    public void setThreadsuspended( boolean threadsuspended ) {
        this.threadsuspended = threadsuspended;
    }
}
