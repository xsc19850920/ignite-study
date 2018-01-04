package com.genpact.ignite.test;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;
import org.junit.Before;
import org.junit.Test;

public class ClusterTest {
	private String IGNITE_HOME;
	private Ignite ignite;

	@Before
	public void before() {
		IGNITE_HOME = "D:/soft/apache-ignite-fabric-1.7.0-bin/";
		// if you want get the cache data from server ignite
		// The ClientMode must bu set true
		Ignition.setClientMode(true);
		ignite = Ignition.start(IGNITE_HOME + "examples/config/example-ignite.xml");
	}
	
	
	
	/**
	 * 
	 * 方法名:demo1
	 * 描    述:读取节点的指标
	 * 返回值:void
	 * 参    数:
	 * 作    者:710009498
	 * 时    间:Oct 27, 2016 10:32:41 AM
	 */
	@Test
	public void demo1(){
		IgniteCluster cluster = ignite.cluster();
		for(ClusterNode node : cluster.nodes()){
			System.out.println(">>> is Local :" + node.isLocal());
			ClusterMetrics metrics = node.metrics();
			double cpuLoad = metrics.getCurrentCpuLoad();
			long usedHeap = metrics.getHeapMemoryUsed();
			int numberOfCores = metrics.getTotalCpus();
			int activeJobs = metrics.getCurrentActiveJobs();
			System.out.println(">>> cpuLoad : "+cpuLoad);
			System.out.println(">>> usedHeap : "+usedHeap);
			System.out.println(">>> numberOfCores : "+numberOfCores);
			System.out.println(">>> activeJobs : "+activeJobs);
			System.out.println("=======================");
		}
//		// Local Ignite node.
//		ClusterNode localNode = cluster.localNode();
//		// Node metrics.
//		ClusterMetrics metrics = localNode.metrics();
//		// Get some metric values.
//		double cpuLoad = metrics.getCurrentCpuLoad();
//		long usedHeap = metrics.getHeapMemoryUsed();
//		int numberOfCores = metrics.getTotalCpus();
//		int activeJobs = metrics.getCurrentActiveJobs();
//		
//		System.out.println(">>> cpuLoad : "+cpuLoad);
//		System.out.println(">>> usedHeap : "+usedHeap);
//		System.out.println(">>> numberOfCores : "+numberOfCores);
//		System.out.println(">>> activeJobs : "+activeJobs);
	}
	
	
	/**
	 * 方法名:demo2
	 * 描    述:一个集群组只会包括CPU利用率小于50%的节点，注意这个组里面的节点会随着CPU负载的变化而改变。
	 * 返回值:void
	 * 参    数:
	 * 作    者:710009498
	 * 时    间:Oct 27, 2016 10:38:20 AM
	 */
	@Test
	public void demo2(){
		ClusterGroup clusterGroup = ignite.cluster().forPredicate((node)-> node.metrics().getCurrentCpuLoad()< 0.5  );
		
		//根据id选择节点
//		 ClusterNode node = clusterGroup.node(UUID.fromString(""));
		
		
		for (ClusterNode node :clusterGroup.nodes()) {
			ClusterMetrics metrics = node.metrics();
			double cpuLoad = metrics.getCurrentCpuLoad();
			long usedHeap = metrics.getHeapMemoryUsed();
			int numberOfCores = metrics.getTotalCpus();
			int activeJobs = metrics.getCurrentActiveJobs();
			
			System.out.println(">>> nodeId : "+node.id());
			System.out.println(">>> cpuLoad : "+cpuLoad);
			System.out.println(">>> usedHeap : "+usedHeap);
			System.out.println(">>> numberOfCores : "+numberOfCores);
			System.out.println(">>> activeJobs : "+activeJobs);
			System.out.println("=======================");
		}
		
	}
	
	
	/**
	 * 	
	 * 方法名:demo3
	 * 描    述:选择最老的节点
	 * 返回值:void
	 * 参    数:
	 * 作    者:710009498
	 * 时    间:Oct 27, 2016 10:42:21 AM
	 */
	@Test
	public void demo3(){
		ClusterGroup clusterGroup = ignite.cluster().forOldest();
		for (ClusterNode node :clusterGroup.nodes()) {
			ClusterMetrics metrics = node.metrics();
			double cpuLoad = metrics.getCurrentCpuLoad();
			long usedHeap = metrics.getHeapMemoryUsed();
			int numberOfCores = metrics.getTotalCpus();
			int activeJobs = metrics.getCurrentActiveJobs();
			
			System.out.println(">>> nodeId : "+node.id());
			System.out.println(">>> cpuLoad : "+cpuLoad);
			System.out.println(">>> usedHeap : "+usedHeap);
			System.out.println(">>> numberOfCores : "+numberOfCores);
			System.out.println(">>> activeJobs : "+activeJobs);
			System.out.println("=======================");
		}
	}
	
	
	
	
}
