package com.smoke.utilities.runtime;

import com.smoke.core.Environment;
import com.smoke.logging.LogLevel;
import com.smoke.settings.Properties;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class RuntimeController implements Runnable {

    private Thread tMonitor;
    private OperatingSystemMXBean mBean;

    private volatile int iCPUPeak;
    private volatile int iRAMPeak;
    private volatile int iRAMFree;
    private volatile int iRAMHeap;
    private volatile int iCPUUsage;
    private volatile int iRAMUsage;
    private volatile int iRoomPeak;
    private volatile int iPlayerPeak;
    private volatile int iThreadPeak;
    private volatile int iThreadUsage;
    private volatile int iDaemonThreads;
    private volatile int iIncomingTraffic;
    private volatile int iOutgoingTraffic;
    private volatile int iConcurrentRooms;
    private volatile int iConcurrentPlayers;
    private volatile int iIncomingTrafficPeak;
    private volatile int iOutgoingTrafficPeak;

    private static final long SLEEP_TIME = 1000l;

    public RuntimeController()
    {
        this.tMonitor = new Thread(this);
        this.tMonitor.setPriority(Thread.MIN_PRIORITY);
        this.tMonitor.setName("RUNTIME-MONITOR-THREAD-0");
        this.mBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    private String getUptime()
    {
        // Calc. lapsus
        long lapsus = (
            Environment.traceMilliTime()
          - Environment.START_UP_TIME
        );

        // Calculate Uptime
        long days    = lapsus / 86400000;
        long hours   = lapsus /  3600000;
        long module  = lapsus %  3600000;
        long minutes = module /    60000;

        return  String.format("%d day(s), %d hour(s) and %d minute(s)", days, hours, minutes);
    }

    private void setCPUUsage()
    {
        if (this.iCPUPeak < this.iCPUUsage)
        {
            this.iCPUPeak = this.iCPUUsage;
        }

        this.iCPUUsage = (int) (this.mBean.getProcessCpuLoad() * 10);
    }

    private void setRAMUsage()
    {
        if (this.iRAMPeak < this.iRAMUsage)
        {
            this.iRAMPeak = this.iRAMUsage;
        }

        this.iRAMFree = (int) Runtime.getRuntime().freeMemory() >> 10;
        this.iRAMHeap = (int) Runtime.getRuntime().totalMemory() >> 10;
        this.iRAMUsage = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) >> 10;
    }

    private void setThreadUsage()
    {
        if (this.iThreadPeak < this.iThreadUsage)
        {
            this.iThreadPeak = this.iThreadUsage;
        }

        this.iDaemonThreads = ManagementFactory.getThreadMXBean().getDaemonThreadCount();
        this.iThreadUsage = ManagementFactory.getThreadMXBean().getThreadCount() - this.iDaemonThreads;
    }

    public void setConcurrentRooms(int amount)
    {
        if (this.iRoomPeak < amount)
        {
            this.iRoomPeak = amount;
        }

        this.iConcurrentRooms = amount;
    }

    private void setConcurrentPlayers(int amount)
    {
        if (this.iPlayerPeak < amount)
        {
            this.iPlayerPeak = amount;
        }

        this.iConcurrentPlayers = amount;
    }

    private void setIncomingTraffic(long amount)
    {
        if (this.iIncomingTrafficPeak < amount)
        {
            this.iIncomingTrafficPeak = (int) amount;
        }

        this.iIncomingTraffic = (int) amount;
    }

    private void setOutgoingTraffic(long amount)
    {
        if (this.iOutgoingTrafficPeak < amount)
        {
            this.iOutgoingTrafficPeak = (int) amount;
        }

        this.iOutgoingTraffic = (int) amount;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Thread.sleep(RuntimeController.SLEEP_TIME);
                this.setCPUUsage(); this.setRAMUsage(); this.setThreadUsage();
                this.setConcurrentPlayers(Environment.getCommunication().getModules().getActiveModules());
                this.setIncomingTraffic(Environment.getCommunication().getNetworkProfiler().getIncomingTraffic());
                this.setOutgoingTraffic(Environment.getCommunication().getNetworkProfiler().getOutgoingTraffic());
            }

            catch (Exception ex)
            {
                Environment.getLogger().printOut(LogLevel.CRITICAL, "RuntimeController.run() has thrown an exception!", ex);
            }
        }
    }

    public void stop()
    {
        // TODO...
    }

    public void start()
    {
        this.tMonitor.start();
        Environment.getCommunication().initializeNetworkProfiler();
    }

    public String getStats()
    {
        // INSTANTIATE STRING
        StringBuilder stats = new StringBuilder(320);

        // RUNTIME STATISTICS
        stats.append(String.format("GC Ratio: %.2f%%\n", 0.9f));
        stats.append(String.format("CPU Usage: %d%%\n", this.iCPUUsage));
        stats.append(String.format("RAM Usage: %d MB\n", (this.iRAMUsage >> 10)));
        stats.append(String.format("NET I/O Ratio: %d KB/s\n", (this.iIncomingTraffic >> 10)));
        stats.append(String.format("NET O/I Ratio: %d KB/s\n", (this.iOutgoingTraffic >> 10)));
        //stats.append(String.format("DB CONN. Usage: %d connection(s)\n\n", Environment.getDatabaseController().getActiveConnections()));

        // OS. INFORMATION
        stats.append(String.format("Operating System: %s\n" + System.getProperty("os.name")));
        stats.append(String.format("JVM  Information: %s\n\n" + System.getProperty("java.vm.name")));

        // UPTIME STATISTICS
        stats.append(String.format("Server uptime is %s\n" + this.getUptime()));

        return stats.toString();
    }

    @Override
    public String toString() {
        return
                "<runtime-stats>"							+
                    "<cpu>"									+
                        "<peak>"							+
                            this.iCPUPeak					+
                        "</peak>"							+
                        "<current>"							+
                            this.iCPUUsage					+
                        "</current>"						+
                    "</cpu>"								+
                    "<ram>"									+
                         "<peak>"							+
                            this.iRAMPeak					+
                        "</peak>"							+
                        "<free>" 							+
                            this.iRAMFree					+
                        "</free>"							+
                        "<heap>"							+
                            this.iRAMHeap					+
                        "</heap>"							+
                        "<current>"							+
                            this.iRAMUsage					+
                        "</current>"						+
                    "</ram>"								+
                    "<game>"								+
                        "<rooms>"							+
                            "<peak>"						+
                                this.iRoomPeak				+
                            "</peak>"						+
                            "<current>"						+
                                this.iConcurrentRooms		+
                            "</current>"					+
                        "</rooms>"							+
                        "<players>"							+
                            "<peak>"						+
                                this.iPlayerPeak			+
                            "</peak>"						+
                            "<current>"						+
                                this.iConcurrentPlayers		+
                            "</current>"					+
                        "</players>"						+
                    "</game>"								+
                    "<uptime>"								+
                        Environment.START_UP_TIME	    	+
                    "</uptime>"								+
                    "<traffic>"								+
                        "<incoming>"						+
                            "<peak>"						+
                                this.iIncomingTrafficPeak 	+
                            "</peak>"						+
                            "<current>"						+
                                this.iIncomingTraffic		+
                            "</current>"					+
                        "</incoming>"						+
                        "<outgoing>"						+
                            "<peak>"						+
                                this.iOutgoingTrafficPeak	+
                            "</peak>"						+
                            "<current>"						+
                                this.iOutgoingTraffic		+
                            "</current>"					+
                        "</outgoing>"						+
                    "</traffic>"							+
                    "<threads>"                             +
                        "<peak>"							+
                            this.iThreadPeak				+
                        "</peak>"							+
                        "<daemon>"							+
                            this.iDaemonThreads				+
                        "</daemon>"							+
                        "<current>"							+
                            this.iThreadUsage				+
                        "</current>"						+
                    "</threads>"                            +
                "</runtime-stats>"							;
    }
}