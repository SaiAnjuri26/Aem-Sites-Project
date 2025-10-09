package com.task01.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomCoponent {
  private static final Logger log = LoggerFactory.getLogger(CustomCoponent.class);
    @ValueMapValue
    private String name;

    public String getName() {
        return name;
    }

}
