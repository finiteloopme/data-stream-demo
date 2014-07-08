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

import com.kunal.data.stream.cache.Tweet;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
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

	private static final String PARENT_CACHE_LIST = "parent-cache-list";
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
	
		List<String> keys = this.getAllKeys();
		if(!keys.contains(key)){
			BasicCache<String, String> cache = this.getCachePOJO(PARENT_CACHE_LIST);
			cache.put(PARENT_CACHE_LIST_KEY, key);
		}
		BasicCache<String, String> cache = this.getCachePOJO(key);
		cache.put(key, eventData);
		
		//TODO: change to created()
		return Response.ok().build();
	}

	@GET
	@Produces("application/json")
	@Path("/keys")
	public List<String> getAllKeys(){
		BasicCache<String, String> cache = 
				provider.getCacheContainer().getCache(PARENT_CACHE_LIST);
		
		List<String> keys = (cache==null || cache.values()==null) ? 
				new ArrayList<String>() : 
					new ArrayList<String>((cache.values()));
		logger.info(keys.toString());
		return keys;
	}
	
	private BasicCache<String, String> getCachePOJO(String key){
		return provider.getCacheContainer().getCache(key);
	}
	
	@GET
	@Produces("application/json")
	@Path("/key/{key}")
	public List<String> getCache(@PathParam("key") String key){
		BasicCache<String, String> cache = this.getCachePOJO(key);
		List<String> values = (cache==null || cache.values()==null) ? 
				new ArrayList<String>() : 
					new ArrayList<String>(cache.values());
		logger.info(values.toString());
		
		return values;
	}
	
}
