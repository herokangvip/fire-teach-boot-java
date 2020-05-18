package com.example.demo.log;

import org.slf4j.Logger;
import org.slf4j.Marker;

public class TraceLog implements Logger {

    private Logger logger;


    private String createLogMessage(String msg) {
        StringBuilder sb = new StringBuilder();
        String split = "|";
        sb.append(TraceLogHolder.getUuid()).append(split).append(msg);
        return sb.toString();
    }

    public TraceLog(Logger logger) {
        this.logger = logger;
    }


    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        logger.trace(createLogMessage(s));
    }

    @Override
    public void trace(String s, Object o) {
        logger.trace(createLogMessage(s), o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        logger.trace(createLogMessage(s), o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        logger.trace(createLogMessage(s), objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        logger.trace(createLogMessage(s), throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        logger.trace(marker, createLogMessage(s));
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        logger.trace(marker, createLogMessage(s), o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        logger.trace(marker, createLogMessage(s), o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        logger.trace(marker, createLogMessage(s), objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        logger.trace(marker, createLogMessage(s), throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        logger.debug(createLogMessage(s));
    }

    @Override
    public void debug(String s, Object o) {
        logger.debug(createLogMessage(s), o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        logger.debug(createLogMessage(s), o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        logger.debug(createLogMessage(s), objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        logger.debug(createLogMessage(s), throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        logger.debug(marker, createLogMessage(s));
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        logger.debug(marker, createLogMessage(s), o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        logger.debug(marker, createLogMessage(s), o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        logger.debug(marker, createLogMessage(s), objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        logger.debug(marker, createLogMessage(s), throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        logger.info(createLogMessage(s));
    }

    @Override
    public void info(String s, Object o) {
        logger.info(createLogMessage(s), o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        logger.info(createLogMessage(s), o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        logger.info(createLogMessage(s), objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        logger.info(createLogMessage(s), throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        logger.info(marker, createLogMessage(s));
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        logger.info(marker, createLogMessage(s), o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        logger.info(marker, createLogMessage(s), o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        logger.info(marker, createLogMessage(s), objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        logger.info(marker, createLogMessage(s), throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        logger.warn(createLogMessage(s));
    }

    @Override
    public void warn(String s, Object o) {
        logger.warn(createLogMessage(s), o);
    }

    @Override
    public void warn(String s, Object... objects) {
        logger.warn(createLogMessage(s), objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        logger.warn(createLogMessage(s), o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        logger.warn(createLogMessage(s), throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        logger.warn(marker, createLogMessage(s));
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        logger.warn(marker, createLogMessage(s), o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        logger.warn(marker, createLogMessage(s), o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        logger.warn(marker, createLogMessage(s), objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        logger.warn(marker, createLogMessage(s), throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        logger.error(createLogMessage(s));
    }

    @Override
    public void error(String s, Object o) {
        logger.error(createLogMessage(s), o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        logger.error(createLogMessage(s), o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        logger.error(createLogMessage(s), objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        logger.error(createLogMessage(s), throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        logger.error(marker, createLogMessage(s));
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        logger.error(marker, createLogMessage(s), o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        logger.error(marker, createLogMessage(s), o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        logger.error(marker, createLogMessage(s), objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        logger.error(marker, createLogMessage(s), throwable);
    }
}
