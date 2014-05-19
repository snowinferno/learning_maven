package com.gwilburn.html_scrape;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Beginnings of an HTML Scraper.
 */
public class App {
    private static Logger logger = LogManager.getLogger(App.class.getName());
    public static void main(String[] args) {
        try {
            String protocol = "https";
            String webHost = "www.google.com";
            String webPath = "/search";
            HashMap<String, String> params = new HashMap<>();
            params.put("q","lmgtfy");

            WebRequest request = new WebRequest();
            request.setWebProtocol(WebRequest.protocol.HTTPS);
            request.addParameter("q", "lmgtfy");
            logger.trace("FLUENT REQUEST: ");

            request.makeFluentRequest(protocol, webHost, webPath, params);
            logger.trace("MANUAL REQUEST: ");

            request.makeManualHTTPRequest(protocol, webHost, webPath, params);
        } catch (Exception e) {
            logger.error("ERROR SCRAPING HTML", e);
        }
    }

}
