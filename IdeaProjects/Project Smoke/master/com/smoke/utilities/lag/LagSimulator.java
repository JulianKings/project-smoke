package com.smoke.utilities.lag;

import com.smoke.core.Environment;
import com.smoke.logging.LogLevel;

public class LagSimulator {

    public static void simulate(long milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }

        catch (InterruptedException ex)
        {
            Environment.getLogger().printOut(LogLevel.CRITICAL, "LagSimulator.simulate(" + milliseconds + ") has thrown an exception.", ex);
        }
    }
}
