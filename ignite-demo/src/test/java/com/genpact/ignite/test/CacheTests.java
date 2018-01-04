package com.genpact.ignite.test;

import java.util.concurrent.locks.Lock;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.junit.Before;
import org.junit.Test;

public class CacheTests {
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
	 * 方法名:demo1 描 述:原子性，分布式事务锁 返回值:void 参 数: 作 者:710009498 时 间:Oct 28, 2016
	 * 8:28:02 AM
	 */
	@Test
	public void demo1() {

		CacheConfiguration<Integer, String> cfg = new CacheConfiguration<Integer, String>();
		cfg.setName("myFirstIgniteCache");
		cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
		IgniteCache<Integer, String> cache = ignite.getOrCreateCache(cfg);
		// for (int i = 0; i < 10; i++)
		// cache.put(i, Integer.toString(i));
		for (int i = 0; i < 10; i++)
			System.out.println("Got [key=" + i + ", val=" + cache.get(i) + ']');
		System.out.println(">>>>>>>>>>>>>>>>>");

		// 注意此处给1这个键增加了分布式锁，确保其更新的原子性，而2这个键没有加锁，所以就不能保证脏数据问题
		Lock lock = cache.lock(1);

		lock.lock();
		try {
			cache.put(1, "2");
			cache.put(2, "3");
		} finally {
			lock.unlock();
		}

		for (int i = 0; i < 10; i++)
			System.out.println("Got [key=" + i + ", val=" + cache.get(i) + ']');
	}

	/**
	 * 方法名:demo2 
	 * 描 述:当在缓存中执行puts和updates操作时，通常需要在网络中发送完整的状态数据，而EntryProcessor可以直接在主节点上处理数据
	 		，只需要传输增量数据而不是全量数据。 此外，可以在EntryProcessor中嵌入自定义逻辑，比如，获取之前缓存的数据然后加1.
	 * 返回值:void 
	 * 参 数: 
	 * 作 者:710009498 
	 * 时 间:Oct 28, 2016 8:32:33 AM
	 */
	@Test
	public void demo2() {
		CacheConfiguration<String, Integer> cfg = new CacheConfiguration<String, Integer>();
		cfg.setName("myFirstIgniteCache");
		cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
		//分区化缓存适合于数据量很大而更新频繁的场合。
//		cfg.setCacheMode(CacheMode.PARTITIONED);
		//适用于或者数据是只读的，或者需要定期刷新的场景中
//		cfg.setCacheMode(CacheMode.LOCAL);
		//复制缓存适用于数据集不大而且更新不频繁的场合。
//		cfg.setCacheMode(CacheMode.REPLICATED);
		
		IgniteCache<String, Integer> cache = ignite.getOrCreateCache(cfg);

		for (int i = 0; i < 10; i++) {
			cache.invoke("key", new EntryProcessor<String, Integer, Void>() {

				@Override
				public Void process(MutableEntry<String, Integer> entry, Object... arg1) throws EntryProcessorException {
					Integer val = entry.getValue();
					entry.setValue(val == null ? 1 : val + 1);
					return null;
				}

			});
		}

	}

}
