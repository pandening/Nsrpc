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

package io.hujian.npc.manager;

import io.hujian.npc.discover.ServiceDiscover;
import io.hujian.npc.logger.NpcLogger;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by HuJian on 2017/10/4.
 *
 * The Rpc client
 */
public class RpcClient {
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(RpcClient.class.getName());

    private String serverAddress; // the server address
    private ServiceDiscover serviceDiscover; // the discover

    //thread exception handler
    private static Thread.UncaughtExceptionHandler rpcClientExceptionHandler =
            (t, e) -> NPC_LOGGER.error("Uncaught Exception from rpcClient. threadName = " + t.getName(), e);

    //thread factory
    private static ThreadFactory rpcClientThreadFactory =
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "RpcClientThreadPool-"
                            + threadNumber.getAndIncrement());
                    t.setUncaughtExceptionHandler(rpcClientExceptionHandler);
                    return t;
                }
            };

    //the thread pool
    private static volatile ExecutorService rpcClientExecutorService =
            Executors.newCachedThreadPool(rpcClientThreadFactory);

    public RpcClient() {

    }

    public RpcClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcClient(ServiceDiscover discover) {
        this.serviceDiscover = discover;
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass)
        );
    }

    public static Object createSimple(Class<Object> interfaceClass) {
        return Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new ObjectProxy<Object>(interfaceClass)
        );
    }

    public static <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass);
    }

    public static void submit(Runnable task){
        rpcClientExecutorService.submit(task);
    }

    public void stop() {
        rpcClientExecutorService.shutdown();
        serviceDiscover.stop();

        ServiceManager.ServiceManagerHolder.SERVICE_MANAGER.stop();
    }
}
