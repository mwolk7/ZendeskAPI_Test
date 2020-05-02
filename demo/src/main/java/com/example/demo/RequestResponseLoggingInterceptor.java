package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

@Configuration
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = Logger.getLogger( RequestResponseLoggingInterceptor.class.getName() );

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        LOG.info("===========================request begin================================================");
        LOG.info("URI         : {}"+ request.getURI());
        LOG.info("Method      : {}"+ request.getMethod());
        LOG.info("Headers     : {}"+ request.getHeaders());
        LOG.info("Request body: {}"+ new String(body, "UTF-8"));
        LOG.info("==========================request end================================================");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        LOG.info("============================response begin==========================================");
        LOG.info("Status code  : {}"+ response.getStatusCode());
        LOG.info("Status text  : {}"+ response.getStatusText());
        LOG.info("Headers      : {}"+ response.getHeaders());
        LOG.info("Response body: {}"+ StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        LOG.info("=======================response end=================================================");
    }
}