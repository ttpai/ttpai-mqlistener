package cn.ttpai.mqlistener.admin.common.component.net;

import cn.ttpai.mqlistener.admin.common.util.JsonUtil;
import cn.ttpai.mqlistener.admin.rabbit.constant.RabbitConst;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author jiayuan.su
 */
public class RabbitMQTest {

    private int maxTotal = 200;
    private int maxPerRoute = 50;

    private int connectionRequestTimeout = 2000;
    private int connectTimeout = 3000;
    private int socketTimeout = 5000;
    private long connectionTimeToLive = 60000;
    private long evictIdleConnections = 30000;

    private HttpClient httpClient;

    @Before
    public void before() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(maxPerRoute);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout).build();

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(connManager);
        httpClientBuilder.setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy());
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setConnectionTimeToLive(connectionTimeToLive, TimeUnit.MILLISECONDS);
        httpClientBuilder.evictIdleConnections(evictIdleConnections, TimeUnit.MILLISECONDS);

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope("192.168.8.1", 15672), new UsernamePasswordCredentials("admin", "123456"));
        credentialsProvider.setCredentials(new AuthScope("192.168.8.1", 15672), new UsernamePasswordCredentials("admin", "123456"));
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        httpClient = httpClientBuilder.build();
    }

    @Test
    public void testSend() throws IOException {
        String url = String.format(RabbitConst.Url.PUBLISH_TPLT, "192.168.8.1:15672", "test");

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("properties", Collections.emptyMap());
        bodyMap.put("routing_key", "routingKey.test");
        bodyMap.put("payload", "2222111");
        bodyMap.put("payload_encoding", "string");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");

        post(url, JsonUtil.toJsonString(bodyMap), headerMap);
    }

    public String post(String url, String body, Map<String, String> headerMap) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        StringEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }

        return request(httpPost);
    }

    private String request(HttpRequestBase requestBase) throws IOException {
        HttpResponse response = httpClient.execute(requestBase);

        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }
}
