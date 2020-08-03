package com.assure.spring;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(//
        basePackages = { "com.assure" },
        excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
        @PropertySource(value = "classpath:./com/assure/test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {


}
