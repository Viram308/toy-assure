package com.channel.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.channel")
@PropertySources({ //
        @PropertySource(value = "file:./dbConfig.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {

}
