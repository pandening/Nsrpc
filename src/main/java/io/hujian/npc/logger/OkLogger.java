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

/**
 * Created by HuJian on 2017/10/2.
 *
 * logger
 */
public interface OkLogger {

    /**
     * Logs a message object with the debug level.
     *
     * @param message
     *            the message object to log.
     */
    void debug(Object message);

    /**
     * Logs a message at the debug  level including the stack
     * trace of the {@link Throwable}.
     *
     * @param message
     *            the message to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void debug(Object message, Throwable t);

    /**
     * Logs a message object with the debug level.
     *
     * @param message
     *            the message string to log.
     */
    void debug(String message);

    /**
     * Logs a message at the debug level including the stack
     * trace of the {@link Throwable} passed as parameter.
     *
     * @param message
     *            the message to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void debug(String message, Throwable t);

    /**
     * Logs a message object with the  ERROR level.
     *
     * @param message
     *            the message object to log.
     */
    void error(Object message);

    /**
     * Logs a message at the  ERROR level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void error(Object message, Throwable t);

    /**
     * Logs a message object with the  ERROR level.
     *
     * @param message
     *            the message string to log.
     */
    void error(String message);

    /**
     * Logs a message at the  ERROR level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void error(String message, Throwable t);

    /**
     * Logs a message object with the  FATAL level.
     *
     * @param message
     *            the message object to log.
     */
    void fatal(Object message);

    /**
     * Logs a message at the  FATAL level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void fatal(Object message, Throwable t);

    /**
     * Logs a message object with the  FATAL level.
     *
     * @param message
     *            the message string to log.
     */
    void fatal(String message);

    /**
     * Logs a message at the  FATAL level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void fatal(String message, Throwable t);

    /**
     * Gets the logger name.
     *
     * @return the logger name.
     */
    String getName();

    /**
     * Logs a message object with the INFO level.
     *
     * @param message
     *            the message object to log.
     */
    void info(Object message);

    /**
     * Logs a message at the INFO level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void info(Object message, Throwable t);

    /**
     * Logs a message object with the INFO level.
     *
     * @param message
     *            the message string to log.
     */
    void info(String message);

    /**
     * Logs a message at the INFO level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void info(String message, Throwable t);

    /**
     * Checks whether this Logger is enabled for the
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level ,
     *         {@code false} otherwise.
     */
    boolean isDebugEnabled();

    /**
     * Checks whether this Logger is enabled for the  ERROR}
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level
     *          ERROR}, {@code false} otherwise.
     */
    boolean isErrorEnabled();

    /**
     * Checks whether this Logger is enabled for the  FATAL
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level
     *          FATAL, {@code false} otherwise.
     */
    boolean isFatalEnabled();

    /**
     * Checks whether this Logger is enabled for the INFO
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level INFO,
     *         {@code false} otherwise.
     */
    boolean isInfoEnabled();

    /**
     * Checks whether this Logger is enabled for the TRACE
     * level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level TRACE,
     *         {@code false} otherwise.
     */
    boolean isTraceEnabled();

    /**
     * Checks whether this Logger is enabled for the WARN
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level
     *         WARN, {@code false} otherwise.
     */
    boolean isWarnEnabled();

    /**
     * Logs a message object with the TRACE level.
     *
     * @param message
     *            the message object to log.
     */
    void trace(Object message);

    /**
     * Logs a message at the TRACE level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     * @see #debug(String)
     */
    void trace(Object message, Throwable t);

    /**
     * Logs a message object with the TRACE level.
     *
     * @param message
     *            the message string to log.
     */
    void trace(String message);

    /**
     * Logs a message at the TRACE level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     * @see #debug(String)
     */
    void trace(String message, Throwable t);

    /**
     * Logs a message object with the WARN level.
     *
     * @param message
     *            the message object to log.
     */
    void warn(Object message);

    /**
     * Logs a message at the WARN level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void warn(Object message, Throwable t);

    /**
     * Logs a message object with the WARN level.
     *
     * @param message
     *            the message string to log.
     */
    void warn(String message);

    /**
     * Logs a message at the WARN level including the stack
     * trace of the {@link Throwable}  passed as parameter.
     *
     * @param message
     *            the message object to log.
     * @param t
     *            the exception to log, including its stack trace.
     */
    void warn(String message, Throwable t);

}
