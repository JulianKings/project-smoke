package com.smoke.core;

public class Main {
    public static void main(String[] args)
    {
        // sup
        Environment.bootstrap(args[0], ServerType.MASTER, "com.smoke.communication.events.EventResolver");
    }
}
