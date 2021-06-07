package cn.ttpai.mqlistener.admin.common.component.net;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient工厂类
 *
 * @author jiayuan.su
 */
public class HttpClientFactory implements FactoryBean<HttpClient> {

    private int maxTotal = 200;
    private int maxPerRoute = 50;

    private int connectionRequestTimeout = 2000;
    private int connectTimeout = 3000;
    private int socketTimeout = 5000;
    private long connectionTimeToLive = 60000;
    private long evictIdleConnections = 30000;

    private List<CredentialItem> credentialItems;

    @Override
    public HttpClient getObject() throws Exception {
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

        if (CollectionUtils.isNotEmpty(credentialItems)) {
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            for (CredentialItem credentialItem : credentialItems) {
                credentialsProvider.setCredentials(new AuthScope(credentialItem.getIp(), credentialItem.getPort()),
                        new UsernamePasswordCredentials(credentialItem.getUsername(), credentialItem.getPassword()));
            }
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }

        return httpClientBuilder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setConnectionTimeToLive(long connectionTimeToLive) {
        this.connectionTimeToLive = connectionTimeToLive;
    }

    public void setEvictIdleConnections(long evictIdleConnections) {
        this.evictIdleConnections = evictIdleConnections;
    }

    public void setCredentialItems(List<CredentialItem> credentialItems) {
        this.credentialItems = credentialItems;
    }
}
