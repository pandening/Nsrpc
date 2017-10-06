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
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by HuJian on 2017/10/4.
 *
 * the rpc deCoder of this rpc framework
 */
public class RpcDecoder extends ByteToMessageDecoder{
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(RpcDecoder.class.getName());

    private Class<?> clazz; // the class
    private RpcCodec codec; // the codec

    /**
     * using default codec: Protostuff
     * @param clazz the class
     */
    public RpcDecoder(Class<?> clazz) {
        this(clazz, ProtostuffCodec.SharedProtostuffCodecHolder.PROTOSTUFF_CODEC);

        NPC_LOGGER.warn("Using default Codec: Protostuff");
    }

    public RpcDecoder(Class<?> clazz, RpcCodec codec) {
        this.clazz = clazz;
        this.codec = codec;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
            throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        Object obj = codec.deCode(data, clazz);

        list.add(obj);
    }
}
