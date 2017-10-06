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

package io.hujian.npc.discover;

import io.hujian.npc.common.NpcConstant;
import io.hujian.npc.logger.NpcLogger;
import io.hujian.npc.manager.ServiceManager;
import io.hujian.npc.zk.ZookeeperClient;
import lombok.Getter;
import lombok.Setter;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuJian on 2017/10/3.
 *
 * service discover
 */
public class ServiceDiscover {
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(ServiceDiscover.class.getName());

    @Setter @Getter
    private volatile List<String> dataList = new ArrayList<>();

    private static final ZookeeperClient ZOOKEEPER_CLIENT = ZookeeperClient.ZookeeperClientFactory.sharedZkClient();

    public ServiceDiscover() {
        watchNode();
    }

    /**
     * watch the node status change.
     */
    private void watchNode() {
        try {
            List<String> nodeList = ZOOKEEPER_CLIENT.getChildren(NpcConstant.NPC_SERVICE_DATA_PATH, watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    watchNode();
                }
            });
            List<String> dataList = new ArrayList<>();
            for (String node : nodeList) {
                 dataList.add(ZOOKEEPER_CLIENT.get(NpcConstant.NPC_SERVICE_DATA_PATH + "/" + node,
                         false));
            }

            NPC_LOGGER.warn("get node list:" + dataList);
            this.dataList = dataList;

            //update the service
            updateService(dataList);
        } catch (Exception e) {

            NPC_LOGGER.error("Could not get children of node:" + NpcConstant.NPC_SERVICE_REGISTER_PATH, e);
        }

    }

    /**
     * update the service
     * @param dataList the node list
     */
    private void updateService(List<String> dataList) {

        NPC_LOGGER.warn("Update the Service....");

        ServiceManager.ServiceManagerHolder.SERVICE_MANAGER.updateService(dataList);
    }

    public void stop() {
        try {
            ZOOKEEPER_CLIENT.close();

            NPC_LOGGER.warn("Close the Zookeeper client ok");
        } catch (Exception e) {
            NPC_LOGGER.warn("Close the Zookeeper client error", e);
        }
    }

}
