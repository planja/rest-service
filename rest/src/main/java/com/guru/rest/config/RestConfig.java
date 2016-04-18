package com.guru.rest.config;

import com.guru.domain.config.DataConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.guru.service.config.ServiceConfig;

@Configuration
@ComponentScan( {"com.guru.rest"} )
@Import( {ServiceConfig.class} )
public class RestConfig {

}
