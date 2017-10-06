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

import io.hujian.npc.logger.NpcLogger;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.util.Map;

/**
 * Created by HuJian on 2017/10/4.
 *
 * Server Handler
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final NpcLogger LOGGER = NpcLogger.getLogger(RpcServerHandler.class.getName());

    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final RpcRequest request) throws Exception {
        RpcServer.submit(() -> {

            LOGGER.error("Receive request " + request.getRequestId());

            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());

            try {
                Object result = handle(request);
                response.setResult(result);
            } catch (Throwable t) {
                response.setError(t.toString());
                LOGGER.error("RPC Server handle request error", t);
            }

            //write
            ctx.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture ->
                    LOGGER.warn("Send response for request " + request.getRequestId() +
                            " Result:" + response));
        });
    }

    private Object handle(RpcRequest request) throws Throwable {

        LOGGER.warn("Start to handler the request:" + request);

        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // Cglib reflect
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);

        Object result = serviceFastMethod.invoke(serviceBean, parameters);

        LOGGER.error("invoke the method:" + methodName + " result:" + result);

        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);

        ctx.close();
    }
}
