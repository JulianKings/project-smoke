package com.smoke.settings;

import com.smoke.core.Environment;

public class Properties {

    // Scheduler Intervals
    public static final long SESSION_CHECK_INTERVAL = 0x7530l;
    public static final long SCHEDULER_PURGATION_INTERVAL = 0xea60l;

    // Network
    public static final short NIO_SERVER_PORT = Environment.getProperties().getShort("nio.server.port", (short) 10477);
    public static final String NIO_RCON_WHITELIST = Environment.getProperties().get("nio.rcon.whitelist", "127.0.0.1");

    // Logging
    public static final boolean TRACK_TRACE = Environment.getProperties().getBoolean("logger.track.trace", false);
    public static final boolean TRACK_DEBUG = Environment.getProperties().getBoolean("logger.track.debug", false);
    public static final boolean TRACK_WARNING = Environment.getProperties().getBoolean("logger.track.warning", false);
    public static final boolean TRACK_CRITICAL = Environment.getProperties().getBoolean("logger.track.critical", false);
    public static final boolean MESSAGE_REQUEST_PROFILING = Environment.getProperties().getBoolean("profiling.message.requests", false);
    public static final boolean MESSAGE_RESPONSE_PROFILING = Environment.getProperties().getBoolean("profiling.message.responses", false);

    // Threading
    public static final short THREADING_GENERAL_SCHEDULER_SIZE = Environment.getProperties().getShort("threading.general.scheduler.size", (short) 50);
    public static final short THREADING_IO_POOL_SIZE = Environment.getProperties().getShort("threading.io.pool.size", (short) 15);
    public static final short THREADING_PACKET_POOL_SIZE = Environment.getProperties().getShort("threading.packet.pool.size", (short) 50);
    public static final short THREADING_GENERAL_POOL_SIZE = Environment.getProperties().getShort("threading.general.pool.size", (short) 20);
    public static final short THREADING_AI_SCHEDULER_SIZE = Environment.getProperties().getShort("threading.ai.scheduler.size", (short) 15);

    // Network System
    public static final short DISPATCHER_QUEUE_LIMIT = Environment.getProperties().getShort("dispatcher.queue.limit", (short) 5000);
    public static final short DISPATCHER_EXECUTOR_SIZE = Environment.getProperties().getShort("dispatcher.executor.size", (short) 2);

    // Proxy
    public static final short PROXY_NIO_SERVER_PORT = Environment.getProperties().getShort("proxy.nio.server.port", (short)10433);
    public static final short PROXY_NIO_MASTER_PORT = Environment.getProperties().getShort("proxy.nio.master.port", (short)10377);

    // Proxy
    public static final short CHAT_NIO_SERVER_PORT = Environment.getProperties().getShort("chat.nio.server.port", (short)10434);
    public static final short CHAT_NIO_MASTER_PORT = Environment.getProperties().getShort("chat.nio.master.port", (short)10378);

}
