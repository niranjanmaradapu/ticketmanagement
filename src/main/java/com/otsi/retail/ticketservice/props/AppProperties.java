/**
 * 
 */
package com.otsi.retail.ticketservice.props;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author Sudheer.Swamy
 *
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ticket-app")
public class AppProperties {

	private Map<String, String> messages = new HashMap<>();

}
