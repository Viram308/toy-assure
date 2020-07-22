package com.assure.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.assure")
@PropertySources({ //
        @PropertySource(value = "file:./dbConfig.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {

}
