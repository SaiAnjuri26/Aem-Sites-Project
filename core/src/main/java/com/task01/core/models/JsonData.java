package com.task01.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
//import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, resourceType = "task01/components/jsondata", // Required!
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonData {
    @ValueMapValue
    private String text;

    private String name;

    @PostConstruct
    protected void init() {
        if (text != null) {
            name = text.toUpperCase();
        }
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }
}