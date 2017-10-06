package io.hujian.rpc.test.application;

import io.hujian.npc.discover.ServiceDiscover;
import io.hujian.npc.manager.RpcClient;
import io.hujian.rpc.test.client.HelloService;

/**
 * Created by HuJian on 2017/10/4.
 *
 * test
 */
public class Benchmark {

    public static void main(String[] args) throws InterruptedException {

        ServiceDiscover serviceDiscovery = new ServiceDiscover();
        final RpcClient rpcClient = new RpcClient(serviceDiscovery);

        int threadNum = 1;
        final int requestNum = 1;
        Thread[] threads = new Thread[threadNum];

        long startTime = System.currentTimeMillis();
        //benchmark for sync call
        for(int i = 0; i < threadNum; ++i) {
            threads[i] = new Thread(() -> {
                for (int i1 = 0; i1 < requestNum; i1++) {
                    final HelloService syncClient = RpcClient.create(HelloService.class);
                    String result = syncClient.hello(Integer.toString(i1));

                    System.out.println("RPC-RESULT:" + result);

                }
            });
            threads[i].start();
        }
        for(int i=0; i<threads.length;i++){
            threads[i].join();
        }
        long timeCost = (System.currentTimeMillis() - startTime);
        String msg = String.format("Sync call total-time-cost:%sms, req/s=%s",
                timeCost, ((double)(requestNum * threadNum)) / timeCost * 1000);

        System.out.println(msg);

        rpcClient.stop();
    }
}
