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

package io.hujian.npc.codec;

import io.hujian.npc.logger.NpcLogger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by HuJian on 2017/10/4.
 *
 * The EnCoder of this rpc framework
 */
public class RpcEnCoder extends MessageToByteEncoder {
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(RpcEnCoder.class.getName());

    private Class<?> clazz;
    private RpcCodec rpcCodec; // the rpc codec

    public RpcEnCoder(Class<?> cls, RpcCodec rpcCodec) {
        this.clazz = cls;
        this.rpcCodec = rpcCodec;
    }

    /**
     * using Protostuff default.
     * @param cls the class.
     */
    public RpcEnCoder(Class<?> cls) {
        this(cls, ProtostuffCodec.SharedProtostuffCodecHolder.PROTOSTUFF_CODEC);

        NPC_LOGGER.warn("Using Default EnCoder : Protostuff");
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf)
            throws Exception {
        if (clazz.isInstance(o)) {
            byte[] bytes = rpcCodec.enCode(o);

            NPC_LOGGER.debug("Success to encode the object:" + o + " to byte array:" + bytes);

            /* length:bytes  */
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);

            NPC_LOGGER.debug("Success to write the bytes to byteBuf:" + bytes);
        }

    }
}
