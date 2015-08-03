package com.hao.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZkDemo {
	private static final int SESSION_TIMEOUT = 30000;

	// 创建 ZooKeeper 实例
	ZooKeeper zk;

	// 创建 Watcher 实例
	Watcher wh = new Watcher() {
		public void process(org.apache.zookeeper.WatchedEvent event) {
			System.out.println("path:" + event.getPath());
			System.out.println(event.getState());
			System.out.println(event.getType());
			System.out.println(event.getWrapper());
		}
	};

	// 初始化 ZooKeeper 实例
	private void createZKInstance() throws IOException {
		zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", ZkDemo.SESSION_TIMEOUT, this.wh);
	}

	public void createNode(String path, byte[] data) throws KeeperException, InterruptedException {
		System.out.println("\n1. 创建 ZooKeeper 节点 (znode ： " + path + ", 数据： " + new String(data) + " ，权限： OPEN_ACL_UNSAFE ，节点类型： Persistent");
		zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	public void getData(String path) throws KeeperException, InterruptedException {
		System.out.println("查看数据：" + path);
		System.out.println(new String(zk.getData(path, wh, null)));
	}

	public void setData(String path, byte[] data) throws KeeperException, InterruptedException {
		System.out.println("\n3. 修改节点数据 ");
		zk.setData(path, data, -1);
	}

	public void deleteNode(String path) throws InterruptedException, KeeperException {
		System.out.println("\n5. 删除节点 ");
		zk.delete(path, -1);
	}

	public void getChildren(String path) throws KeeperException, InterruptedException {
		System.out.println(zk.getChildren(path, false));
	}

	public void isExits(String path) throws KeeperException, InterruptedException {
		System.out.println(null == zk.exists(path, null));
	}

	public void ZKClose() throws InterruptedException {
		zk.close();
	}

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		ZkDemo dm = new ZkDemo();
		String testNode = "/createByJava";
		dm.createZKInstance();
		// dm.createNode(testNode, "create config by java client".getBytes());
		// dm.getData(testNode);
		dm.setData(testNode, ("update config by java client" + System.currentTimeMillis()).getBytes());
		// dm.getData(testNode);
		// dm.isExits(testNode);
		// dm.deleteNode(testNode);
		// dm.getChildren("/zk_test");
		dm.ZKClose();
	}
}
