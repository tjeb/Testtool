/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.tools.TTSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

@Service("smpMessagingService")
public class SMPMessagingService {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private TTSettings ttSettings;

    public String makePostRequest(String stringToSend_, String url) {
        return makeRequest(stringToSend_, url, "POST");
    }

    public String makeGetRequest(String url) {
        return makeRequest("", url, "GET");
    }

    public String makeDeleteRequest(final String url) {
        return makeRequest("", url, "DELETE");
    }

    private String makeRequest(final String stringToSend_, final String url, final String requestMethod_) {
        final StringBuilder response = new StringBuilder();
        BufferedReader in = null;
        HttpURLConnection connection = null;
        try {
            URL urlObject = new URL(url);
            connection = (HttpURLConnection) urlObject.openConnection();

            connection.setRequestMethod(requestMethod_);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Token", ttSettings.smpToken);
            connection.setDoOutput(true);

            logger.info("HTTP Request POST message: " + stringToSend_.toString());

            if (requestMethod_.equals("POST")) {
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(stringToSend_);
                wr.flush();
                wr.close();
            }

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            logger.info("HTTP Response POST message: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.severe("An error has occurred while trying to make request: " + e.getMessage());

            try {
                System.err.println("ErrorCode : " + connection.getResponseCode());
                System.err.println("Response Message : " + connection.getResponseMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }

        return response.toString();
    }
}