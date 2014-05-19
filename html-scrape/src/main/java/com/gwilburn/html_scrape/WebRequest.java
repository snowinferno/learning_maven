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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Greg on 5/13/14.
 */
public class WebRequest {
    private static Logger logger = LogManager.getLogger(WebRequest.class.getName());
    private HashMap<String,String> parameters;
    private String webProtocol;
    private String host;
    private String path;

    public WebRequest(String url) {
        if( url.startsWith("http") ){
            webProtocol = url.toLowerCase().split(":")[0];
            url = url.split("://")[1];
        }
        String[] parts = url.split("/");
        this.host = parts[0];
        for( int i = 1; i < parts.length; i++ ){
            this.path += parts[i];
        }
    }

    public WebRequest(){

    }

    public String getWebProtocol() {
        return webProtocol;
    }

    public void setWebProtocol(protocol webProtocol) {
        this.webProtocol = webProtocol.name().toLowerCase();
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public WebRequest addParameter(String key, String value){
        if( this.parameters == null )
            this.parameters = new HashMap<>();
        this.parameters.put(key, value);
        return this;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void makeFluentRequest(String protocol, String host, String path, HashMap<String,String> params) throws URISyntaxException, IOException {
        this.webProtocol = protocol;
        this.host = host;
        this.path = path;
        this.parameters = params;
        this.makeFluentRequest();
    }

    public void makeFluentRequest() throws URISyntaxException, IOException {
        checkRequestPieces();
        URIBuilder builder = new URIBuilder();
        builder.setScheme(this.webProtocol).setHost(this.host).setPath(this.path);
        if( this.parameters != null ){
            Set<String> keys = this.parameters.keySet();
            for( String key : keys ){
                builder.addParameter(key,this.parameters.get(key));
            }
        }

        URI uri = builder.build();
        Response resp = Request.Get(uri).execute();
        Content content = resp.returnContent();
        logger.info(content);
    }

    public void makeManualHTTPRequest(String protocol, String host, String path, HashMap<String, String> params) throws URISyntaxException, IOException {
        this.webProtocol = protocol;
        this.host = host;
        this.path = path;
        this.parameters = params;
        this.makeManualHTTPRequest();
    }

    public void makeManualHTTPRequest() throws URISyntaxException, IOException {
        checkRequestPieces();
        URIBuilder builder = new URIBuilder();
        builder.setScheme(this.webProtocol).setHost(host).setPath(path);
        if (this.parameters != null) {
            Set<String> keys = this.parameters.keySet();
            for (String key : keys) {
                builder.addParameter(key, this.parameters.get(key));
            }
        }

        URI uri = builder.build();
        HttpGet getReq = new HttpGet(uri);

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(getReq);

        HttpEntity entity = response.getEntity();

        String content = EntityUtils.toString(entity);

        logger.info(content);
        EntityUtils.consume(entity);
        response.close();
    }

    private void checkRequestPieces() throws URISyntaxException {
        if (this.host == null || this.path == null || this.webProtocol == null) {
            String providedURL = this.webProtocol + "://" + this.host + "/" + this.path;

            if (this.parameters == null)
                providedURL += "?" + this.parameters;
            else {
                providedURL += "?";
                Set<String> keys = this.parameters.keySet();
                for (String key : keys) {
                    providedURL += key + "=" + this.parameters.get(key) + "&";
                }
                providedURL = providedURL.replaceAll("&$", "");
            }

            String reason = "";
            if (this.host == null)
                reason += "Host is null. ";
            if (this.path == null)
                reason += "Path is null. ";
            if (this.webProtocol == null)
                reason += "Protocol is null. ";

            throw new URISyntaxException(providedURL, reason);
        }
    }

    public enum protocol {
        HTTP,
        HTTPS
    }
}
