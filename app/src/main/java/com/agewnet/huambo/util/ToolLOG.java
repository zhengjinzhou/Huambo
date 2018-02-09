package com.agewnet.huambo.util;


import com.orhanobut.logger.Logger;

/**
 * Created by Dumpling on 2017/10/12.
 * 日志打印工具类
 */

public class ToolLOG {
    /**
     * Debug
     *
     * @param log
     */
    public static void D(String log) {
        //Logger.d(log);
    }
    /**
     * Debug
     *
     * @param log
     */
    public static void d(String log) {
        //Logger.d(log);
    }
    /**
     * Verbose
     *
     * @param log
     */
    public static void V(String log) {
        //Logger.v(log);
    }
    /**
     * ERRORcwq
     *
     * @param log
     */
    public static void E(String log) {
        Logger.e(log);
    }
    /**
     * ERROR
     *
     * @param log
     */
    public static void e(String log) {
        // Logger.e(log);
    }
    /**
     * Info
     *
     * @param log
     */
    public static void I(String log) {
        // Logger.i(log);
    }
    /**
     * JSON
     *
     * @param json
     */
    public static void JSON(String json) {
        // Logger.json(json);
    }
}
