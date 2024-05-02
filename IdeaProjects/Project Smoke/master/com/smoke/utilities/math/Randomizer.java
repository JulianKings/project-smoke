package com.smoke.utilities.math;

import com.smoke.core.Environment;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    public static Random RANDOM;

    static {
        Randomizer.RANDOM = new Random(
            Environment.traceNanoTime() ^ Environment.traceMilliTime()
        );
    }

    public static int nextInt()
    {
        return ThreadLocalRandom.current().nextInt();
    }

    public static double nextDouble()
    {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static int nextInt(int minimum, int maximum)
    {
        return Math.abs(minimum + (ThreadLocalRandom.current().nextInt()  % ( (maximum - minimum) + 1) ) );
    }

    public static int nextIntInRange(int minimum, int maximum)
    {
        return ThreadLocalRandom.current().nextInt(minimum, (maximum + Numbers.ONE));
    }
}
