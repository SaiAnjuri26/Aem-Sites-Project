package com.task01.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ExporterConstants;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(extensions = ExporterConstants.SLING_MODEL_EXTENSION, name = ExporterConstants.SLING_MODEL_EXPORTER_NAME)
public class BajajPraticeCode {
    @ValueMapValue
    private String name;

    public String getName() {
        return name;
    }

}
