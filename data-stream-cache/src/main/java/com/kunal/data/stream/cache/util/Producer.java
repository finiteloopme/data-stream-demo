/**
 * 
 */
package com.kunal.data.stream.cache.util;

/**
 * @author kunallimaye
 *
 */
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

public class Producer {

	@Produces
	public static Logger produceLog(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
				.getName());
	}
}
