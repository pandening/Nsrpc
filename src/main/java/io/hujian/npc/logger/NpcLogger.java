/**
 * Copyright (c) 2017 hujian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hujian.npc.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContext;

import java.net.URL;

/**
 * Created by HuJian on 2017/10/2.
 *
 * the implement of Logger
 */
public class NpcLogger implements OkLogger{

    private volatile static NpcLogger NPC_LOGGER = null; // the final logger

    /**
     * the logger holder
     */
    private static class LoggerHolder {
        private static final NpcLogger NPC_LOGGER = new NpcLogger();
    }

    /**
     * get the logger
     * @return the logger
     */
    public static NpcLogger getLogger() {
        return LoggerHolder.NPC_LOGGER;
    }

    /**
     * get the logger, assign the logger name.
     * @param loggerName the logger name
     * @return the logger
     */
    public static NpcLogger getLogger(String loggerName) {
        if (NPC_LOGGER == null) {
            synchronized (NpcLogger.class) {
                if (NPC_LOGGER == null) {
                    NPC_LOGGER = new NpcLogger(loggerName);
                }
            }
        }

        return NPC_LOGGER;
    }

    private Logger LOG;
    private static volatile boolean isDebugEnabled = false;

    private static LoggerContext context = null;

    public NpcLogger() {
        this(NpcLogger.class.getName());
    }

    public NpcLogger(String loggerName) {
        if (context == null) {
            setUp();
        }
        this.LOG = context.getLogger(loggerName);
    }

    private static synchronized void setUp() {
        if (context == null) {
            URL url = NpcLogger.class.getResource("log4j2.xml");
            LoggerContext ctx;
            if (url == null) {
                ctx = LogManager.getContext(false);
            } else {
                try {
                    ctx = new org.apache.logging.log4j.core.LoggerContext("ok-rpc", null, url.toURI());
                    ((org.apache.logging.log4j.core.LoggerContext) ctx).start();
                } catch (Throwable t) {
                    ctx = LogManager.getContext(false);
                }
            }
            context = ctx;
        }
    }

    @Override
    public void debug(Object message) {
        if (this.isDebugEnabled()) {
            this.LOG.debug(message);
        }
    }

    @Override
    public void debug(Object message, Throwable t) {
        if (this.isDebugEnabled()) {
            this.LOG.debug(message, t);
        }
    }

    @Override
    public void debug(String message) {
        if (this.isDebugEnabled()) {
            this.LOG.debug(message);
        }
    }

    @Override
    public void debug(String message, Throwable t) {
        if (this.isDebugEnabled()) {
            this.LOG.debug(message, t);
        }
    }

    @Override
    public void error(Object message) {
        this.LOG.error(message);
    }

    @Override
    public void error(Object message, Throwable t) {
        this.LOG.error(message, t);
    }

    @Override
    public void error(String message) {
        this.LOG.error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        this.LOG.error(message, t);
    }

    @Override
    public void fatal(Object message) {
        this.LOG.fatal(message);
    }

    @Override
    public void fatal(Object message, Throwable t) {
        this.LOG.fatal(message, t);
    }

    @Override
    public void fatal(String message) {
        this.LOG.fatal(message);
    }

    @Override
    public void fatal(String message, Throwable t) {
        this.LOG.fatal(message, t);
    }

    @Override
    public String getName() {
        return this.LOG.getName();
    }

    @Override
    public void info(Object message) {
        this.LOG.info(message);
    }

    @Override
    public void info(Object message, Throwable t) {
        this.LOG.info(message, t);
    }

    @Override
    public void info(String message) {
        this.LOG.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        this.LOG.info(message, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    public static void setDebugEnabled(boolean enabled) {
        isDebugEnabled = enabled;
    }

    @Override
    public boolean isErrorEnabled() {
        return this.LOG.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return this.LOG.isFatalEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return this.LOG.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.LOG.isTraceEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return this.LOG.isWarnEnabled();
    }

    @Override
    public void trace(Object message) {
        this.LOG.trace(message);
    }

    @Override
    public void trace(Object message, Throwable t) {
        this.LOG.trace(message, t);
    }

    @Override
    public void trace(String message) {
        this.LOG.trace(message);
    }

    @Override
    public void trace(String message, Throwable t) {
        this.LOG.trace(message, t);
    }

    @Override
    public void warn(Object message) {
        this.LOG.warn(message);
    }

    @Override
    public void warn(Object message, Throwable t) {
        this.LOG.warn(message, t);
    }

    @Override
    public void warn(String message) {
        this.LOG.warn(message);
    }

    @Override
    public void warn(String message, Throwable t) {
        this.LOG.warn(message, t);
    }

}
