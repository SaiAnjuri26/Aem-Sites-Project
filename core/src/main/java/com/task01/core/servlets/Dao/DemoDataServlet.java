package com.task01.core.servlets.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Demo Data Servlet",
        "sling.servlet.paths=/bin/demo/data",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET + "," + HttpConstants.METHOD_POST
})
public class DemoDataServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DemoDataServlet.class);

    private static final String BASE_PATH = "/content/demo"; // Always under /content
    private static final String SUBSERVICE = "saishyam";

    @Reference
    private ResourceResolverFactory resolverFactory;

    private String readBody(SlingHttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ResourceResolver resolver = null;

        try {
            String body = readBody(request);
            if (body == null || body.trim().isEmpty()) {
                response.setStatus(400);
                response.getWriter().write(new JSONObject()
                        .put("status", "error")
                        .put("message", "Empty request body")
                        .toString());
                return;
            }

            JSONObject json = new JSONObject(body);
            String id = json.optString("id", "").trim();
            String name = json.optString("name", "").trim();
            String salary = json.optString("salary", "").trim();

            if (id.isEmpty() || name.isEmpty() || salary.isEmpty()) {
                response.setStatus(400);
                response.getWriter().write(new JSONObject()
                        .put("status", "error")
                        .put("message", "id, name, and salary are required")
                        .toString());
                return;
            }

            Map<String, Object> authInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
            resolver = resolverFactory.getServiceResourceResolver(authInfo);

            if (resolver == null) {
                throw new IllegalStateException(
                        "Could not get service resource resolver for subservice: " + SUBSERVICE);
            }

            Session session = resolver.adaptTo(Session.class);
            if (session == null) {
                throw new IllegalStateException("Could not get JCR session from ResourceResolver");
            }

            // Ensure /content exists
            Node root = session.getRootNode();
            Node contentNode;
            if (root.hasNode("content")) {
                contentNode = root.getNode("content");
            } else {
                contentNode = root.addNode("content", "sling:Folder");
            }

            // Ensure /content/demo exists
            Node demoNode;
            if (contentNode.hasNode("demo")) {
                demoNode = contentNode.getNode("demo");
            } else {
                demoNode = contentNode.addNode("demo", "nt:unstructured");
            }

            // Create or update node
            Node targetNode;
            if (demoNode.hasNode(name)) {
                targetNode = demoNode.getNode(name);
                targetNode.setProperty("id", id);
                targetNode.setProperty("salary", salary);
                response.setStatus(200);
                response.getWriter().write(new JSONObject()
                        .put("status", "success")
                        .put("message", "Node updated for: " + name)
                        .toString());
            } else {
                targetNode = demoNode.addNode(name, "nt:unstructured");
                targetNode.setProperty("id", id);
                targetNode.setProperty("name", name);
                targetNode.setProperty("salary", salary);
                response.setStatus(201);
                response.getWriter().write(new JSONObject()
                        .put("status", "success")
                        .put("message", "Node created: " + name)
                        .toString());
            }

            session.save();

        } catch (Exception e) {
            log.error("Error saving demo data", e);
            sendErrorJson(response, e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ResourceResolver resolver = null;

        try {
            Map<String, Object> authInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
            resolver = resolverFactory.getServiceResourceResolver(authInfo);

            if (resolver == null) {
                throw new IllegalStateException(
                        "Could not get service resource resolver for subservice: " + SUBSERVICE);
            }

            Session session = resolver.adaptTo(Session.class);
            JSONArray submissions = new JSONArray();

            if (session.nodeExists(BASE_PATH)) {
                Node parent = session.getNode(BASE_PATH);
                NodeIterator iterator = parent.getNodes();
                while (iterator.hasNext()) {
                    Node node = iterator.nextNode();
                    JSONObject obj = new JSONObject();
                    if (node.hasProperty("id"))
                        obj.put("id", node.getProperty("id").getString());
                    if (node.hasProperty("name"))
                        obj.put("name", node.getProperty("name").getString());
                    if (node.hasProperty("salary"))
                        obj.put("salary", node.getProperty("salary").getString());
                    submissions.put(obj);
                }
            }

            JSONObject result = new JSONObject();
            result.put("status", "success");
            result.put("submissions", submissions);
            response.getWriter().write(result.toString());

        } catch (Exception e) {
            log.error("Error reading demo nodes", e);
            sendErrorJson(response, e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }

    private void sendErrorJson(SlingHttpServletResponse response, Exception e) throws IOException {
        response.setStatus(500);
        try {
            JSONObject err = new JSONObject();
            err.put("status", "error");
            err.put("message", e.getMessage() != null ? e.getMessage() : e.toString());
            response.getWriter().write(err.toString());
        } catch (Exception jsonEx) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Unexpected error\"}");
        }
    }
}
