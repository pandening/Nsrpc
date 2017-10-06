/**
 * Created by HuJian on 2017/10/3.
 *
 * the service entry.
 *
 * the framework will detect the service's methods auto.
 * so you just need to register the service. and the ip:port pair
 * of this service.
 *
 * the important point is the ip:port and serviceName
 *
 * you can using the serviceVersion to do the version-control
 * then you can assign some same service with different version, like
 * for service "EchoService", you can register it to zookeeper with
 * 0.0.1 version, and if you want to update your service "EchoService",
 * but you want to contain the old version's service, you can register
 * the same service with different version such as "0.0.2".
 *
 */

package io.hujian.npc.register;

import io.hujian.npc.manager.RpcClientHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
public class ServiceEntry {

    private String serviceIp;
    private String servicePort;
    private String serviceName;
    private String serviceDesc;
    private String serviceTag;
    private String serviceVersion;

    private String serviceLoadBalance = "roundRobin"; // the load balance
    private int nodeWeight = 1; // the weight of this node
    private AtomicInteger nodeHitCount = new AtomicInteger(0); // the hit count

    private RpcClientHandler rpcClientHandler; // the handler

    public ServiceEntry() {

    }

    public ServiceEntry(String ip, String port, String name, String desc,
                        String tag, String version) {
        this.serviceIp = ip;
        this.servicePort = port;
        this.serviceName = name;
        this.serviceDesc = desc;
        this.serviceTag = tag;
        this.serviceVersion = version;
    }

    /**
     * main point of the entry
     * @return main str.
     */
    @Override
    public String toString() {
        return "rpc://" + serviceIp + ":" + servicePort + "/" + serviceName + "_" + serviceVersion;
    }

    /**
     * NOTICE: just compare name,tag,version
     * @param serviceEntry the another service
     * @return true or false
     */
    @Override
    public boolean equals(Object serviceEntry) {
        if (serviceEntry instanceof ServiceEntry) {
            ServiceEntry se = (ServiceEntry) serviceEntry;

            return Objects.equals(se.serviceName, this.serviceName) && Objects.equals(se.getServiceTag(), this.serviceTag)
                    && Objects.equals(se.serviceVersion, this.serviceVersion);
        } else {
            return false;
        }
    }
}
