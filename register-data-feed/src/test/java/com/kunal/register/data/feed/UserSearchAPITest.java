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
import com.kunal.register.data.feed.rest.UserSearchCriteriaAPI;

@RunWith(Arquillian.class)
public class UserSearchAPITest {
	@Inject
	private UserSearchCriteriaAPI userService;
	
//	File file = new File(
//			this.getClass().getClassLoader().getResource("/tweet-event.json").getFile()
//			);

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "user-search-api-test.war")
				.addClass(CamelRoute.class)
				.addClass(Tweet.class)
				.addClass(User.class)
				.addClass(UserSearchCriteriaAPI.class)
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
		Assert.assertNotNull(userService);
	}
	
	@Test
	public void testNewUser() throws Exception{
		User user = new User();
		user.setUser("kunal");
		user.setSearchCriteria("Learning");
		Assert.assertEquals(
				Response.Status.CREATED.getStatusCode(),
				userService.create(user).getStatus()
				);
		//TODO: this delay has been introduced to check the the twitter search is working.
		// need to check this manually on the console (logs)
		// Need to check programmatically
		Thread.sleep(10000);
	}
	
//	@Test
//	@RunAsClient
//	public void testNewEvent() throws InterruptedException{
//		
//		WebClient client = WebClient.create("http://localhost:8080/user-search-api-test/users/new/{" +
//				"user:limaye," +
//				"searchCriteria:basic" +
//				"}");
//		client.type("application/json").accept("application/json");
//		Response response = client.post(null);
//		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//		Thread.sleep(10000);
//	}

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
