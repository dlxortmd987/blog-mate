package com.project.blogmate.infra.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RestClientInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RestClientInterceptor.class);

    static void logRequest(HttpRequest request, byte[] bytes) {
        log.info("Request: ${request.method} ${request.URI}");
        log.info("Headers: ${request.headers}");
        log.info("Body: ${new String(bytes)}");
    }

    static void logResponse(ClientHttpResponse response) {
        log.info("Response: ${response.statusCode} ${response.statusText}");
        log.info("Headers: ${response.headers}");
        log.info("Body: ${IOUtils.toString(response.body, Charset.forName('UTF-8')).length()}");
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // log the request
        logRequest(request, body);

        // execute the request
        ClientHttpResponse response = execution.execute(request, body);

        // wrap the response that buffers the body, so that we can read from it multiple times
        BufferingClientHttpResponseWrapper responseWrapper = new BufferingClientHttpResponseWrapper(response);

        // log the response
        logResponse(responseWrapper);

        // return the wrapped response
        return responseWrapper;
    }

    static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
        ClientHttpResponse response;
        byte[] body;

        BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
            this.response = response;
        }

        @Override
        public InputStream getBody() throws IOException {
            if (this.body == null) {
                this.body = StreamUtils.copyToByteArray(this.response.getBody());
            }
            return new ByteArrayInputStream(this.body);
        }

        @Override
        public HttpStatusCode getStatusCode() throws IOException {
            return response.getStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return response.getStatusText();
        }

        @Override
        public void close() {

        }

        @Override
        public HttpHeaders getHeaders() {
            return response.getHeaders();
        }
    }
}
