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

package io.hujian.npc.pubisher;

import java.util.Objects;

/**
 * Created by HuJian on 2017/10/4.
 *
 * the callType
 */
public enum CallType {
    SYNC(0, "sync"),  // sync default
    FUTURE(1, "future"), // future
    CALLBACK(2, "callback"); // callback

    CallType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    private int id;
    private String desc;

    /**
     * get the desc according to the index
     * @param index the index
     * @return the desc
     */
    public static String getDesc(int index) {
        for (SerializeType c : SerializeType.values()) {
            if (c.getId() == index) {
                return c.getDesc();
            }
        }
        return null;
    }

    /**
     * get the index according to the desc
     * @param desc the desc
     * @return the index
     */
    public static int getIndex(String desc) {
        for (SerializeType c : SerializeType.values()) {
            if (Objects.equals(c.getDesc(), desc)) {
                return c.getId();
            }
        }
        return -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
