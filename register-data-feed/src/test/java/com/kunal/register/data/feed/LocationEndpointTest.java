package com.kunal.register.data.feed;

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

import com.kunal.register.data.feed.bean.CamelRoute;
import com.kunal.register.data.feed.bean.Tweet;
import com.kunal.register.data.feed.model.Location;
import com.kunal.register.data.feed.model.User;
import com.kunal.register.data.feed.rest.LocationEndpoint;
import com.kunal.register.data.feed.rest.UserEndpoint;

@RunWith(Arquillian.class)
public class LocationEndpointTest {
	@Inject
	private LocationEndpoint locationService;
	
//	File file = new File(
//			this.getClass().getClassLoader().getResource("/tweet-event.json").getFile()
//			);

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "location-endpoint-test.war")
				.addClass(CamelRoute.class)
				.addClass(Tweet.class)
				.addClass(Location.class)
				.addClass(User.class)
				.addClass(LocationEndpoint.class)
				.addClass(UserEndpoint.class)
				.addAsWebInfResource("web.xml")
				//.addAsWebInfResource("META-INF/test-persistence.xml")
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsLibraries(Maven.resolver().resolve(
						"org.apache.camel:camel-twitter:2.13.0",
						"org.apache.camel:camel-http4:2.13.0",
						"org.apache.camel:camel-jackson:2.13.0",
						"org.apache.cxf:cxf-rt-rs-client:3.0.0-milestone1"
						).withTransitivity().asFile())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void testIsDeployed() {
		Assert.assertNotNull(locationService);
	}
	
	@Test
	public void testCreateLocation() throws Exception{
		Location location = new Location();
		location.setLeftAsLongitude((long)144.94781);
		location.setBottomAsLatitude((long)-37.82331);
		location.setRightAsLongitude((long)144.98121);
		location.setTopAsLatitude((long)-37.80301);
		Response response = locationService.create(location);
		
		Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
	}
	
	
//	@Test
//	public void testNewEvent1(){
//
//		Assert.assertEquals(Response.Status.OK.getStatusCode(), locationService.newEvent("k1", "e1").getStatus());
//	}
//	
//	
	@Test
	@RunAsClient
	public void testNewEvent(){
		
		WebClient client = WebClient.create("http://localhost:8080/location-endpoint-test/locations/new/{" +
				"leftAsLongitude:144.94781," +
				"bottomAsLatitude:-37.82331," +
				"righAsLongitude:144.98121," +
				"topAsLatitude:-37.80301" +
				"}");
		client.type("application/json").accept("application/json");
		Response response = client.post(null);
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
//	
//	@Test
//	@RunAsClient
//	public void testGetCache() throws IOException{
		
////		FileInputStream fis = new FileInputStream(file);
////		byte[] data = new byte[(int)file.length()];
////	    fis.read(data);
////	    fis.close();
////	    String sData = new String(data);
////	    
////		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/new-event/testK2/" + sData);
//		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/new-event/testK2/testED2");
//		client.type("application/json").accept("application/json");
//		Response response = client.post(null);
//		
//		client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/key/testK2");
//		client.type("application/json").accept("application/json");
//		response = client.get();
////		System.out.println("OUTPUT: " + response.toString());
//		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//	}
//	
//	@Test
//	@RunAsClient
//	public void testGetAllKeys(){
//		
//		WebClient client = WebClient.create("http://localhost:8080/stream-manager-test/cache/streaming-data/keys");
//		client.type("application/json").accept("application/json");
//		Response response = client.get();
//		System.out.println("OUTPUT: " + response.toString());
//		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//	}
//	
}
