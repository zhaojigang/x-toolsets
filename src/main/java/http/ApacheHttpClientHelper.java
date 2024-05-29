package http;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * apache http client helper
 *
 * @author zhaojigang
 */
public class ApacheHttpClientHelper {
    private static final CloseableHttpClient CLIENT;

    static {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000) // 连接超时时间
                .setConnectionRequestTimeout(5000) // 请求超时时间
                .setSocketTimeout(5000) // 读取超时时间
                .build();
        CLIENT = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }

    public static <T> String post(HttpRequest<T> request) {
        HttpPost httpPost = new HttpPost(request.getUrl());
        if (MapUtils.isNotEmpty(request.getHeaders())) {
            request.getHeaders().forEach(httpPost::setHeader);
        }
        if (request.getBody() != null) {
            httpPost.setEntity(new StringEntity(JSON.toJSONString(request.getBody()), "UTF-8"));
        }
        try (CloseableHttpResponse response = CLIENT.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            if (!success(response) || entity == null) {
                throw new RuntimeException("http request error");
            }
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("http request error");
        }
    }

    public static <T> String get(HttpRequest<T> request) {
        HttpGet httpGet = new HttpGet(buildUri(request.getUrl(), request.getParams()));
        if (MapUtils.isNotEmpty(request.getHeaders())) {
            request.getHeaders().forEach(httpGet::setHeader);
        }
        try (CloseableHttpResponse response = CLIENT.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (!success(response) || entity == null) {
                throw new RuntimeException("http request error");
            }
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("http request error");
        }
    }

    private static URI buildUri(String url, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (MapUtils.isNotEmpty(params)) {
                params.forEach(uriBuilder::setParameter);
            }
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean success(CloseableHttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode >= 200 && statusCode < 300;
    }

    @Accessors(chain = true)
    @Data
    public static class HttpRequest<T> {
        private String url;
        private Map<String, String> headers;
        private Map<String, String> params;
        private T body;
    }
}
