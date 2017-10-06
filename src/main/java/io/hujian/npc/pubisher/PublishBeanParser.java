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

import io.hujian.npc.common.NpcConstant;
import io.hujian.npc.logger.NpcLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuJian on 2017/10/4.
 *
 *
 * you should using the resource file
 * {@link io.hujian.npc.common.NpcConstant#NPC_SERVICE_PUBLISH_XML_RESOURCE_FILE_PATH}
 * to publish your service. or , this rpc framework will just offer base implementation.
 *
 */
public class PublishBeanParser {
    private static final NpcLogger LOGGER = NpcLogger.getLogger(PublishBeanParser.class.getName());

    public static class PublishBeanParserHolder {
        public static final PublishBeanParser PUBLISH_BEAN_PARSER = new PublishBeanParser();
    }

    private static final String scanResourceFile = NpcConstant.NPC_SERVICE_PUBLISH_XML_RESOURCE_FILE_PATH; // do not change the file.

    /**
     * using this method to get total service bean.
     * @return the bean name -> bean list Map.
     */
    public Map<String, RpcServicePublishBean> getWholeServiceBeans() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(scanResourceFile);

        int size = applicationContext.getBeanDefinitionCount();

        LOGGER.error("scan file:" +scanResourceFile + " find publish service: " +
                applicationContext.getBeanDefinitionCount());

        if (size == 0) {
            return Collections.emptyMap();
        }

        Map<String, RpcServicePublishBean> beanMap = new HashMap<>();

        Object o;
        for(String bean : applicationContext.getBeanDefinitionNames()) {
            if ( (o = applicationContext.getBean(bean)) instanceof RpcServicePublishBean) {
                beanMap.put(bean, (RpcServicePublishBean) o);

                LOGGER.error("Find Publish Service:" + o.toString());
            }
        }

        return beanMap;
    }

}
