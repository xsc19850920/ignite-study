package com.genpact.ignite.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteFileSystem;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.FileSystemConfiguration;
import org.apache.ignite.igfs.IgfsIpcEndpointConfiguration;
import org.apache.ignite.igfs.IgfsIpcEndpointType;
import org.apache.ignite.igfs.IgfsPath;
import org.junit.Before;
import org.junit.Test;

public class IGFSTest {
	private String IGNITE_HOME;
	private Ignite ignite;

	@Before
	public void before() {
		IGNITE_HOME = "D:/soft/apache-ignite-hadoop-1.7.0-bin/";
		// if you want get the cache data from server ignite
		// The ClientMode must bu set true
		Ignition.setClientMode(true);
		ignite = Ignition.start(IGNITE_HOME + "config/default-config.xml");
	}
	
	
	
	/**
	 * 方法名:demo1
	 * 描    述:IGFS 文件操作
	 * 返回值:void
	 * 参    数:@throws IgniteException
	 * 参    数:@throws IOException
	 * 作    者:710009498
	 * 时    间:Oct 28, 2016 10:18:55 AM
	 */
	@Test
	public void demo1() throws IgniteException, IOException{
		IgniteFileSystem fs = ignite.fileSystem("myFileSystem");
		// Create directory.
		IgfsPath dir = new IgfsPath("/myDir");
//		fs.mkdirs(dir);
		
		// Create file and write some data to it.
		IgfsPath file = new IgfsPath(dir, "myFile");
//		try (OutputStream out = fs.create(file, true)) {
//		    out.write("aaa".getBytes());
//		}
//		
		// Read from file.
		try (InputStream in = fs.open(file)) {
			byte[] b = new byte[1024];
		    in.read(b);
		    System.out.println(new String(b));
		}
		
	}
	
	
	@Test
	public void demo2() throws IgniteException, IOException{
//		igfs://[igfs_name@][host]:[port]
		IgniteFileSystem fs = ignite.fileSystem("igfs");
		IgfsPath dir = new IgfsPath("/xsc");
//		fs.mkdirs(dir);
		
		// Create file and write some data to it.
		IgfsPath file = new IgfsPath(dir, "bbb.txt");
//		try (OutputStream out = fs.create(file, true)) {
//		    out.write("aaa".getBytes());
//		}
//		
		
		// Read from file.
		try (InputStream in = fs.open(file)) {
			byte[] b = new byte[1024];
		    in.read(b);
		    System.out.println(new String(b));
		}
		
	}
}
