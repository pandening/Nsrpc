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

package io.hujian.npc.zk;

import io.hujian.npc.common.NpcConstant;
import io.hujian.npc.logger.NpcLogger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by HuJian on 2017/10/3.
 *
 * Zookeeper Client
 */
public final class ZookeeperClient implements ZkOperations{
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(ZookeeperClient.class.getName());

    private static volatile ZookeeperClient ZkClient = null; // the singleton instance of this class

    private ZooKeeper zooKeeper; // the zookeeper

    private static int ZK_SESSION_TIMEOUT = 5000; // session time out value
    private static String zkAddress = null; // the zookeeper Address
    private static final String DEFAULT_ZK_ADDRESS = "127.0.0.1:2181"; //default address

    private CountDownLatch latch = new CountDownLatch(1); // the latch

    private ZookeeperClient() {
        NPC_LOGGER.warn("Start to get zookeeper server address from configure..");
        zkAddress = NpcConstant.ZK_SERVER_ADDRESS;

        if (zkAddress.isEmpty()) {
            NPC_LOGGER.error("No Zookeeper Server Address find from configure, using default address:127.0.0.1:2188");
            zkAddress = DEFAULT_ZK_ADDRESS;
        }

        ZK_SESSION_TIMEOUT = NpcConstant.ZK_SESSION_TIME_OUT;

        init();
    }

    private ZookeeperClient(String address) {
        if (address == null || address.isEmpty()) {
            NPC_LOGGER.error("The Param Zookeeper Address is nullOrEmpty, try to find zookeeper server address " +
                    "from configure.");

            zkAddress = NpcConstant.ZK_SERVER_ADDRESS;
            if (zkAddress.isEmpty()) {
                NPC_LOGGER.error("No Zookeeper Server Address find from configure, using default address:127.0.0.1:2188");
                zkAddress = DEFAULT_ZK_ADDRESS;
            }

        } else {
            zkAddress = address;
        }

        ZK_SESSION_TIMEOUT = NpcConstant.ZK_SESSION_TIME_OUT;

        init();
    }

