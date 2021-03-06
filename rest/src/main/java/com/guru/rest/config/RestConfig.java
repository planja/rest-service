package com.guru.rest.config;

import com.guru.service.config.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.guru.rest"})
@Import({ServiceConfig.class})
public class RestConfig {

}
