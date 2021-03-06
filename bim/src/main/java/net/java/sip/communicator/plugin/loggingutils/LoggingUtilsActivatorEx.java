/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.plugin.loggingutils;

import net.java.sip.communicator.util.*;

import org.jitsi.service.fileaccess.*;
import org.jitsi.service.log.*;
import org.osgi.framework.*;

/**
 * Creates and registers logging config form.
 *
 * @author Damian Minkov
 */
public class LoggingUtilsActivatorEx extends LoggingUtilsActivator {
    /**
     * The OSGi service registration.
     */
    private ServiceRegistration logUploadServReg = null;

    /**
     * <tt>LogUploadService</tt> impl instance.
     */
    private LogUploadServiceImpl logUploadImpl;

    private static FileAccessService fileAccessService;

    /**
     * Creates and register logging configuration.
     *
     * @param bundleContext OSGI bundle context
     * @throws Exception if error creating configuration.
     */
    public void start(BundleContext bundleContext) throws Exception {
        LoggingUtilsActivator.bundleContext = bundleContext;

        getConfigurationService().setProperty(DISABLED_PROP, "true");

        super.start(bundleContext);

        logUploadImpl = new LogUploadServiceImpl();
        logUploadServReg = bundleContext.registerService(LogUploadService.class.getName(), logUploadImpl, null);
    }

    /**
     * Stops the Logging utils bundle
     *
     * @param bundleContext the OSGI bundle context
     */
    public void stop(BundleContext bundleContext) throws Exception {
        super.stop(bundleContext);

        logUploadServReg.unregister(); logUploadImpl.dispose();
    }

    /**
     * Returns a reference to a FileAccessService implementation
     * currently registered in the bundle context or null if no such
     * implementation was found.
     *
     * @return a currently valid implementation of the
     * FileAccessService .
     */
    public static FileAccessService getFileAccessService() {
        if (fileAccessService == null) {
            fileAccessService = ServiceUtils.getService(bundleContext, FileAccessService.class);
        } return fileAccessService;
    }
}
