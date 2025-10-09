package com.task01.core.models;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ExporterConstants;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(extensions = ExporterConstants.SLING_MODEL_EXTENSION, name = ExporterConstants.SLING_MODEL_EXPORTER_NAME)
public class DemoSlingModelPractice {
    @ValueMapValue
    private String title;
    @ValueMapValue
    private String description;
    @ValueMapValue
    private String articleImage;
    @ValueMapValue
    private Date expiry;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public boolean articleExperied = false;

    public boolean getArticleExperied() {
        return articleExperied;
    }

    @PostConstruct
    public void init() {
        Date today = new Date();
        if (expiry != null && expiry.compareTo(today) < 0) {
            articleExperied = true;
        }
    }

}
