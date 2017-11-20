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
 * @Description: zookeeper 设置节点数据的demo演示
 * Copyright: Copyright (c) 2017
 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
 * 
 * @author leechenxiang
 * @date 2017年11月15日 上午11:18:08
 * @version V1.0
 */
public class ZKSetDataDemo {

	private ZooKeeper zookeeper = null;
	
	public static final String zkServerPath = "192.168.1.210:2181";
	public static final Integer timeout = 5000;
	private static Stat stat = new Stat();
	
	public ZKSetDataDemo() {}
	
	public ZKSetDataDemo(String connectString) {
		try {
			zookeeper = new ZooKeeper(connectString, timeout, null);
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
	
	public static void main(String[] args) throws Exception {
	
		ZKSetDataDemo zkServer = new ZKSetDataDemo(zkServerPath);
		
		/**
		 * 参数：
		 * path：节点路径
		 * data：数据
		 * version：数据状态
		 */
		zkServer.getZookeeper().setData("/child", "itzixi".getBytes(), 4);
	}
	
	public ZooKeeper getZookeeper() {
		return zookeeper;
	}
	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}
	
}

