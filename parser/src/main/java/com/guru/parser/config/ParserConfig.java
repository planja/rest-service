package com.guru.parser.config;

import com.guru.parser.dl.DLParser;
import com.guru.parser.impl.acparser.ACParser;
import com.guru.parser.impl.qfparser.QFParser;
import com.guru.parser.ke.KEParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfig {

    @Bean
    public QFParser qfParser(){
        return new QFParser();
    }

    @Bean
    public ACParser acParser(){
        return new ACParser();
    }

    @Bean
    public KEParser keParser(){
        return new KEParser();
    }

    @Bean
    public DLParser dlParser() {
        return new DLParser();
    }

}
