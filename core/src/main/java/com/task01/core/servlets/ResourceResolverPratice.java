package com.task01.core.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true)
@SlingServletResourceTypes(resourceTypes = "sai/shyam/resourceResolver/pratice", extensions = "json")
@SlingServletPaths(value = "/bin/sai/shyam")
public class ResourceResolverPratice extends SlingAllMethodsServlet {
    @Reference
    ResourceResolverFactory resourceResolverFactory;
    private static final Logger log = LoggerFactory.getLogger(ResourceResolverPratice.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        HashMap<String, Object> parMap = new HashMap<>();
        parMap.put(resourceResolverFactory.SUBSERVICE, "saishyam");
    }

}
