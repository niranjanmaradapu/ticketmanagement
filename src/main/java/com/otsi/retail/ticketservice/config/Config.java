package com.otsi.retail.ticketservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
	
	@Value("${get_clients_from_urm}")
	private String getClientsByUserIdFromUrm;
	

}
