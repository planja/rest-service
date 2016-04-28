package com.guru.service.config;

import com.guru.domain.config.DataConfig;
import com.guru.parser.config.ParserConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.guru.service"})
@Import({DataConfig.class, Actors.class, ParserConfig.class})
public class ServiceConfig {

}
