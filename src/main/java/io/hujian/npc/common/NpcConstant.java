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

package io.hujian.npc.common;

/**
 * Created by HuJian on 2017/10/3.
 *
 * some const here
 */
public class NpcConstant {

    public static final int ZK_RETRIES = 10;
    public static final int ZK_RETRY_RETRY_INTERVAL = 1000;
    public static final int ZK_RETRY_LIMIT = 20;
    public static final int ZK_SESSION_TIME_OUT = 5000;
    public static final int ZK_CONNECTION_TIME_OUT = 5000;
    public static final String  ZK_SERVER_ADDRESS = "127.0.0.1:2181";

    public static final String NPC_SERVICE_REGISTER_PATH = "/npc_service_register";
    public static final String NPC_SERVICE_DATA_PATH = NPC_SERVICE_REGISTER_PATH + "/data";

    public static final String NPC_SERVICE_PUBLISH_XML_RESOURCE_FILE_PATH = "service-publish-evil.xml";

}
