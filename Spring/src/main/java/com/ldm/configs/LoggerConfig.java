package com.ldm.configs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerConfig {

    // Tạo logger mặc định (có thể dùng tên global hoặc của lớp này)
    private static final Logger logger = LoggerFactory.getLogger("GLOBAL_LOGGER");

    // Info log
    public static void info(String msg, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(msg, args);
        }
    }

    // Debug log
    public static void debug(String msg, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg, args);
        }
    }

    // Warn log
    public static void warn(String msg, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(msg, args);
        }
    }

    // Error log
    public static void error(String msg, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(msg, args);
        }
    }

    // Error log kèm exception
    public static void error(String msg, Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(msg, t);
        }
    }
}