package com.guru.parser.config;

import com.guru.parser.impl.qfparser.QFParser;
import com.guru.parser.interf.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ParserConfig {

    @Bean
    public QFParser qfParser(){
        return new QFParser();
    }

}
