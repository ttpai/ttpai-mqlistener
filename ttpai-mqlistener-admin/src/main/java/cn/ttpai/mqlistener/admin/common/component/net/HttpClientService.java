package cn.ttpai.mqlistener.admin.common.component.net;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HttpClient service
 *
 * @author jiayuan.su
 */
public class HttpClientService {
    private HttpClient httpClient;


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

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
