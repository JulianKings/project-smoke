package com.smoke.communication.modules;

import com.smoke.communication.ChannelGroup;
import com.smoke.communication.protocol.MessageResponse;
import com.smoke.core.Environment;
import com.smoke.logging.LogLevel;
import com.smoke.utilities.math.Numbers;
import com.smoke.utilities.memory.IDisposable;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class ModuleController implements Runnable, IDisposable {

    private List<Module> lJunk;
    private AtomicInteger iPlayers;
    private ScheduledFuture mScheduler;
    private Map<Long, Module> mResolver;
    private Map<Channel, Module> mModules;

    public static ChannelGroup CHANNELS;
    private static final long SESSION_TIMEOUT = 0x7530l;

    static {
        ModuleController.CHANNELS = new ChannelGroup().construct(
            ModuleController.class.getCanonicalName()
        );
    }

    // region #Accessors

    public short getActiveModules()
    {
        return this.iPlayers.shortValue();
    }

    public boolean isOnline(long ID)
    {
        return this.mResolver.containsKey(ID);
    }

    public Module getModule(long ID)
    {
        return this.mResolver.get(ID);
    }

    private Module getModule(Channel channel)
    {
        return this.mModules.get(channel);
    }

    // endregion

    // region #Methods
    private void purge()
    {
        // Recollect all timed out sessions
        for (Module session : this.mModules.values())
        {
            if ((Environment.traceMilliTime() - session.getLastReadTime()) > SESSION_TIMEOUT)
            {
                this.lJunk.add(session);
            }
        }

        // Iterate through all dead sessions
        int amount = 0; for (Module session : this.lJunk)
        {
            session.getChannel().close(); amount++;
        }

        // clear junk list
        this.lJunk.clear();

        // Any removed session?
        if (amount != Numbers.ZERO)
        {
            // Then print out the amount of disposed sessions
            Environment.getLogger().printOut(LogLevel.DEBUG, "ModuleController disconnected " + amount + " timed out sessions successfully.");
        }
    }

    @Override
    public void run()
    {
        this.purge();
    }

    @Override
    public void destruct()
    {
        // Release pointers
        this.lJunk.clear();
        this.mResolver.clear();
        this.mModules.clear();
        this.mScheduler.cancel(true);

        // Point to null
        this.lJunk = null;
        this.iPlayers = null;
        this.mResolver = null;
        this.mModules = null;
        this.mScheduler = null;
    }

    public void markOnline(Module session)
    {

    }

    public void markOffline(Module session)
    {
    }

    public Module addConnection(Channel channel)
    {
        Module session =
        new Module().construct(channel);
        this.mModules.put(channel, session);
        Environment.getLogger().printOut(LogLevel.DEBUG, "Accepted " +
        "connection #" + session.getID() + " from [" + session.getIPAddress() + "]");

        return session;
    }

    public void removeConnection(Channel channel)
    {
        Module session =
        this.getModule(channel);
        this.markOffline(session);
        this.mModules.remove(channel);

        Environment.getLogger().printOut(LogLevel.DEBUG, "Removed " +
        "connection #" + session.getID() + " from [" + session.getIPAddress() + "]");
    }

    public void broadcast(MessageResponse response)
    {
        ModuleController.CHANNELS.writeAndFlush(response.getPayload());
    }

    // endregion

    // region #Constructors
    public ModuleController()
    {
        this.lJunk = new ArrayList<>();
        this.iPlayers = new AtomicInteger();
        this.mResolver = new ConcurrentHashMap<>();
        this.mModules = new ConcurrentHashMap<>();
        /*this.mScheduler = Environment.getThreadController().scheduleAtFixedRate(
            this, ModuleController.SESSION_TIMEOUT, ModuleController.SESSION_TIMEOUT
        );*/
    }
    // endregion
}
