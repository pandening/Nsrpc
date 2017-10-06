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

package io.hujian.npc.register;

import io.hujian.npc.common.NpcConstant;
import io.hujian.npc.logger.NpcLogger;
import io.hujian.npc.zk.ZookeeperClient;

import java.util.Objects;

/**
 * Created by HuJian on 2017/10/3.
 *
 * implement of serviceRegister
 */
public class SampleServiceRegister implements ServiceRegister{
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(SampleServiceRegister.class.getName());

    public static class SampleServiceRegisterHolder {
        public static final SampleServiceRegister SAMPLE_SERVICE_REGISTER = new SampleServiceRegister();
    }

    private static final ZookeeperClient ZOOKEEPER_CLIENT = ZookeeperClient.ZookeeperClientFactory.sharedZkClient();

    private static final String SERVICE_REGISTER_PATH = NpcConstant.NPC_SERVICE_REGISTER_PATH;
    private static final String SERVICE_DATA_PATH = NpcConstant.NPC_SERVICE_DATA_PATH;

    /**
     * trans the service entry to string
     * @param entry the service entry
     * @return string value
     */
    private String generateServiceData(ServiceEntry entry) {
        if (entry == null) {
            return "";
        }

        return "ip=" + entry.getServiceIp() + "#port=" + entry.getServicePort() +
                "#sname=" + entry.getServiceName() + "#sdesc=" + entry.getServiceDesc() +
                "#stag=" + entry.getServiceTag() + "#sversion=" + entry.getServiceVersion() +
                "#loadBalance=" + entry.getServiceLoadBalance() + "#weight=" + entry.getNodeWeight();
    }

    /**
     * check and create the register if needed
     */
    private void createServiceRegisterRootNode() {
        try {
            if (!ZOOKEEPER_CLIENT.exists(SERVICE_REGISTER_PATH)) {
                ZOOKEEPER_CLIENT.create(SERVICE_REGISTER_PATH);

                NPC_LOGGER.warn("Success to create the service register root node.");
            }

            if (!ZOOKEEPER_CLIENT.exists(SERVICE_DATA_PATH)) {
                ZOOKEEPER_CLIENT.create(SERVICE_DATA_PATH);

                NPC_LOGGER.warn("Success to create the service register data node.");
            }

        } catch (Exception e) {

            NPC_LOGGER.error("Error When using exists", e);
        }
    }

    @Override
    public void registerService(ServiceEntry serviceEntry) {
        String data = generateServiceData(serviceEntry);

        NPC_LOGGER.error("Start to Register:" + data);

        if (Objects.equals("", data)) {
            NPC_LOGGER.error("Empty ServiceEntry.");
        } else {

            //create the root node.
            createServiceRegisterRootNode();

            try {
                ZOOKEEPER_CLIENT.create(SERVICE_DATA_PATH + "/" + data, data);

                NPC_LOGGER.warn("Success to register the service:" + data + " to zookeeper Server");
            } catch (Exception e) {

                NPC_LOGGER.error("Could not register the service:" + data + " to zookeeper server", e);
            }
        }
    }
}
