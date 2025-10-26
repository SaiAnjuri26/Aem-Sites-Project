package com.task01.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
//import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemds.guide.service.JsonObjectCreator;

@Component(service = Servlet.class, immediate = true)
@SlingServletPaths("/bin/sai/shyam/userInfo")
public class PraticeServlet extends SlingAllMethodsServlet {

    @Reference
    ResourceResolverFactory resourceResolverFactory;
    private static final Logger Log = LoggerFactory.getLogger(PraticeServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource userResource = resourceResolver.getResource("/content/userInfo");

        if (userResource != null) {
            Iterator<Resource> childUserList = userResource.listChildren();
            JsonArrayBuilder userArrayList = Json.createArrayBuilder();
            while (childUserList.hasNext()) {
                Resource childUserResource = (Resource) childUserList.next();
                ValueMap properties = childUserResource.getValueMap();
                String firstName = properties.get("firstName", String.class);
                String lastName = properties.get("lastName", String.class);
                String email = properties.get("email", String.class);
                String phoneNumber = properties.get("phoneNumber", String.class);

                // JsonObjectBuilder userJson = Json.createObjectBuilder();
                JsonObjectBuilder userJson = Json.createObjectBuilder();
                userJson.add("firstName: ", firstName);
                userJson.add("lastName: ", lastName);
                userJson.add("email: ", email);
                userJson.add("phoneNumber: ", phoneNumber);
                userArrayList.add(userJson);
            }
            response.getWriter().write(userArrayList.build().toString());
        }
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        Log.info("doPost is Executing >>> servlet is Registered");
        // ResourceResolver resourceResolver = request.getResourceResolver();
        // Resource userResource = resourceResolver.getResource("/content/userInfo");
        // Log.info("userResource>>>", userResource);
        // String userId = request.getParameter("userId");
        // if (userId != null) {
        // Log.info("request is entered into the Post method >>>> {}", userId);
        // HashMap<String, Object> userDetails = new HashMap<String, Object>();
        // userDetails.put("firstName", request.getParameter("firstName"));
        // userDetails.put("lastName", request.getParameter("lastName"));
        // userDetails.put("email", request.getParameter("email"));
        // userDetails.put("phoneNumber", request.getParameter("phoneNumber"));
        // resourceResolver.create(userResource, userId, userDetails);
        // resourceResolver.commit();
        // response.getWriter().write("User Sucessfully Created..... userId--> {}" +
        // userId);

        // ResourceResolver resourceResolver = req.getResourceResolver();
        // Resource resource = resourceResolver.getResource("/content/userInfo");

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put(resourceResolverFactory.SUBSERVICE, "saishyam");
        try {
            ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
            Resource userResource = resourceResolver.getResource("/content/userInfo");
            String userId = request.getParameter("userId");
            if (userId != null) {
                HashMap<String, Object> userDetails = new HashMap<String, Object>();
                userDetails.put("firstName", request.getParameter("firstName"));
                userDetails.put("lastName", request.getParameter("lastName"));
                userDetails.put("email", request.getParameter("email"));
                userDetails.put("phoneNumber", request.getParameter("phoneNumber"));
                Resource uResource = resourceResolver.create(userResource, userId, userDetails);
                resourceResolver.commit();
                response.getWriter().write("user Created Sucessfully  {}" + userId);
            }

        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

}
