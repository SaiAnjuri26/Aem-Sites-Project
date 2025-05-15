package com.task01.core.servlets.Dao;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class UserDaoImportServlet {

    private static final String API_URL = "https://gorest.co.in/public/v2/users";
    private static final String ACCESS_TOKEN = "Bearer YOUR_ACCESS_TOKEN_HERE"; // Replace with your actual token

    public String postUserData(Map<String, String> userData) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", ACCESS_TOKEN);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            String jsonInputString = new Gson().toJson(userData);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int status = con.getResponseCode();
            InputStream is = (status < HttpURLConnection.HTTP_BAD_REQUEST)
                    ? con.getInputStream()
                    : con.getErrorStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseStr = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                responseStr.append(line);
            }

            in.close();
            con.disconnect();
            return responseStr.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
