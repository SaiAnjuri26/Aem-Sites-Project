package com.task01.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
//import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true)
@SlingServletPaths(value = { "/bin/sai/shyam" })
public class PraticeServletArticleUser extends SlingSafeMethodsServlet {
    private static final Logger log = LoggerFactory.getLogger(PraticeServletArticleUser.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        log.info("Servlet is Working Fine");
        response.getWriter().write("responseComes inside the doGet Serlet");
    }

}
