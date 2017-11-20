package com.itzixi.zk.demo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;

/**
 * 
 * @Title: ZKConnectDemo.java
 * @Package com.itzixi.zk.demo
 * @Description: zookeeper 获取子节点数据的demo演示
 * Copyright: Copyright (c) 2017
 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
 * 
 * @author leechenxiang
 * @date 2017年11月15日 上午11:18:08
 * @version V1.0
 */
public class ZKGetChildrenDemo implements Watcher {

	private ZooKeeper zookeeper = null;
	
	public static final String zkServerPath = "192.168.1.210:2181";
	public static final Integer timeout = 5000;
	
	public ZKGetChildrenDemo() {}
	
	public ZKGetChildrenDemo(String connectString) {
		try {
			zookeeper = new ZooKeeper(connectString, timeout, new ZKGetChildrenDemo());
		} catch (IOException e) {
			e.printStackTrace();
			if (zookeeper != null) {
				try {
					zookeeper.close();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
	
		ZKGetChildrenDemo zkServer = new ZKGetChildrenDemo(zkServerPath);
		
		/**
		 * 参数：
		 * path：父节点路径
		 * watch：true或者false，注册一个watch事件
		 */
		List<String> strChildList = zkServer.getZookeeper().getChildren("/child", true);
		for (String s : strChildList) {
			System.out.println(s);
		}
		connectedSemaphore.await();
//		Thread.sleep(5000);
	}
	
	@Override
	public void process(WatchedEvent event) {
		try {
			System.out.println("监听到watch事件...");
			if(event.getType()==EventType.NodeChildrenChanged){
				ZKGetChildrenDemo zkServer = new ZKGetChildrenDemo(zkServerPath);
				List<String> strChildList = zkServer.getZookeeper().getChildren(event.getPath(), true);
				for (String s : strChildList) {
					System.out.println(s);
				}
				connectedSemaphore.countDown();
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ZooKeeper getZookeeper() {
		return zookeeper;
	}
	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}
	
}