    /**
     * set up the zookeeper client.
     */
    private void init() {
        try {
            zooKeeper = new ZooKeeper(zkAddress, ZK_SESSION_TIMEOUT, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();
            NPC_LOGGER.warn("Success to connect to Zookeeper Server");
        } catch (IOException | InterruptedException e) {
            NPC_LOGGER.error("Could not connect to zookeeper server", e);
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public static class ZookeeperClientFactory {

        /**
         * the shared zookeeper client holder
         */
        private static class SharedZookeeperHolder {
            private static ZookeeperClient SHARED_ZOOKEEPER_CLIENT = new ZookeeperClient();
        }

        /**
         * get the shared zookeeper client
         * @return the client
         */
        public static ZookeeperClient sharedZkClient() {
            return SharedZookeeperHolder.SHARED_ZOOKEEPER_CLIENT;
        }

        /**
         * get the zookeeper client with address.
         * @param address the zookeeper address
         * @return the client
         */
        public static ZookeeperClient sharedZkClient(String address) {
            if (ZkClient == null) {
                synchronized (ZookeeperClientFactory.class) {
                    if (ZkClient == null) {
                        ZkClient = new ZookeeperClient(address);
                    }
                }
            }

            return ZkClient;
        }

        /**
         * new Client any time
         * @return new client
         */
        public static ZookeeperClient newZkClient() {
            if (ZkClient != null) {
              synchronized (ZookeeperClient.class) {
                  if (ZkClient != null) {
                      try {
                          ZkClient.close();
                          NPC_LOGGER.warn("Success to close the old zookeeper client.");
                      } catch (Exception e) {
                          NPC_LOGGER.error("Failed to close the old zookeeper client:", e);
                      } finally {
                          ZkClient = new ZookeeperClient();
                      }
                  }
              }
            } else {
                synchronized (ZookeeperClient.class) {
                    if (ZkClient == null) {
                        ZkClient = new ZookeeperClient();

                        NPC_LOGGER.warn("Success to get an new Zookeeper Client");
                    }
                }
            }

            return ZkClient;
        }

        /**
         * new Client any request
         * @param address the zkAddress
         * @return the new Client
         */
        public static ZookeeperClient newZkClient(String address) {
            if (ZkClient != null) {
                synchronized (ZookeeperClient.class) {
                    if (ZkClient != null) {
                        try {
                            ZkClient.close();
                            NPC_LOGGER.warn("Success to close the old zookeeper client.");
                        } catch (Exception e) {
                            NPC_LOGGER.error("Failed to close the old zookeeper client:", e);
                        } finally {
                            ZkClient = new ZookeeperClient(address);
                        }
                    }
                }
            } else {
                synchronized (ZookeeperClient.class) {
                    if (ZkClient == null) {
                        ZkClient = new ZookeeperClient(address);

                        NPC_LOGGER.warn("Success to get an new Zookeeper Client");
                    }
                }
            }

            return ZkClient;
        }
    }

    /**
     * you can do some assert work here before do something with zookeeper client
     */
    private void requireEnv() {
        if (zooKeeper == null) {
            synchronized (ZookeeperClient.class) {
                if (zooKeeper == null) {
                    NPC_LOGGER.warn("Try to ReConnect to Zookeeper Server:" + zkAddress);
                    init();
                }

                if (zooKeeper == null) {
                    throw new RuntimeException("Could not connect to zookeeper server");
                }
            }
        }
    }


    @Override
    public String get(String path) throws Exception {
        requireEnv();

        byte[] data = zooKeeper.getData(path, false, null);

        return new String(data);
    }

    @Override
    public String get(String path, Stat stat) throws Exception {
        requireEnv();

        byte[] data = zooKeeper.getData(path, false, stat);

        return new String(data);
    }

    @Override
    public String get(String path, boolean isWatch) throws Exception {
        requireEnv();

        byte[] data = zooKeeper.getData(path, isWatch, null);

        return new String(data);
    }

    @Override
    public String getWithNodeExistsEx(String path, Stat stat) throws Exception {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }

    @Override
    public void set(String path, Object data, int version) throws Exception {
        requireEnv();

        zooKeeper.setData(path, data.toString().getBytes(), version);
    }

    @Override
    public void set(String path, Object data) throws Exception {
        set(path, data, 0);
    }

    @Override
    public void create(String path) throws Exception {
        requireEnv();

        zooKeeper.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        NPC_LOGGER.warn("Create PERSISTENT type node ok");
    }

    @Override
    public void create(String path, byte[] data, List<ACL> aclList, CreateMode mode) {
        requireEnv();

        try {
            zooKeeper.create(path, data, aclList, mode);

            NPC_LOGGER.warn("Create an node ok");
        } catch (KeeperException | InterruptedException e) {

            NPC_LOGGER.error("Could not create node with :" +
                    " create(String path, byte[] data, List<ACL> aclList, CreateMode mode)", e);
        }
    }

    @Override
    public void create(String path, Object data) throws Exception {
        requireEnv();

        create(path, data.toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        NPC_LOGGER.warn("Success to create an EPHEMERAL node");
    }

    @Override
    public void create(String path, Object value, int version) throws Exception {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }

    @Override
    public void createIfNotExists(String path, Object data) throws Exception {
        if (!exists(path)) {
            create(path, data);
        }
    }

    @Override
    public void createEphemeral(String path, Object data) throws Exception {
        create(path, data);
    }

    @Override
    public void createEphemeral(String path) throws Exception {
        create(path, "");
    }

    @Override
    public boolean exists(String path) throws Exception {
        requireEnv();

        return zooKeeper.exists(path, false) != null;
    }

    @Override
    public boolean exists(String path, boolean watch) throws Exception {
        requireEnv();

        return zooKeeper.exists(path, watch) != null;
    }

    @Override
    public List<String> getChildren(String path) throws Exception {
        requireEnv();

        return zooKeeper.getChildren(path, false);
    }

    @Override
    public List<String> getChildren(String path, Watcher watcher) {
        requireEnv();

        try {
            return zooKeeper.getChildren(path, watcher);
        } catch (KeeperException | InterruptedException e) {

            NPC_LOGGER.error("Could not get children for:" + path + " with watcher", e);

            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getChildren(String path, boolean watch) throws Exception {
        requireEnv();

        return zooKeeper.getChildren(path, watch);
    }

    @Override
    public void delete(String path) throws Exception {
        delete(path, 1);
    }

    @Override
    public void delete(String path, int version) {
        requireEnv();

        try {
            zooKeeper.delete(path, version);
            NPC_LOGGER.warn("Delete an node with path = " + path + ", version :" + version);
        } catch (InterruptedException | KeeperException e) {

            NPC_LOGGER.error("Could not delete the node:" + path + " With version:" + version, e);
        }
    }

    @Override
    public void deleteIfExists(String path) throws Exception {
        if (exists(path)) {
            delete(path);
        }
    }

    @Override
    public void watch(String path) throws Exception {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }

    @Override
    public void watchChildren(String path) throws Exception {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }

    @Override
    public void close() throws Exception {
        requireEnv();

        zooKeeper.close();

        NPC_LOGGER.warn("Close the Zookeeper Client ok");
    }
}
