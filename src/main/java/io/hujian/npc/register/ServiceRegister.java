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

/**
 * Created by HuJian on 2017/10/3.
 *
 * the service register
 */
public interface ServiceRegister {

    /**
     * register this service to zookeeper server. let the client could
     * find the server.
     * @param serviceEntry the service entry
     */
    void registerService(ServiceEntry serviceEntry);

}
