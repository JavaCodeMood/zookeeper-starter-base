package com.itzixi.zk.demo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

/**
 * 
 * @Title: ZKConnectDemo.java
 * @Package com.itzixi.zk.demo
 * @Description: zookeeper 获取节点数据的demo演示
 * Copyright: Copyright (c) 2017
 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
 * 
 * @author leechenxiang
 * @date 2017年11月15日 上午11:18:08
 * @version V1.0
 */
public class ZKGetDataDemo implements Watcher {

	private ZooKeeper zookeeper = null;
	
	public static final String zkServerPath = "192.168.1.210:2181";
	public static final Integer timeout = 5000;
	private static Stat stat = new Stat();
	
	public ZKGetDataDemo() {}
	
	public ZKGetDataDemo(String connectString) {
		try {
			zookeeper = new ZooKeeper(connectString, timeout, new ZKGetDataDemo());
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
	
		ZKGetDataDemo zkServer = new ZKGetDataDemo(zkServerPath);
		
		/**
		 * 参数：
		 * path：节点路径
		 * watch：true或者false，注册一个watch事件
		 * stat：状态
		 */
		byte[] resByte = zkServer.getZookeeper().getData("/child", true, stat);
		String result = new String(resByte);
		System.out.println(result);
		connectedSemaphore.await();
//		Thread.sleep(5000);
	}
	
	@Override
	public void process(WatchedEvent event) {
		try {
			System.out.println("监听到watch事件...");
			if(event.getType() == EventType.NodeDataChanged){
				ZKGetDataDemo zkServer = new ZKGetDataDemo(zkServerPath);
				byte[] resByte = zkServer.getZookeeper().getData("/child", true, stat);
				String result = new String(resByte);
				System.out.println(result);
				System.out.println("版本号变化dversion：" + stat.getVersion());
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

