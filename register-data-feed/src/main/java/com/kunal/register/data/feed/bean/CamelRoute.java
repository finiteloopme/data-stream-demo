/**
 * 
 */
package com.kunal.register.data.feed.bean;

import javax.ejb.Stateless;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * @author kunallimaye
 *
 */
@Stateless
public class CamelRoute {

	private CamelContext camelContext	= null;
	private final String DATA_STREAM_HOST	= "http4://streaming-kunal.rhcloud.com/";
//	private final String DATA_STREAM_HOST	= "http://localhost:8080/";
	
	/**
	 * 
	 */
	public CamelRoute() throws Exception {
		camelContext = new DefaultCamelContext();
		
	}
	
	public void configure(final String username, final String searchCriteria)
		throws Exception{
		
		camelContext.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				from("twitter://"
						// query type = search
						+ "search?"
//						+ "streaming/filter?"
						// query execution type = polling
						+ "type=polling" + "&"
						// poll every 10 sec
						+ "delay=10" + "&"
						+ "keywords=" + searchCriteria + "&"
						+ "consumerKey=HjLbxF1IoN1bXENLDuMPbJsQT" + "&"
						+ "consumerSecret=62no3XxjQGrgWzASJXkyoW2L0Rs2Ba6Qi1OWFcwilgzOC1rWwW" + "&"
						+ "accessToken=229302807-NwkYedrDGdZ4CDxvoRcxVtH8klPqfC9Yxt0fD9Fn" + "&"
						+ "accessTokenSecret=its7D8zTq8GEgCzxHYeCYt4g9HtswwUFBLwrv0bHjngzJ")
					.log("\n====================\n" +
						 "\tTweeted at place: ${body.place}\n" +
						 "\tTweeted at location: ${body.geoLocation}")
						 .marshal().json(JsonLibrary.Jackson)
						 .log("Marshalled to JSON:\n${body}")
					 .log("==Route Completed==");
			}
		});
		
		camelContext.start();
	}

}
