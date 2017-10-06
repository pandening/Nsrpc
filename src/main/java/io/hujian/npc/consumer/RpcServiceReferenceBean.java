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

package io.hujian.npc.consumer;

import io.hujian.npc.logger.NpcLogger;
import io.hujian.npc.manager.RpcClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * Created by hujian06 on 2017/10/6.
 *
 * the reference bean.
 *
 * todo: more information to server. more and more powerful!
 */
@Setter
@Getter
@Component
public class RpcServiceReferenceBean implements FactoryBean<Object> , BeanNameAware{
    private static final NpcLogger LOGGER = NpcLogger.getLogger(RpcServiceReferenceBean.class.getName());

    private String desc; // the desc

    private Class<Object> clazz; // the service-interface class. like io.hujian.rpc.xxx
    private Object obj; // the obj

    private String callType = "sync"; // the callType [sync|future|callback]
    private String loadBalance = "roundRobin"; // load balance
    private String codecAlgorithm = "Protostuff"; // the codec
    private String timeout = "4000"; // the timeout unit is :(ms)

    public RpcServiceReferenceBean() {

    }

    /**
     * must let the init function setUp the bean before you use the service bean.
     */
    public void init() {

        if (callType.equals("sync")) {
            obj = RpcClient.create(clazz);
        } else {
            obj = RpcClient.createAsync(clazz);
        }

        LOGGER.warn("Success to init bean at: RpcServiceReferenceBean.init");

    }

    @Override
    public Object getObject() throws Exception {
        return obj;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setBeanName(String s) {
        LOGGER.warn("Set up the Service bean:" + s + " at RpcServiceReferenceBean.setBeanName");
    }

}
