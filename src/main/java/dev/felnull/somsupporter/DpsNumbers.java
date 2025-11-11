package dev.felnull.somsupporter;

import dev.felnull.somsupporter.listener.ClientEvents;

public class DpsNumbers {
    public static double getWindowDps()   { return ClientEvents.getWindowDps(); }
    public static double getSessionDps()  { return ClientEvents.getSessionDps(); }
    public static double getSessionTotal(){ return ClientEvents.getSessionTotal(); }
    public static void   resetSession()   { ClientEvents.resetSession(); }
}