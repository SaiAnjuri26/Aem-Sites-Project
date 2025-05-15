package com.task01.core.servlets;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonReader;
// Add this import
//import java.io.StringReader;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = { Servlet.class }, property = { "sling.servlet.methods=GET",
        "sling.servlet.paths=/bin/showUsers",
        "sling.servlet.extensions=json"
}, immediate = true)
public class UrlServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String apiUrl = "https://gorest.co.in/public/v2/users";
        StringBuilder result = new StringBuilder();

        try {
            // pw = response.getWriter();
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {

                result.append(line);

            }
            reader.close();

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("Error fetching data: " + e.getMessage());
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result.toString());

    }

}
