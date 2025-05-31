package com.ldm.configs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerConfig {

    private static final Logger logger = LoggerFactory.getLogger("GLOBAL_LOGGER");

    public static void info(String msg, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(msg, args);
        }
    }

    public static void debug(String msg, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg, args);
        }
    }

    public static void warn(String msg, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(msg, args);
        }
    }

    public static void error(String msg, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(msg, args);
        }
    }

    public static void error(String msg, Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(msg, t);
        }
    }
}