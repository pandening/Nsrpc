package io.hujian.rpc.test.server;


import io.hujian.npc.manager.RpcService;
import io.hujian.rpc.test.client.HelloService;

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        return "say hello o:" + msg;
    }

}
