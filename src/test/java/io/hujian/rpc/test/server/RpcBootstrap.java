package io.hujian.rpc.test.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * set up the rpc Server
 */
public class RpcBootstrap {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("ApplicationContext.xml");
    }
}
