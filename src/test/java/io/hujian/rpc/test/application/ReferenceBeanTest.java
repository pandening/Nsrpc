package io.hujian.rpc.test.application;

import io.hujian.npc.manager.AsyncRPCCallback;
import io.hujian.npc.manager.IAsyncObjectProxy;
import io.hujian.npc.manager.RPCFuture;
import io.hujian.npc.manager.RpcClient;
import io.hujian.rpc.test.client.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutionException;

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

        HelloService helloService = (HelloService) context.getBean("syncHelloService");

        System.out.println("Sync Call Result:" + helloService.hello("hello ok-rpc"));

        IAsyncObjectProxy proxy = (IAsyncObjectProxy) context.getBean("futureHelloService");

        RPCFuture future = proxy.call("hello", "hello ok-rpc");

        try {
            System.out.println("Future Call Type:" + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        proxy = (IAsyncObjectProxy) context.getBean("callbackHelloService");

        proxy.call("hello", "hello ok-rpc").addCallback(new AsyncRPCCallback() {
            @Override
            public void success(Object result) {
                System.out.println("Callback Call Result:" + result);
            }

            @Override
            public void fail(Exception e) {
                System.out.println("Callback Call Result:" + e);
            }
        });
    }

}
