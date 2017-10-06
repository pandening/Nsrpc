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

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import io.hujian.npc.logger.NpcLogger;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HuJian on 2017/10/4.
 *
 * the  Protostuff codec implementation.
 */
public class ProtostuffCodec implements RpcCodec{
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(ProtostuffCodec.class.getName());

    //the shared ProtostuffCodec.
    public static class SharedProtostuffCodecHolder {
        public static final ProtostuffCodec PROTOSTUFF_CODEC = new ProtostuffCodec();
    }

    //the schema cache
    private static Map<Class<?>, Schema<?>> classSchemaMapCache = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    /**
     * get the class's schema, using cache first.
     * @param cls the class
     * @param <T> type
     * @return the schema
     */
    @SuppressWarnings(value = "unchecked")
    private <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) classSchemaMapCache.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            classSchemaMapCache.put(cls, schema);
        }

        NPC_LOGGER.debug("get schema of class:" + cls + " : " + schema);

        return schema;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public <T> byte[] enCode(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {

            NPC_LOGGER.debug("Try to get schema of class:" + cls);

            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {

            NPC_LOGGER.error("Could not get the schema of class:" + cls, e);

            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public <T> Object deCode(byte[] bytes, Class<T> clazz) {
        try {
            T message = (T) objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);

            NPC_LOGGER.debug("decode ok:" + message);

            return message;
        } catch (Exception e) {

            NPC_LOGGER.error("Could not decode:" + clazz + ":" + new String(bytes), e);

            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
