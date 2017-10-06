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

/**
 * Created by HuJian on 2017/10/4.
 *
 * the codec of this rpc framework
 */
public interface RpcCodec {

    /**
     * enCode the object to byte array
     * @param o the origin object
     * @return the byte array
     */
    <T> byte[] enCode(T o);

    /**
     * decode object from byte array.
     * @param bytes the origin byte array
     * @param clazz the class
     * @return object from byte array
     */
    <T> Object deCode(byte[] bytes, Class<T> clazz);

}
