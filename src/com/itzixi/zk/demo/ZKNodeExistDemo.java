package com.itzixi.zk.demo;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
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
public class ZKNodeExistDemo implements Watcher {

	private ZooKeeper zookeeper = null;
	
	public static final String zkServerPath = "192.168.1.210:2181";
	public static final Integer timeout = 5000;
	
	public ZKNodeExistDemo() {}
	
	public ZKNodeExistDemo(String connectString) {
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
	
		ZKNodeExistDemo zkServer = new ZKNodeExistDemo(zkServerPath);
		
		/**
		 * 参数：
		 * path：节点路径
		 * watch：watch
		 */
		Stat stat = zkServer.getZookeeper().exists("/child", new ZKNodeExistDemo());
		if (stat != null) {
			System.out.println("节点版本：" + stat.getVersion());
		} else {
			System.out.println("节点不存在...");
		}
		Thread.sleep(10000);
	}
	
	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeCreated) {
			System.out.println("节点创建");
		} else if (event.getType() == EventType.NodeDataChanged) {
			System.out.println("节点数据改变");
		} else if (event.getType() == EventType.NodeDeleted) {
			System.out.println("节点删除");
		}
	}
	
	public ZooKeeper getZookeeper() {
		return zookeeper;
	}
	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}

	
	
}

