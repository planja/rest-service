package rest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import service.config.ServiceConfig;

@Configuration
@ComponentScan( {"rest"} )
@Import( {ServiceConfig.class} )
public class RestConfig {


}
