package com.erban.common.util;

/**
 * @file KLog.java
 * @brief This file is part of the Utils module of KBrowser project. \n
 *            This file serves as "java" source file that presents common log 
 *            utilities that would be required by all of the modules. \n
 *
 * @author zhouchenguang
 * @since 1.0.0.0
 * @version 1.0.0.0
 * @date 2012-12-10
 *
 * \if TOSPLATFORM_CONFIDENTIAL_PROPRIETARY
 * ============================================================================\n
 *\n
 *           Copyright (c) 2012 zhouchenguang.  All Rights Reserved.\n
 *\n
 * ============================================================================\n
 *\n
 *                              Update History\n
 *\n
 * Author (Name[WorkID]) | Modification | Tracked Id | Description\n
 * --------------------- | ------------ | ---------- | ------------------------\n
 * zhouchenguang[7897]   |  2012-12-10  | <xxxxxxxx> | Initial Created.\n
 *\n
 * \endif
 *
 * <tt>
 *\n
 * Release History:\n
 *\n
 * Author (Name[WorkID]) | ModifyDate | Version | Description \n
 * --------------------- | ---------- | ------- | -----------------------------\n
 * zhouchenguang[7897]   | 2012-12-10 | 1.0.0.0 | Initial created. \n
 *\n
 * </tt>
 */
//=============================================================================
//                                IMPORT PACKAGES
//=============================================================================

import android.util.Log;

import com.erban.common.device.AppEnvUtils;

//=============================================================================
//                               CLASS DEFINITIONS
//=============================================================================

/**
 * @author zhouchenguang
 * @version 1.0.0.0
 * @class KLog
 * @brief Class for present common log utilities. \n
 * @date 2012-12-10
 * @par Applied: External
 * @since 1.0.0.0
 */
public class KLog {
    // -------------------------------------------------------------------------
    // Public static members
    // -------------------------------------------------------------------------
    /**
     * @brief Log priority of none.
     */
    public final static int PRIORITY_NONE = 0xFFFF;

    /**
     * @brief Log priority of verbose.
     */
    public final static int PRIORITY_VERBOSE = 2;
    /**
     * @brief Log priority of debug.
     */
    public final static int PRIORITY_DEBUG = 3;
    /**
     * @brief Log priority of info.
     */
    public final static int PRIORITY_INFO = 4;
    /**
     * @brief Log priority of warning.
     */
    public final static int PRIORITY_WARN = 5;
    /**
     * @brief Log priority of error.
     */
    public final static int PRIORITY_ERROR = 6;
    /**
     * @brief Log priority of exception.
     */
    public final static int PRIORITY_ASSERT = 7;

    /**
     * @brief Result id for succeed.
     */
    public final static int RESULT_SUCCESS = 0;

    public static boolean isDebug() {
        return msLogPriority <= PRIORITY_DEBUG;
    }
    // -------------------------------------------------------------------------
    // Private static members
    // -------------------------------------------------------------------------
    /**
     * @brief Log tag.
     */
    private final static String TAG = "NEWS_SDK: ";

    /**
     * 打印调试级别
     */
    private static int msLogPriority = PRIORITY_VERBOSE;// default

    /**
     * 服务端设置的release开关默认值，add by caisenchuan
     */
    private static boolean mServerReleaseLogSwitch = false;

    /**
     * Release下是否打印调试，add by caisenchuan
     */
    private static boolean mReleaseLogEnable = false;

    static {
        setLogPriority(getDefaultLogPriority());
    }

    static public int getDefaultLogPriority() {
        return AppEnvUtils.DEBUG ? PRIORITY_VERBOSE : PRIORITY_NONE;
        //return PRIORITY_VERBOSE;
    }

    public static boolean isLogEnable(int priority) {
        return (msLogPriority <= priority);
    }

    // -------------------------------------------------------------------------
    // Public static member methods
    // -------------------------------------------------------------------------
    public static int getLogPriority() {
        return msLogPriority;
    }

    public static void setLogPriority(int priority) {
        msLogPriority = priority;
    }

    // -------------------------------------------------------------------------

    /**
     * @param tag     log tag that mentioned module name.\n
     * @param message log info.\n
     * @return Result.
     * @brief Log for debug info.
     * @par Sync (or) Async: This is a Synchronous function.
     * @author zhouchenguang
     * @version 1.0.0.0
     * @par Prospective Clients: External Classes
     * @since 1.0.0.0
     */
    public static int d(String tag,
                        String message) {
        if (msLogPriority <= PRIORITY_DEBUG) {
            if (message == null) {
                message = "";
            }
            return Log.d(TAG + tag, message);
        } else {
            return RESULT_SUCCESS;
        }
    }

