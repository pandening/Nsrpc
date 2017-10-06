package io.hujian.rpc.test.application;

import io.hujian.npc.discover.ServiceDiscover;
import io.hujian.npc.manager.IAsyncObjectProxy;
import io.hujian.npc.manager.RPCFuture;
import io.hujian.npc.manager.RpcClient;
import io.hujian.rpc.test.client.HelloService;

import java.util.concurrent.TimeUnit;

/**
 * Created by HuJian on 2017/10/4.
 *
 *
 */
public class BenchmarkAsync {
    public static void main(String[] args) throws InterruptedException {
        ServiceDiscover serviceDiscovery = new ServiceDiscover();
        final RpcClient rpcClient = new RpcClient(serviceDiscovery);

        int threadNum = 3;
        final int requestNum = 5;
        Thread[] threads = new Thread[threadNum];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadNum; ++i) {
            threads[i] = new Thread(() -> {
                for (int i1 = 0; i1 < requestNum; i1++) {
                    try {

                        IAsyncObjectProxy client = RpcClient.createAsync(HelloService.class);

                        RPCFuture helloFuture = client.call("hello", Integer.toString(i1));

                        String result = (String) helloFuture.get(3000, TimeUnit.MILLISECONDS);

                        System.out.println("ASYNC-RPC-RESULT:" + result);

                    } catch (Exception e) {

                        System.out.println(e);
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long timeCost = (System.currentTimeMillis() - startTime);
        String msg = String.format("Async call total-time-cost:%sms, req/s=%s", timeCost, ((double) (requestNum * threadNum)) / timeCost * 1000);
        System.out.println(msg);

        rpcClient.stop();

    }
}
