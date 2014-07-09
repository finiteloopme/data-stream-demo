package com.kunal.data.stream.cache.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.kunal.data.stream.cache.Tweet;
import com.kunal.data.stream.cache.util.CacheContainerProvider;
import com.kunal.data.stream.cache.util.LocalCacheContainerProvider;
import com.kunal.data.stream.cache.util.LocalStatisticsProvider;
import com.kunal.data.stream.cache.util.Producer;
import com.kunal.data.stream.cache.util.StatisticsProvider;
import com.kunal.data.stream.cache.util.StreamManager;


@RunWith(Arquillian.class)
public class StreamManagerTest {
	@Inject
	private StreamManager streamManager;
	
//	File file = new File(
//			this.getClass().getClassLoader().getResource("/tweet-event.json").getFile()
//			);

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "stream-manager-test.war")
				.addClass(Tweet.class)
				.addClass(CacheContainerProvider.class)
				.addClass(LocalCacheContainerProvider.class)
				.addClass(LocalStatisticsProvider.class)
				.addClass(Producer.class)
				.addClass(StatisticsProvider.class)
				.addClass(StreamManager.class)
				.addAsWebInfResource("web.xml")
				.addAsResource("tweet-event.json")
				.addAsLibraries(Maven.resolver().resolve(
						"org.infinispan:infinispan-core:6.0.0.Final",
						"com.thetransactioncompany:cors-filter:1.3.2",
						"org.apache.cxf:cxf-rt-rs-client:3.0.0-milestone1"
						).withTransitivity().asFile())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void testIsDeployed() {
		Assert.assertNotNull(streamManager);
	}
	
//	@Test
//	@RunAsClient
//	public void testInsertTweet(){
//		
//		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/test");
//		client.type("application/json").accept("application/json");
//		Response response = client.get();
//		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//	}
	
	
	@Test
	public void testNewEvent1(){

		Assert.assertEquals(Response.Status.OK.getStatusCode(), streamManager.newEvent("k1", "e1").getStatus());
	}
	
	
	@Test
	@RunAsClient
	public void testNewEvent(){
		
		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/new-event/testK1/testED1");
		client.type("application/json").accept("application/json");
		Response response = client.post(null);
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	@RunAsClient
	public void testGetCache() throws IOException{
		
//		FileInputStream fis = new FileInputStream(file);
//		byte[] data = new byte[(int)file.length()];
//	    fis.read(data);
//	    fis.close();
//	    String sData = new String(data);
//	    
//		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/new-event/testK2/" + sData);
		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/new-event/testK2/testED2");
		client.type("application/json").accept("application/json");
		Response response = client.post(null);
		
		client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/key/testK2");
		client.type("application/json").accept("application/json");
		response = client.get();
//		System.out.println("OUTPUT: " + response.toString());
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	@RunAsClient
	public void testGetAllKeys(){
		
		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/keys");
		client.type("application/json").accept("application/json");
		Response response = client.get();
		System.out.println("OUTPUT: " + response.toString());
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
}
