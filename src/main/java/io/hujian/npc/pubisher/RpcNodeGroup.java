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

import lombok.Getter;
import lombok.Setter;

/**
 * Created by HuJian on 2017/10/4.
 *
 * node list
 */
@Setter
@Getter
public class RpcNodeGroup {

    private String nodeGroupName; // the group
    private String nodeList = ""; // ip:port:weight

    public RpcNodeGroup() {
        nodeGroupName = "default-rpc-nodes-group"; // the default group
    }

    public RpcNodeGroup(String nodeGroupName) {
        this.nodeGroupName = nodeGroupName;
    }

    @Override
    public String toString() {
        if (nodeList == null || nodeList.isEmpty()) {
            return "";
        }

        return "[" + nodeGroupName + "] -> [" + nodeList + "]";
    }
}
