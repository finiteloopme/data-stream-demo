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
	private final String DATA_STREAM_HOST	= "http://localhost:8080/";
//	private final String DATA_STREAM_HOST	= "http://cache-kunal.rhcloud.com/";
	
	/**
	 * 
	 */
	public CamelRoute() throws Exception {
		camelContext = new DefaultCamelContext();
		
	}
	
	public void configure(final String location) throws Exception{
		camelContext.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("twitter://"
						+ "streaming/filter?"
						// query execution type = polling
						+ "type=polling" + "&"
						// poll every 10 sec
						+ "delay=10" + "&"
						+ "locations=" + location
						+ "" + "&"
						+ "consumerKey=HjLbxF1IoN1bXENLDuMPbJsQT" + "&"
						+ "consumerSecret=62no3XxjQGrgWzASJXkyoW2L0Rs2Ba6Qi1OWFcwilgzOC1rWwW" + "&"
						+ "accessToken=229302807-NwkYedrDGdZ4CDxvoRcxVtH8klPqfC9Yxt0fD9Fn" + "&"
						+ "accessTokenSecret=its7D8zTq8GEgCzxHYeCYt4g9HtswwUFBLwrv0bHjngzJ")
					.log("\n====================\n" +
						 "\tAuthor: ${body.user.screenName}" + "\n" +
						 "\t-------" + "\n" +
						 "\t${body.text}" +"\n" +
						 "\tTweeted at place: ${body.place}\n" +
						 "\tTweeted at location: ${body.geoLocation}")
						 .marshal().json(JsonLibrary.Jackson)
						 .log("Marshalled to JSON:\n${body}");
//					.setHeader(Exchange.HTTP_METHOD, constant("POST"))
//					.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//					.to(DATA_STREAM_HOST
//							+ "data-stream-cache/"
//							+ "cache/"
//							+ "streaming-data/"
//							+ "new-event/"
//							+ "${body.place}/"
//							+ "${body}");
			}
		});
	}
	
//	public void configure(final String username, final String searchCriteria)
	public void configure(final String username, final String searchCriteria)
		throws Exception{
		
		camelContext.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("twitter://"
						// query type = search
//						+ "search?"
						+ "streaming/filter?"
						// query execution type = polling
						+ "type=polling" + "&"
						// poll every 10 sec
						+ "delay=10" + "&"
//						+ "keywords=" + searchCriteria + "&"
						+ "locations=144.94781,-37.82331;144.98121,-37.80301" + "&"
						+ "consumerKey=HjLbxF1IoN1bXENLDuMPbJsQT" + "&"
						+ "consumerSecret=62no3XxjQGrgWzASJXkyoW2L0Rs2Ba6Qi1OWFcwilgzOC1rWwW" + "&"
						+ "accessToken=229302807-NwkYedrDGdZ4CDxvoRcxVtH8klPqfC9Yxt0fD9Fn" + "&"
						+ "accessTokenSecret=its7D8zTq8GEgCzxHYeCYt4g9HtswwUFBLwrv0bHjngzJ")
					.log("\n====================\n" +
						 "\tAuthor: ${body.user.screenName}" + "\n" +
						 "\t-------" + "\n" +
						 "\t${body.text}" +"\n" +
						 "\tTweeted at place: ${body.place}\n" +
						 "\tTweeted at location: ${body.geoLocation}")
						 .marshal().json(JsonLibrary.Jackson)
						 .log("Marshalled to JSON:\n${body}")
					.setHeader(Exchange.HTTP_METHOD, constant("POST"))
					.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//					.setBody(body().prepend(""
//								+ "{"
//								+ "\"username\":" + "\"" + username + "\", "
//								+ "\"searchCriteria\":" + "\"" + searchCriteria + "\", "
//								+ "\"tweet\":" + "\""
//								+ ""))
//					.setBody(body().append(""
//							+ "\""
//							+ "}"
//							+ ""))
					.to(DATA_STREAM_HOST
							+ "data-stream-cache/"
							+ "cache/"
							+ "cache/"
							+ "");
			}
		});
		
		camelContext.start();
	}

}
