package com.itzixi.zk.demo;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

/**
 * 
 * @Title: ZKConnectDemo.java
 * @Package com.itzixi.zk.demo
 * @Description: zookeeper 操作demo演示
 * Copyright: Copyright (c) 2017
 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
 * 
 * @author leechenxiang
 * @date 2017年11月15日 上午11:18:08
 * @version V1.0
 */
public class ZKOperatorDemo {

	private ZooKeeper zookeeper = null;
	
	public static final String zkServerPath = "192.168.1.210:2181";
	public static final Integer timeout = 5000;
	
	public ZKOperatorDemo() {}
	
	public ZKOperatorDemo(String connectString) {
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
	
	/**
	 * 
	 * @Title: ZKOperatorDemo.java
	 * @Package com.itzixi.zk.demo
	 * @Description: 创建zk节点
	 * Copyright: Copyright (c) 2017
	 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
	 * 
	 * @author leechenxiang
	 * @date 2017年11月15日 下午2:06:36
	 * @version V1.0
	 */
	public void createZKNode(String path, byte[] data, List<ACL> acls) {
		
		String result = "";
		try {
			/**
			 * 同步或者异步创建节点，都不支持子节点的递归创建，异步有一个callback函数
			 * 参数：
			 * path：创建的路径
			 * data：存储的数据的byte[]
			 * acl：控制权限策略
			 * 			Ids.OPEN_ACL_UNSAFE --> world:anyone:cdrwa
			 * 			CREATOR_ALL_ACL --> auth:user:password:cdrwa
			 * createMode：节点类型, 是一个枚举
			 * 			PERSISTENT：持久节点
			 * 			PERSISTENT_SEQUENTIAL：持久顺序节点
			 * 			EPHEMERAL：临时节点
			 * 			EPHEMERAL_SEQUENTIAL：临时顺序节点
			 */
			result = zookeeper.create(path, data, acls, CreateMode.PERSISTENT);
			System.out.println("创建节点：\t" + result + "\t成功...");
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) throws Exception {
	
		ZKOperatorDemo zkServer = new ZKOperatorDemo(zkServerPath);
		
		/**
		 * ======================  创建node start  ======================  
		 */
		// acl 任何人都可以访问
		/*zkServer.createZKNode("/leenode/node1", "node1".getBytes(), Ids.OPEN_ACL_UNSAFE);
		
		// 用户认证过的访问
		zkServer.getZookeeper().addAuthInfo("digest", "leenode2:123456".getBytes());
		zkServer.createZKNode("/leenode/node2", "node2".getBytes(), Ids.CREATOR_ALL_ACL);
		
		// 自定义用户认证访问
		List<ACL> acls = new ArrayList<ACL>();
		Id lee1001 = new Id("digest", "lee1001:d10P4dEqSdWLiUITSkI0kS/aMKg=");
		Id lee1002 = new Id("digest", "lee1002:d10P4dEqSdWLiUITSkI0kS/aMKg=");
		acls.add(new ACL(Perms.ALL, lee1001));
		acls.add(new ACL(Perms.READ, lee1002));
		zkServer.createZKNode("/leenode/node6", "node4".getBytes(), acls);
		
		// ip方式的acl
		List<ACL> aclsIP = new ArrayList<ACL>();
		Id ipId = new Id("ip", "127.0.0.1");
		aclsIP.add(new ACL(Perms.ALL, ipId));
		zkServer.createZKNode("/leenode/node5", "node5".getBytes(), aclsIP);*/

		
		/**
		 * 更新acl，更新node节点数据
		 */
//		zkServer.getZookeeper().setACL(path, acl, aclVersion)
//		zkServer.getZookeeper().setData(path, data, version)
		
		
		/**
		 * 删除节点 同步
		 */
		zkServer.getZookeeper().addAuthInfo("digest", "lee:lee".getBytes());
		zkServer.createZKNode("/testnode", "node2".getBytes(), Ids.CREATOR_ALL_ACL);
		
		zkServer.getZookeeper().addAuthInfo("digest", "lee:lee".getBytes());
		zkServer.getZookeeper().delete("/testnode", 1);
		
		
		/**
		 * 删除节点 异步
		 */
		zkServer.getZookeeper().addAuthInfo("digest", "lee:lee".getBytes());
		zkServer.createZKNode("/testnode", "node2".getBytes(), Ids.CREATOR_ALL_ACL);
		
		zkServer.getZookeeper().addAuthInfo("digest", "lee:lee".getBytes());
		String ctx = "{'user':'success'}";
		zkServer.getZookeeper().delete("/testnode", 0, new DeleteCallBack(), ctx);
		// 不让线程立即结束，不然异步的线程会接受不到
		Thread.sleep(3000);
	}

	public ZooKeeper getZookeeper() {
		return zookeeper;
	}
	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}
}

