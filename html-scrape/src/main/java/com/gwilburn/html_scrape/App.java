package com.gwilburn.html_scrape;

import java.util.HashMap;

/**
 * Beginnings of an HTML Scraper.
 */
public class App {
    public static void main(String[] args) {
        try {
            String protocol = "https";
            String webHost = "www.google.com";
            String webPath = "/search";
            HashMap<String, String> params = new HashMap<>();
            params.put("q","lmgtfy");

            WebRequest request = new WebRequest();
            request.setWebProtocol(WebRequest.protocol.HTTPS);
            request.addParameter("q","lmgtfy");
            System.out.println("FLUENT REQUEST: ");
            request.makeFluentRequest(protocol,webHost,webPath,params);
            System.out.println("\n\nMANUAL REQUEST: ");
            request.makeManualHTTPRequest(protocol, webHost, webPath, params);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

}
