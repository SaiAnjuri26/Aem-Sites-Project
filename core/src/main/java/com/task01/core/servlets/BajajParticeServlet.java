package com.task01.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true)
@SlingServletResourceTypes(resourceTypes = { "sai/shyam/content" }, extensions = { "json" })
public class BajajParticeServlet extends SlingAllMethodsServlet {
    Logger log = LoggerFactory.getLogger(BajajParticeServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        log.info("servlets Working Fine {}", request);
        response.getWriter().write("maxy data comes inside the {}");
    }
}
