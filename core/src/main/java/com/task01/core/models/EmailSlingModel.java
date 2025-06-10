package com.task01.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmailSlingModel {
    @ValueMapValue
    private String email;
    @ValueMapValue
    private String password;

    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
}
