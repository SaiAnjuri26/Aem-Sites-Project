package com.task01.core.utility;

import java.util.HashMap;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

public class ResourceResolverUtility {

public static final String  saishyam = "saishyam";
public static ResourceResolverFactory newResolver(ResourceResolverFactory resourceResolverFactory){
    HashMap<String, String> parmMap = new HashMap<>();
    parmMap.put(ResourceResolverFactory.SUBSERVICE, saishyam);

    

    return resourceResolverFactory;
}
}

