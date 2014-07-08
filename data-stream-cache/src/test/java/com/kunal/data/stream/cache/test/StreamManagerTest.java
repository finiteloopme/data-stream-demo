package com.kunal.data.stream.cache.test;

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
	private StreamManager streamManger;

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
				.addAsLibraries(Maven.resolver().resolve(
						"org.infinispan:infinispan-core:6.0.0.Final",
						"com.thetransactioncompany:cors-filter:1.3.2",
						"org.apache.cxf:cxf-rt-rs-client:3.0.0-milestone1"
						).withTransitivity().asFile())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void testIsDeployed() {
		Assert.assertNotNull(streamManger);
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
	@RunAsClient
	public void testNewEvent(){
		
		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/new-event/testK/testED");
		client.type("application/json").accept("application/json");
		Response response = client.post(null);
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	@RunAsClient
	public void testGetCache(){
		
		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/key/testK");
		client.type("application/json").accept("application/json");
		Response response = client.get();
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
