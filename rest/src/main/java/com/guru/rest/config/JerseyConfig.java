package com.guru.rest.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages("com.guru");
        register(JacksonJsonProvider.class);
    }
}
