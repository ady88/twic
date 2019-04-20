package com.adrian.twic;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.adrian.twic.rest.TwicRest;

@Configuration
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		register(TwicRest.class);
	}
}
