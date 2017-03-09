package org.benmobile.log;

/**
 * Logger is a wrapper of {@link android.util.Log}
 * But more pretty, simple and powerful
 */
public final class Logger {
    private static boolean mIsPrintDebug = false;
    private static boolean mIsPrintError = false;
    private static boolean mIsPrintInfo = false;
    private static boolean mIsPrintVerbose = false;
    private static boolean mIsPrintWarn = false;
    private static boolean mIsPrintAssert = false;
    private static boolean mIsPrintJson = false;
    private static boolean mIsPrintXml = false;
    private static boolean mIsPrintFile = false;
    private static final String DEFAULT_TAG = "YZG---";
    private static Printer printer = new LoggerPrinter();

    //no instance
    private Logger() {
    }
    /**
     * It is used to get the settings object in order to change settings
     *
     * @return the settings object
     */
    public static Settings init() {
        return init(DEFAULT_TAG,true,true,true,true,true,true,true,true,true);
    }

    /**
     * It is used to change the tag
     *
     * @param tag is the given string which will be used in Logger as TAG
     */
    public static Settings init(String tag,boolean mIsPrintD, boolean mIsPrintE, boolean mIsPrintI,
                                boolean mIsPrintV, boolean mIsPrintW,boolean mIsPrintA,boolean mIsPrintJ,boolean mIsPrintX,boolean mIsPrintF) {
        mIsPrintDebug = mIsPrintD;
        mIsPrintError = mIsPrintE;
        mIsPrintInfo = mIsPrintI;
        mIsPrintVerbose = mIsPrintV;
        mIsPrintWarn = mIsPrintW;
        mIsPrintAssert=mIsPrintA;
        mIsPrintJson=mIsPrintJ;
        mIsPrintXml=mIsPrintX;
        mIsPrintFile = mIsPrintF;
        if (null==printer)
        printer = new LoggerPrinter();
        return printer.init(tag);
    }

    public static void clear() {
        printer.clear();
        printer = null;
    }

    public static Printer t(String tag) {
        return printer.t(tag, printer.getSettings().getMethodCount());
    }

    public static Printer t(int methodCount) {
        return printer.t(null, methodCount);
    }

    public static Printer t(String tag, int methodCount) {
        return printer.t(tag, methodCount);
    }

    public static void d(String message, Object... args) {
        if (mIsPrintDebug) {
            printer.d(message, args);
        }
    }

    public static void e(String message, Object... args) {
        if (mIsPrintError) {
            printer.e(null, message, args);
        }
    }

    public static void e(Throwable throwable, String message, Object... args) {
        if (mIsPrintError) {
            printer.e(throwable, message, args);
        }
    }

    public static void i(String message, Object... args) {
        if (mIsPrintInfo) {
            printer.i(message, args);
        }
    }

    public static void v(String message, Object... args) {
        if (mIsPrintVerbose) {
            printer.v(message, args);
        }
    }

    public static void w(String message, Object... args) {
        if (mIsPrintWarn) {
            printer.w(message, args);
        }
    }

    public static void wtf(String message, Object... args) {
        if (mIsPrintAssert) {
            printer.wtf(message, args);
        }
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        if (mIsPrintJson) {
            printer.json(json);
        }
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        if (mIsPrintXml) {
            printer.xml(xml);
        }
    }

}
