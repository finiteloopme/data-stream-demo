/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kunal.data.stream.cache.util;

import org.infinispan.commons.api.BasicCache;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Adds, retrieves, removes new cars from the cache. Also returns a list of cars
 * stored in the cache.
 * 
 * @author Martin Gencur
 * 
 */
@Stateless
@Path("/streaming-data")
public class StreamManager {
	@Inject
	private CacheContainerProvider provider;
	
	@Inject
	private Logger logger;

//	private static final String PARENT_CACHE_LIST = "parent-cache-list";
	private static final String PARENT_CACHE_LIST_KEY = "parent-cache-list-key";

	public StreamManager() {
	}

//	@GET
//	@Produces("application/json")
//	@Path("/test")
//	public Tweet test() {
//		Tweet t = new Tweet();
//		t.setUsername("testingUserName");
//		t.setSearchCriteria("testingSearchCriteria");
//		t.setTweet("testingTweet");
//		newTweet(t);
//
//		// retrieve a cache
//		searchToTweetCache = provider.getCacheContainer().getCache(
//				SEARCH_TO_TWEETS_CACHE);
//		// retrieve a list of number plates from the cache
//		return ((List<Tweet>) searchToTweetCache.get(encode(t
//				.getSearchCriteria()))).get(0);
//
//	}
	
	@POST
	@Consumes("application/json")
	@Path("/new-event/{key}/{eventData}")
	public Response newEvent(@PathParam("key") String key,
							@PathParam("eventData") String eventData){
	
		logger.info("===: newEvent(" + key +"," + eventData +  ") :===");
		List<String> keys = this.getAllKeys();
		if(!keys.contains(key)){
			putKeyAndValueIntoCache(PARENT_CACHE_LIST_KEY, key);
		}
		
		putKeyAndValueIntoCache(key,  eventData);
		
		//TODO: change to created()
		return Response.ok().build();
	}
	
	@POST
	@Consumes("application/json")
	@Path("/new-eventt/{location}")
	public Response newEventT(@PathParam("location") String key, String eventData){
	
		logger.info("===: newEventT(" + key +"," + eventData +  ") :===");
		List<String> keys = this.getAllKeys();
		if(!keys.contains(key)){
			putKeyAndValueIntoCache(PARENT_CACHE_LIST_KEY, key);
		}
		
		putKeyAndValueIntoCache(key,  eventData);
		
		//TODO: change to created()
		return Response.ok().build();
	}
	
	private BasicCache<String, List<String>> putKeyAndValueIntoCache(String key, String value){
		BasicCache<String, List<String>> cache = this.getCachePOJO(key);
		List<String> events = cache.get(key);
		if(events == null){
			events = new ArrayList<String>();
		}
		events.add(value);
		cache.put(key, events);
		
		return cache;
	}

	@GET
	@Produces("application/json")
	@Path("/keys")
	public List<String> getAllKeys(){
		logger.info("===: getAllKeys() :===");
		return getCache(PARENT_CACHE_LIST_KEY);
	}
	
	private BasicCache<String, List<String>> getCachePOJO(String key){
		return provider.getCacheContainer().getCache(key);
	}
	
	@GET
	@Produces("application/json")
	@Path("/key/{key}")
	public List<String> getCache(@PathParam("key") String key){
		
		List<String> values = new ArrayList<String>();;  
		logger.info("===: getCache(" + key + ") :===");
		BasicCache<String, List<String>> cache = this.getCachePOJO(key);
		
		if(cache!=null && !cache.isEmpty()){
			logger.info("Cache has " + cache.keySet().size() +  " number of keys");
			logger.info("Cache has " + cache.values().size() +  " number of values");
			Iterator<String> iterator = cache.keySet().iterator();
			while(iterator.hasNext()){
				values.addAll(convertCollectionToList(cache.get(iterator.next())));
			}
		}
				
		logger.info(values.toString());
		
		return values;
	}
	
	private List<String> convertCollectionToList(Collection<String> collection){
		List<String> list;
		if(collection==null){
			collection = new ArrayList<String>();
		}
		if(collection instanceof List){
			list = (List<String>) collection;
		}
		else{
			list = new ArrayList<String>(collection);
		}
		return list;
	}
	
}
