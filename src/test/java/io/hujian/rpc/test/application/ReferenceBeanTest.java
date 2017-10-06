package io.hujian.rpc.test.application;

import io.hujian.npc.manager.RpcClient;
import io.hujian.rpc.test.client.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by HuJian on 2017/10/6.
 *
 *
 */
public class ReferenceBeanTest {


    public static void main(String ... args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

       // String className = "io.hujian.rpc.test.server.HelloServiceImpl";

        //HelloService helloService = (HelloService) Class.forName(className).newInstance();

        //System.out.println(helloService.hello("hello"));

        ApplicationContext context = new ClassPathXmlApplicationContext("client-services.xml");

        //ReferenceBean bean = (ReferenceBean) context.getBean("referenceBean");

       //Class<Object> clazz = bean.getClazz();

        //HelloService helloService = (HelloService) RpcClient.createSimple(clazz);

       // System.out.println(helloService.hello("joke!"));

        HelloService helloService = (HelloService) context.getBean("helloService1");

        System.out.println("0000:" + helloService.hello("000999000"));

    }

}