    public static int d(String tag,
                        String message,
                        Throwable tr) {
        if (msLogPriority <= PRIORITY_DEBUG) {
            if (message == null) {
                message = "";
            }
            return Log.d(TAG + tag, message, tr);
        } else {
            return RESULT_SUCCESS;
        }
    }

    /**
     * 防止先计算后输出，定义可变参的log方法，在关闭debug属性后，可以提高效率
     *
     * @param tag
     * @param format
     * @param args
     * @return
     */
    public static int d(String tag,
                        String format,
                        Object... args) {
        if (msLogPriority <= PRIORITY_DEBUG) {
            String msg = String.format(format, args);
            return Log.d(TAG + tag, msg);
        } else {
            return RESULT_SUCCESS;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * @param tag     log tag that mentioned module name.\n
     * @param e       exception to be shown.\n
     * @param message log info.\n
     * @return KResult.
     * @brief Log for error info.
     * @par Sync (or) Async: This is a Synchronous function.
     * @author zhouchenguang
     * @version 1.0.0.0
     * @par Prospective Clients: External Classes
     * @since 1.0.0.0
     */
    public static int exception(String tag,
                                Exception e,
                                String message) {
        if (msLogPriority <= PRIORITY_ERROR) {
            if (message == null) {
                message = "";
            }
            return Log.e(TAG + tag, message, e);
        } else {
            return RESULT_SUCCESS;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * @param tag     log tag that mentioned module name.\n
     * @param message log info.\n
     * @return KResult.
     * @brief Log for error info.
     * @par Sync (or) Async: This is a Synchronous function.
     * @author zhouchenguang
     * @version 1.0.0.0
     * @par Prospective Clients: External Classes
     * @since 1.0.0.0
     */
    public static int e(String tag,
                        String message) {
        if (msLogPriority <= PRIORITY_ERROR) {
            if (message == null) {
                message = "";
            }
            return Log.e(TAG + tag, message);
        } else {
            return RESULT_SUCCESS;
        }
    }

    /**
     * 打印错误日志，防止先计算后输出，定义可变参的log方法，在关闭debug属性后，可以提高效率
     *
     * @param tag
     * @param format
     * @param args
     * @return
     * @author caisenchuan
     */
    public static int e(String tag,
                        String format,
                        Object... args) {
        if (msLogPriority <= PRIORITY_ERROR) {
            String msg = String.format(format, args);
            return Log.e(TAG + tag, msg);
        } else {
            return RESULT_SUCCESS;
        }
    }

    public static int e(String tag,
                        String message,
                        Throwable tr) {
        if (msLogPriority <= PRIORITY_ERROR) {
            if (message == null) {
                message = "";
            }
            return Log.e(TAG + tag, message, tr);
        } else {
            return RESULT_SUCCESS;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * @param tag     log tag that mentioned module name.\n
     * @param message log info.\n
     * @return KResult.
     * @brief Log for info info.
     * @par Sync (or) Async: This is a Synchronous function.
     * @author zhouchenguang
     * @version 1.0.0.0
     * @par Prospective Clients: External Classes
     * @since 1.0.0.0
     */
    public static int i(String tag,
                        String message) {
        if (msLogPriority <= PRIORITY_INFO) {
            if (message == null) {
                message = "";
            }
            return Log.i(TAG + tag, message);
        } else {
            return RESULT_SUCCESS;
        }
    }

    /**
     * 防止先计算后输出，定义可变参的log方法，在关闭debug属性后，可以提高效率
     *
     * @param tag
     * @param format
     * @param args
     * @return
     */
    public static int i(String tag,
                        String format,
                        Object... args) {
        if (msLogPriority <= PRIORITY_INFO) {
            String msg = String.format(format, args);
            return Log.d(TAG + tag, msg);
        } else {
            return RESULT_SUCCESS;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * @param tag     log tag that mentioned module name.\n
     * @param message log info.\n
     * @return KResult.
     * @brief Log for warning info.
     * @par Sync (or) Async: This is a Synchronous function.
     * @author zhouchenguang
     * @version 1.0.0.0
     * @par Prospective Clients: External Classes
     * @since 1.0.0.0
     */
    public static int w(String tag,
                        String message) {
        if (msLogPriority <= PRIORITY_WARN) {
            if (message == null) {
                message = "";
            }
            return Log.w(TAG + tag, message);
        } else {
            return RESULT_SUCCESS;
        }
    }

    /**
     * 防止先计算后输出，定义可变参的log方法，在关闭debug属性后，可以提高效率
     *
     * @param tag
     * @param format
     * @param args
     * @return
     */
    public static int w(String tag,
                        String format,
                        Object... args) {
        if (msLogPriority <= PRIORITY_WARN) {
            String msg = String.format(format, args);
            return Log.w(TAG + tag, msg);
        } else {
            return RESULT_SUCCESS;
        }
    }

    /**
     * 打印警告，防止先计算后输出，定义可变参的log方法，在关闭debug属性后，可以提高效率
     *
     * @param tag
     * @param message
     * @param tr
     * @return
     */
    public static int w(String tag,
                        String message,
                        Throwable tr) {
        if (msLogPriority <= PRIORITY_WARN) {
            if (message == null) {
                message = "";
            }
            return Log.w(TAG + tag, message, tr);
        } else {
            return RESULT_SUCCESS;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * @param tag     log tag that mentioned module name.\n
     * @param message log info.\n
     * @return KResult.
     * @brief Log for verbose info.
     * @par Sync (or) Async: This is a Synchronous function.
     * @author zhouchenguang
     * @version 1.0.0.0
     * @par Prospective Clients: External Classes
     * @since 1.0.0.0
     */
    public static int v(String tag,
                        String message) {
        if (msLogPriority <= PRIORITY_VERBOSE) {
            if (message == null) {
                message = "";
            }
            return Log.v(TAG + tag, message);
        } else {
            return RESULT_SUCCESS;
        }
    }

    /**
     * 打印VERBOSE
     */
    public static int v(String tag,
                        String format,
                        Object... args) {
        if (msLogPriority <= PRIORITY_VERBOSE) {
            String msg = String.format(format, args);
            return Log.v(TAG + tag, msg);
        } else {
            return RESULT_SUCCESS;
        }
    }

    /**
     * 打印VERBOSE
     */
    public static int v(String tag,
                        String message,
                        Throwable tr) {
        if (msLogPriority <= PRIORITY_VERBOSE) {
            if (message == null) {
                message = "";
            }
            return Log.v(TAG + tag, message, tr);
        } else {
            return RESULT_SUCCESS;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * @param priority indicate log priority.\n
     * @param tag      log tag that mentioned module name.\n
     * @param message  log info.\n
     * @return KResult.
     * @brief Log for indicate priority.
     * @par Sync (or) Async: This is a Synchronous function.
     * @author zhouchenguang
     * @version 1.0.0.0
     * @par Prospective Clients: External Classes
     * @since 1.0.0.0
     */
    public static int println(int priority,
                              String tag,
                              String message) {
        if (message == null) {
            message = "";
        }
        return Log.println(priority, TAG + tag, message);
    }


    /**
     * 初始化KLog
     *
     * @param logEnable
     */
    public static void initialize(boolean logEnable,
                                  boolean serverLogSwitch) {
        mReleaseLogEnable = logEnable;
        mServerReleaseLogSwitch = serverLogSwitch;
        resetLogPriority();
    }


    public static boolean setReleaseLogEnable(boolean enable) {
        mReleaseLogEnable = enable;
        resetLogPriority();
        return mReleaseLogEnable;
    }

    public static boolean getReleaseLogEnable() {
        return mReleaseLogEnable;
    }

    /**
     * 重置打印调试级别
     *
     * @return 新的打印调试级别
     * @author caisenchuan
     */
    public static int resetLogPriority() {
        int pri = getLogPriority_s();
        msLogPriority = pri;
        return pri;
    }

    /**
     * 获取打印调试级别
     *
     * @return
     * @author caisenchuan
     */
    public static int getLogPriority_s() {
        int ret = PRIORITY_NONE;

        if (AppEnvUtils.DEBUG) {
            //Debug版
            ret = PRIORITY_VERBOSE;
        } else {
            //Release版
            if (mReleaseLogEnable || mServerReleaseLogSwitch) {
                //日志打开
                ret = PRIORITY_VERBOSE;
            } else {
                //日志关闭
                ret = PRIORITY_NONE;
            }
        }

        d(TAG, "getLogPriority_s : %s", ret);

        return ret;
    }

}
