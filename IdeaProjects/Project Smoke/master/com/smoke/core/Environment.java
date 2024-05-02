package com.smoke.core;

import com.smoke.communication.CommunicationBootstrap;
import com.smoke.communication.CommunicationController;
import com.smoke.communication.client.ClientCommunicationController;
import com.smoke.logging.Logger;
import com.smoke.settings.Properties;
import com.smoke.settings.PropertiesBox;
import com.smoke.utilities.runtime.GarbageController;
import com.smoke.utilities.runtime.RuntimeController;
import com.smoke.utilities.threading.ThreadController;
import com.smoke.utilities.timestamp.TimeHelper;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Environment {
    private static Logger mLogger;
    private static PropertiesBox mPropertiesBox;
    private static ThreadController mThreadController;
    private static GarbageController mGarbageController;
    private static RuntimeController mRuntimeController;
    private static CommunicationController mCommunicationController;
    private static ClientCommunicationController mClientCommunicationController;

    public  static long START_UP_TIME = 0x00000000l;
    private static final byte NORMAL_TERMINATION = 0x01;
    private static final byte ABNORMAL_TERMINATION = 0x00;

    // region #Accessors
    public static long traceNanoTime()
    {
        return System.nanoTime();
    }

    public static long traceMilliTime()
    {
        return System.currentTimeMillis();
    }

    public static Logger getLogger()
    {
        return Environment.mLogger;
    }

    public static PropertiesBox getProperties()
    {
        return Environment.mPropertiesBox;
    }

    public static ThreadController getThreadController()
    {
        return Environment.mThreadController;
    }

    public static GarbageController getGarbageController()
    {
        return Environment.mGarbageController;
    }

    public static RuntimeController getRuntimeController()
    {
        return Environment.mRuntimeController;
    }

    public static CommunicationController getCommunication() { return Environment.mCommunicationController; }

    public static ClientCommunicationController getClientCommunication() { return Environment.mClientCommunicationController; }
    // endregion

    // region #Methods
    private static void printOutBootBanner(ServerType serverType)
    {
        System.out.println();
        System.out.println("############################################################");
        System.out.println("##                  Project Smoke Server                  ##");
        System.out.println("##                 Written by Julian Reyes                ##");
        System.out.println("##    Special thanks to Jose Carlos Garcia & Jairo Eog    ##");
        switch (serverType)
        {
            default:
            case MASTER:
            {
                System.out.println("############################################################");
                System.out.println("##                     MASTER SERVER                      ##");
            }
            break;
            case PROXY:
            {
                System.out.println("############################################################");
                System.out.println("##                      PROXY SERVER                      ##");
            }
            break;
        }
        System.out.println("############################################################");
        System.out.println("##                 ENGINE BUILD: 0.1-dev                  ##");
        System.out.println("############################################################");
        System.out.println("##                 JVM VERSION: " + System.getProperty("java.version") + "                  ##");
        System.out.println("############################################################");
        System.out.println();
    }

    public static void terminate(boolean force)
    {
        if (force == false)
        {
            Environment.mLogger.destruct();
            Environment.mPropertiesBox.destruct();
        }

        System.exit(!force ? NORMAL_TERMINATION : ABNORMAL_TERMINATION);
    }

    public static void printOutBootInfo(String event)
    {
        System.out.println("[" + TimeHelper.getCurrentDate() + "][BOOT INFO] -- " + event);
    }

    public static void printOutBootError(String event)
    {
        System.err.println("[" + TimeHelper.getCurrentDate() + "][BOOT ERROR] -- " + event);
        System.err.println("[" + TimeHelper.getCurrentDate() + "][BOOT ERROR] -- Core bootstrap will now exit.");

        Environment.terminate(true);
    }

    public static void printOutBootError(String event, Exception ex)
    {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));

        System.err.println("[" + TimeHelper.getCurrentDate() + "][BOOT ERROR] -- " + event + "\nStack trace: " + writer.toString());
        System.err.println("[" + TimeHelper.getCurrentDate() + "][BOOT ERROR] -- Core startup will now exit."); Environment.terminate(true);
    }

    public static boolean bootstrap(String properties, ServerType serverType, String listenerClass)
    {
        switch (serverType) {
            case MASTER:
            default: {
                try {
                    // Track booting time
                    Environment.START_UP_TIME =
                            Environment.traceMilliTime();

                    // PrintOut some shoutouts
                    Environment.printOutBootBanner(serverType);

                    Environment.mPropertiesBox = new PropertiesBox();

                    if (Environment.mPropertiesBox.bootstrap(properties)) {
                        Environment.printOutBootInfo("Loaded " + Environment.mPropertiesBox.size() + " properties from " + properties);

                        Environment.mLogger = new Logger();
                        if (Environment.mLogger.bootstrap() == false) {
                            return false;
                        } else {
                            Environment.mLogger.enable();
                            Environment.printOutBootInfo("System logging interface has been successfully initialized.");
                        }


                        Environment.mThreadController = new ThreadController();
                        Environment.printOutBootInfo("ThreadController has been successfully initialized with: " + (Properties.THREADING_GENERAL_POOL_SIZE + Properties.THREADING_IO_POOL_SIZE + Properties.THREADING_PACKET_POOL_SIZE)
                                + " executors and " + (Properties.THREADING_AI_SCHEDULER_SIZE + Properties.THREADING_GENERAL_SCHEDULER_SIZE) + " schedulers.");

                        Environment.mCommunicationController = new CommunicationController(listenerClass, Properties.NIO_SERVER_PORT);
                        if (Environment.mCommunicationController.bootstrap() == false) {
                            return false;
                        } else {
                            Environment.mCommunicationController.initializeNetworking();
                            Environment.printOutBootInfo("CommunicationController has been successfully initialized with: " + CommunicationBootstrap.BOSS_POOL_SIZE + " i/o boss and " + CommunicationBootstrap.WORKER_POOL_SIZE + " i/o workers.");
                        }

                        // All is initialized... so start runtime monitors!
                        Environment.mGarbageController = new GarbageController();
                        Environment.mRuntimeController = new RuntimeController();

                        Environment.printOutBootInfo("Project Smoke has been initialized in " + (Environment.traceMilliTime() - Environment.START_UP_TIME) / 1000.0d + " seconds!");
                        System.out.println();

                        // All right
                        return true;
                    }
                } catch (Exception ex) {
                    Environment.printOutBootError("Environment.bootstrap() has thrown an exception while bootstrapping!", ex);
                }

                // W00t!?
                return false;
            }
            case PROXY: {
                try {
                    // Track booting time
                    Environment.START_UP_TIME =
                            Environment.traceMilliTime();

                    // PrintOut some shoutouts
                    Environment.printOutBootBanner(serverType);

                    Environment.mPropertiesBox = new PropertiesBox();
                    String[] propertiez = properties.split(";"), listenerClasz = listenerClass.split(";");

                    if (Environment.mPropertiesBox.bootstrap(propertiez[0]) && Environment.mPropertiesBox.bootstrap(propertiez[1])) {
                        Environment.printOutBootInfo("Loaded " + Environment.mPropertiesBox.size() + " properties from " + propertiez[0] + " and " + propertiez[1]);

                        Environment.mLogger = new Logger();
                        if (Environment.mLogger.bootstrap() == false) {
                            return false;
                        } else {
                            Environment.mLogger.enable();
                            Environment.printOutBootInfo("System logging interface has been successfully initialized.");
                        }


                        Environment.mThreadController = new ThreadController();
                        Environment.printOutBootInfo("ThreadController has been successfully initialized with: " + (Properties.THREADING_GENERAL_POOL_SIZE + Properties.THREADING_IO_POOL_SIZE + Properties.THREADING_PACKET_POOL_SIZE)
                                + " executors and " + (Properties.THREADING_AI_SCHEDULER_SIZE + Properties.THREADING_GENERAL_SCHEDULER_SIZE) + " schedulers.");

                        Environment.mClientCommunicationController = new ClientCommunicationController(listenerClasz[0], Properties.PROXY_NIO_MASTER_PORT);
                        if (Environment.mClientCommunicationController.bootstrap() == false) {
                            return false;
                        } else {
                            Environment.mClientCommunicationController.initializeNetworking();
                            Environment.printOutBootInfo("ClientCommunicationController has been successfully initialized.");
                        }

                        Environment.mCommunicationController = new CommunicationController(listenerClasz[1], Properties.PROXY_NIO_SERVER_PORT);
                        if (Environment.mCommunicationController.bootstrap() == false) {
                            return false;
                        } else {
                            Environment.mCommunicationController.initializeNetworking();
                            Environment.printOutBootInfo("CommunicationController has been successfully initialized with: " + CommunicationBootstrap.BOSS_POOL_SIZE + " i/o boss and " + CommunicationBootstrap.WORKER_POOL_SIZE + " i/o workers.");
                        }

                        // All is initialized... so start runtime monitors!
                        Environment.mGarbageController = new GarbageController();
                        Environment.mRuntimeController = new RuntimeController();

                        Environment.printOutBootInfo("Project Smoke Proxy has been initialized in " + (Environment.traceMilliTime() - Environment.START_UP_TIME) / 1000.0d + " seconds!");
                        System.out.println();

                        // All right
                        return true;
                    }
                } catch (Exception ex) {
                    Environment.printOutBootError("Environment.bootstrap() has thrown an exception while bootstrapping!", ex);
                }
            }
            case CHAT: {
                try {
                    // Track booting time
                    Environment.START_UP_TIME =
                            Environment.traceMilliTime();

                    // PrintOut some shoutouts
                    Environment.printOutBootBanner(serverType);

                    Environment.mPropertiesBox = new PropertiesBox();
                    String[] propertiez = properties.split(";"), listenerClasz = listenerClass.split(";");

                    if (Environment.mPropertiesBox.bootstrap(propertiez[0]) && Environment.mPropertiesBox.bootstrap(propertiez[1])) {
                        Environment.printOutBootInfo("Loaded " + Environment.mPropertiesBox.size() + " properties from " + propertiez[0] + " and " + propertiez[1]);

                        Environment.mLogger = new Logger();
                        if (Environment.mLogger.bootstrap() == false) {
                            return false;
                        } else {
                            Environment.mLogger.enable();
                            Environment.printOutBootInfo("System logging interface has been successfully initialized.");
                        }


                        Environment.mThreadController = new ThreadController();
                        Environment.printOutBootInfo("ThreadController has been successfully initialized with: " + (Properties.THREADING_GENERAL_POOL_SIZE + Properties.THREADING_IO_POOL_SIZE + Properties.THREADING_PACKET_POOL_SIZE)
                                + " executors and " + (Properties.THREADING_AI_SCHEDULER_SIZE + Properties.THREADING_GENERAL_SCHEDULER_SIZE) + " schedulers.");

                        Environment.mClientCommunicationController = new ClientCommunicationController(listenerClasz[0], Properties.CHAT_NIO_MASTER_PORT);
                        if (Environment.mClientCommunicationController.bootstrap() == false) {
                            return false;
                        } else {
                            Environment.mClientCommunicationController.initializeNetworking();
                            Environment.printOutBootInfo("ClientCommunicationController has been successfully initialized.");
                        }

                        Environment.mCommunicationController = new CommunicationController(listenerClasz[1], Properties.CHAT_NIO_SERVER_PORT);
                        if (Environment.mCommunicationController.bootstrap() == false) {
                            return false;
                        } else {
                            Environment.mCommunicationController.initializeNetworking();
                            Environment.printOutBootInfo("CommunicationController has been successfully initialized with: " + CommunicationBootstrap.BOSS_POOL_SIZE + " i/o boss and " + CommunicationBootstrap.WORKER_POOL_SIZE + " i/o workers.");
                        }

                        // All is initialized... so start runtime monitors!
                        Environment.mGarbageController = new GarbageController();
                        Environment.mRuntimeController = new RuntimeController();

                        Environment.printOutBootInfo("Project Smoke Proxy has been initialized in " + (Environment.traceMilliTime() - Environment.START_UP_TIME) / 1000.0d + " seconds!");
                        System.out.println();

                        // All right
                        return true;
                    }
                } catch (Exception ex) {
                    Environment.printOutBootError("Environment.bootstrap() has thrown an exception while bootstrapping!", ex);
                }
            }

            // W00t!?
            return false;
        }
    }
}
