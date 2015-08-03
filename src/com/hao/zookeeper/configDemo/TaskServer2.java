package com.hao.zookeeper.configDemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class TaskServer2 {
	private static final String ACCOUNTSERVER_HOST = "/sem.accountserver.host";
	ZooKeeper zk;
	Watcher wh = new Watcher() {
		public void process(org.apache.zookeeper.WatchedEvent event) {
			System.out.println("catch watcher:" + event);
			if (null != event.getPath()) {
				try {
					System.out.println("getData:" + new String(zk.getData(ACCOUNTSERVER_HOST, false, null)));
					zk.exists(ACCOUNTSERVER_HOST, wh);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public static void main(String args[]) throws Exception {
		TaskServer2 server1 = new TaskServer2();
		server1.testWatch();
		server1.startSocketServer();
	}

	private void testWatch() throws IOException, KeeperException, InterruptedException {
		initZookeeper();
		zk.exists(ACCOUNTSERVER_HOST, wh);
	}

	private ZooKeeper initZookeeper() throws IOException {
		if (null == zk) {
			zk = new ZooKeeper("127.0.0.1:2182", 300000, null);
		}
		return zk;
	}


	private void startSocketServer() {
		try {
			ServerSocket server = null;
			try {
				server = new ServerSocket(4702);
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
