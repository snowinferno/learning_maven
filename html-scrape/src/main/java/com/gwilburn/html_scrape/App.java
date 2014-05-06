package com.gwilburn.html_scrape;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

/**
 * Beginnings of an HTML Scraper. Currently the manual method fails on chunked responses.
 */
public class App {
    public static void main(String[] args) {
//        System.out.println("Hello World!");
        try {
            String protocol = "https";
            String webHost = "www.google.com";
            String webPath = "/search";
            HashMap<String, String> params = new HashMap<>();
            params.put("q","lmgtfy");
            makeHTTPRequest(protocol, webHost, webPath, params);
            makeManualHTTPRequest(protocol, webHost, webPath, params);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void makeHTTPRequest(String protocol, String host, String path, HashMap<String,String> params) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(protocol).setHost(host).setPath(path);
        if( params != null ){
            Set<String> keys = params.keySet();
            for( String key : keys ){
                builder.addParameter(key,params.get(key));
            }
        }

        URI uri = builder.build();
        Response resp = Request.Get(uri).execute();
//        HttpResponse response = resp.returnResponse();
//        System.out.println(response.getStatusLine().getStatusCode());
        Content content = resp.returnContent();
        System.out.println(content);
    }

    public static void makeManualHTTPRequest(String protocol, String host, String path, HashMap<String,String> params) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(protocol).setHost(host).setPath(path);
        if( params != null ){
            Set<String> keys = params.keySet();
            for( String key : keys ){
                builder.addParameter(key,params.get(key));
            }
        }

        URI uri = builder.build();
        HttpGet getReq = new HttpGet(uri);

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(getReq);

        HttpEntity entity = response.getEntity();

        String content = EntityUtils.toString(entity);

        System.out.println(content);
        EntityUtils.consume(entity);
        response.close();
    }
}
