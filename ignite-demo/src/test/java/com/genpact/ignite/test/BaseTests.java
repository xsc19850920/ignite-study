package com.genpact.ignite.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.compute.ComputeTaskFuture;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.transactions.Transaction;
import org.junit.Before;
import org.junit.Test;

public class BaseTests {
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

	// 缓存的存储和读取
	@Test
	public void demo1() {

		// if ignite server start with params
		// the client must be same as server like under
		IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myFirstIgniteCache");
		// cache.putIfAbsent("World", 22);
		System.out.println("Got [key=World, val=" + cache.get("World") + ']');
	}

	// 事务
	@Test
	public void demo2() {
		String key = "World";
		IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myFirstIgniteCache");
		Transaction tx = ignite.transactions().txStart();
		Integer value = cache.get(key);
		if(null != value){
			cache.getAndReplace(key, 0);
			tx.commit();
		}else{
			cache.getAndReplace(key, 1);
			tx.rollback();
		}
		
	}
	
//	分布式锁：
	@Test
	public void demo3(){
		String key = "World";
		IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myFirstIgniteCache");
		Lock lock = cache.lock(key);
		lock.lock();
		try {
		    cache.put("Hello", 11);
		    cache.put("World", 22);
		}
		finally {
		    lock.unlock();
		} 
	}
	// 服务端节点执行：
	@Test
	public void demo4() {

		Collection<IgniteCallable<Integer>> calls = new ArrayList<>();
		// Iterate through all the words in the sentence and create Callable
		// jobs.
		
		for (final String word : "Count characters using callable".split(" ")){
			calls.add(word::length);
		}
		// Execute collection of Callables on the grid.
		Collection<Integer> res = ignite.compute().call(calls);
		// Add up all the results.
//		int sum = 0;
		// Add up individual word lengths received from remote nodes.
		int sum = res.stream().mapToInt(Integer::intValue).sum();
//		for (int len : res)
//			sum += len;
		System.out.println("Total number of characters is '" + sum + "'.");

	}

	// 客户端节点执行
	@Test
	public void demo5() {
		ClusterGroup clusterGroup = ignite.cluster().forClients();
		List<IgniteCallable<Integer>> calls = new ArrayList<IgniteCallable<Integer>>();
		for (final String word : "Count characters using callable".split(" ")) {
			calls.add(word::length);
		}

		Collection<Integer> res = ignite.compute(clusterGroup).call(calls);
		// Add up all the results.
		int sum = 0;
		// Add up individual word lengths received from remote nodes.
		for (int len : res)
			sum += len;
		System.out.println("Total number of characters is '" + sum + "'.");
	}
	
	
	//异步
	@Test
	public void demo6(){
		IgniteCompute asyncCompute = ignite.compute().withAsync();
		asyncCompute.call(()->{return "Hello World";});
		ComputeTaskFuture<String> future = asyncCompute.future();
		future.listen(f->System.out.println(f.get()));
	}

}
