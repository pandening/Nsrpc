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

import io.hujian.npc.logger.NpcLogger;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by HuJian on 2017/10/4.
 *
 * the bean.
 */
@Setter
@Getter
public class RpcServicePublishBean {
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(RpcServicePublishBean.class.getName());

    private String url = ""; // the service url, ensure unique.
    private String interfaceName = ""; // the interface
    private String serialize = ""; // the serialize method.
    private String callType = ""; // the callType
    private int retries = 1; // try time
    private int timeout = 4000; // the timeout value (ms)
    private String version = ""; // version

    private RpcNodeGroup nodeGroup; // the nodeGroup.
    private String loadBalance = ""; // loadBalance

    public RpcServicePublishBean() {
        serialize = SerializeType.PROTOSTUFF.getDesc(); // this is the default
        callType = CallType.SYNC.getDesc(); // sync
        loadBalance = LoadBalanceType.ROUND_ROBIN.getDesc();// round robin.
        retries = 1; // 1 time
        timeout = 4000; // 4s
        nodeGroup = new RpcNodeGroup();

        NPC_LOGGER.info("constructor of RpcServicePublishBean run.");
    }

    @Override
    public String toString() {
        return url + "_" + interfaceName + "_" + version + "_" + callType +
                "_" + serialize + "_" + retries + "_" + timeout + "_" + nodeGroup.toString();
    }

}
