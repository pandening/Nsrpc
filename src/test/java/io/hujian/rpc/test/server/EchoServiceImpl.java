package io.hujian.rpc.test.server;

import io.hujian.npc.manager.RpcService;
import io.hujian.rpc.test.client.EchoService;

/**
 * Created by HuJian on 2017/10/5.
 *
 * impl
 */
@RpcService(EchoService.class)
public class EchoServiceImpl implements EchoService{
    @Override
    public void echo(String msg) {
        System.out.println("echo:" + msg);
    }
}
