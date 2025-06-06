package com.task01.core.servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.task01.core.servlets.Dao.UserDaoImportServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component(service = Servlet.class, immediate = true, property = {
        Constants.SERVICE_DESCRIPTION + "=Shyam's User Import Servlet",
        "sling.servlet.resourceTypes=shyam/users/featchingwith/servlet/api",
        "sling.servlet.methods=GET",
        "sling.servlet.selectors=importusers",
        "sling.servlet.extensions=json"
})
public class UserImportServlet extends SlingAllMethodsServlet {

    private static final String API_URL = "https://gorest.co.in/public/v2/users";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        ResourceResolver resolver = request.getResourceResolver();
        Resource parent = resolver.getResource("/content/shyamsusersdata");

        if (parent == null) {
            response.getWriter().write("Parent node /content/shyamsusersdata not found.");
            return;
        }

        String json = fetchUserData();

        if (json == null) {
            response.getWriter().write("Failed to fetch user data.");
            return;
        }

        Type listType = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        List<Map<String, String>> users = new Gson().fromJson(json, listType);

        try {
            for (Map<String, String> user : users) {
                String userId = String.valueOf(user.get("id"));
                String nodeName = "user-" + userId;

                Resource existingResource = parent.getChild(nodeName);

                // Avoid duplicate creation
                if (existingResource == null) {
                    LinkedHashMap<String, Object> props = new LinkedHashMap<>();
                    props.put("jcr:primaryType", "nt:unstructured");
                    props.put("name", user.get("name"));
                    props.put("email", user.get("email"));
                    props.put("gender", user.get("gender"));
                    props.put("status", user.get("status"));

                    resolver.create(parent, nodeName, props);
                } else {
                    ModifiableValueMap map = existingResource.adaptTo(ModifiableValueMap.class);
                    if (map != null) {
                        map.put("name", user.get("name"));
                        map.put("email", user.get("email"));
                        map.put("gender", user.get("gender"));
                        map.put("status", user.get("status"));

                    }
                }
            }

            resolver.commit();
            response.getWriter().write("User nodes created/updated under /content/shyamsusersdata.");
        } catch (PersistenceException e) {
            response.getWriter().write("Error saving nodes: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        LinkedHashMap<String, String> newUser = new LinkedHashMap<String, String>();
        newUser.put("name", "Sai Shyam");
        newUser.put("email", "SaiShyam" + System.currentTimeMillis() + "@email.com");
        newUser.put("gender", "male");
        newUser.put("status", "activte");

        UserDaoImportServlet user1 = new UserDaoImportServlet();
        String apiResponse = user1.postUserData(newUser);

        response.setContentType("application/json");
        response.getWriter().write(apiResponse != null ? apiResponse : "Error creating user via API.");

    }

    private String fetchUserData() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            if (status != 200)
                return null;

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();
            return content.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
