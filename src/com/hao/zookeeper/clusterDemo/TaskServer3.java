package com.hao.zookeeper.clusterDemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class TaskServer3 {
	private static final String ACCOUNTSERVER_HOST = "/AllTaskServer";
	ZooKeeper zk;
	Watcher wh = new Watcher() {
		public void process(org.apache.zookeeper.WatchedEvent event) {
			try {
				List<String> childrenList = zk.getChildren(ACCOUNTSERVER_HOST, false);
				System.out.println("all valid TaskServer:");
				for (String server : childrenList) {
					System.out.println(server);
				}
				zk.getChildren(ACCOUNTSERVER_HOST, wh);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public static void main(String args[]) throws Exception {
		TaskServer3 server1 = new TaskServer3();
		server1.startUp();
		server1.startSocketServer();
	}

	private void startUp() throws Exception {
		System.out.println("I'm Server3");
		initZookeeper();
		zk.getChildren(ACCOUNTSERVER_HOST, wh);
		zk.create(ACCOUNTSERVER_HOST.concat("/10.100.30.127"), "10.100.30.127".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}

	private ZooKeeper initZookeeper() throws IOException {
		if (null == zk) {
			zk = new ZooKeeper("127.0.0.1:2183", 5000, wh);
		}
		return zk;
	}

	private void startSocketServer() {
		try {
			ServerSocket server = null;
			try {
				server = new ServerSocket(4703);
			} catch (Exception e) {
				System.out.println("can not listen to:" + e);
			}
			Socket socket = null;
			try {
				socket = server.accept();
			} catch (Exception e) {
				System.out.println("Error." + e);
			}
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}
}
