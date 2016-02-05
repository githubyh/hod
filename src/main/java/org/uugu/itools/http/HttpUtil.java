package org.uugu.itools.http;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * Http访问工具类
 * @author Silence
 *
 */
public class HttpUtil {

    private HttpClient httpClient;
    private String url;
    private Map<String, String> params;
    private Map<String, String> headerParams;

    private static CloseableHttpClient closeableHttpClient;
    private static CloseableHttpResponse closeableHttpResponse;


    public HttpUtil() {
        httpClient = HttpClients.createDefault();
    }

    public HttpUtil(String url, Map<String, String> params, Map<String, String> headerParam) {
        httpClient = HttpClients.createDefault();
        this.url = url;
        this.params = params;
        this.headerParams = headerParam;
    }

    public String get() throws IOException {
        RequestBuilder builder = RequestBuilder.get().setUri(this.url);

        if(this.headerParams != null){
            for (String key : this.headerParams.keySet()) {
                builder.setHeader(key, this.headerParams.get(key));
            }
        }

        if (this.params != null) {
            for (String key : this.params.keySet()) {
                builder = builder.addParameter(key, this.params.get(key));
            }
        }

        HttpResponse response = httpClient.execute(builder.build());
        return EntityUtils.toString(response.getEntity());
    }

    /**
     *
     * @param uri   请求地址
     * @param params    参数的Map
     * @param headerParams  header参数Map。key为需要设置的header名，value为header值
     * @return
     * @throws IOException
     */
    public String get(String uri, Map<String, String> params, Map<String, String> headerParams) throws IOException {
        RequestBuilder builder = RequestBuilder.get().setUri(uri);

        if(headerParams != null){
            for (String key : headerParams.keySet()) {
                builder.setHeader(key, headerParams.get(key));
            }
        }

        if (params != null) {
            for (String key : params.keySet()) {
                builder = builder.addParameter(key, params.get(key));
            }
        }

        HttpResponse response = httpClient.execute(builder.build());
        return EntityUtils.toString(response.getEntity());
    }

    public String post() throws IOException {
        HttpPost httpRequest = new HttpPost(this.url);
        List<NameValuePair> appParams = new ArrayList<NameValuePair>();

        if(this.headerParams != null){
            for (String key : this.headerParams.keySet()) {
                httpRequest.setHeader(key, this.headerParams.get(key));
            }
        }

        if (this.params != null) {
            for (String key : this.params.keySet()) {
                appParams.add(new BasicNameValuePair(key, this.params.get(key)));
            }
        }

        HttpEntity httpEntity = new UrlEncodedFormEntity(appParams, "UTF-8");
        httpRequest.setEntity(httpEntity);

        HttpResponse response = httpClient.execute(httpRequest);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     *
     * @param uri   请求地址
     * @param params    参数的Map
     * @param headerParams  header参数Map。key为需要设置的header名，value为header值
     * @return
     * @throws IOException
     */
    public String post(String uri, Map<String, String> params, Map<String, String> headerParams) throws IOException {
        HttpPost httpRequest = new HttpPost(uri);
        List<NameValuePair> appParams = new ArrayList<NameValuePair>();

        if(headerParams != null){
            for (String key : headerParams.keySet()) {
                httpRequest.setHeader(key, headerParams.get(key));
            }
        }

        if (params != null) {
            for (String key : params.keySet()) {
                appParams.add(new BasicNameValuePair(key, params.get(key)));
            }
        }

        HttpEntity httpEntity = new UrlEncodedFormEntity(appParams, "UTF-8");
        httpRequest.setEntity(httpEntity);

        HttpResponse response = httpClient.execute(httpRequest);
        return EntityUtils.toString(response.getEntity());
    }


	/**
	 * Get访问方式(解析JSON为对象)
	 * @param url 访问地址
	 * @param params 请求参数
	 * @param clazz 返回数据背解析成为的对象类型
	 * @param argClasses 返回集合类型时的泛型类型
	 * @return
	 * @throws Exception
	 */
	public static <T> T doGet(String url, Map<String, String> params,
			Class<T> clazz, Class<?>... argClasses) throws Exception {
        closeableHttpClient = HttpClients.createDefault();
		ObjectMapper objectMapper = new ObjectMapper();
		String responseBody = "";

        RequestBuilder requestBuilder = RequestBuilder.get().setUri(new URI(url));
        if (params != null) {
            for (Entry<String, String> entry : params.entrySet()) {
                requestBuilder.addParameter(entry.getKey(),
                        entry.getValue());
            }
        }
        HttpUriRequest httpUriRequest = requestBuilder.build();
        httpUriRequest.addHeader("Content-Type", "text/html;charset=UTF-8");
        closeableHttpResponse = closeableHttpClient.execute(httpUriRequest);
		try {
				HttpEntity entity = closeableHttpResponse.getEntity();
				// 判断执行结果返回状态
				int status = closeableHttpResponse.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					responseBody = entity != null ? EntityUtils
							.toString(entity) : null;
				} else {
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
				// 判断返回类型是否为JSON
				ContentType contentType = ContentType.getOrDefault(entity);
				if (!contentType.getMimeType().equals(
						ContentType.APPLICATION_JSON.getMimeType())) {
					throw new ClientProtocolException("Unexpected content type: " + contentType);
				}
				EntityUtils.consume(entity);

		} finally {
            close();
		}

		if (argClasses.length > 0) {
            // 解决反序列化为集合失败问题
            objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			JavaType javaType = objectMapper.getTypeFactory()
					.constructParametricType(clazz, argClasses);
			return objectMapper.readValue(responseBody, javaType);
		} else {
			return objectMapper.readValue(responseBody, clazz);
		}

	}

	/**
	 * Post访问方式(解析JSON为对象)
	 * @param url 访问地址
	 * @param params 请求参数
	 * @param clazz 返回数据背解析成为的对象类型
	 * @param argClasses 返回集合类型时的泛型类型
	 * @return
	 * @throws Exception
	 */
	public static <T> T doPost(String url, Map<String, String> params,
			Class<T> clazz, Class<?>... argClasses) throws Exception {
		closeableHttpClient = HttpClients.createDefault();
		ObjectMapper objectMapper = new ObjectMapper();
		String responseBody = "";
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "text/html;charset=UTF-8");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		closeableHttpResponse = closeableHttpClient.execute(httpPost);

		try {
			HttpEntity entity = closeableHttpResponse.getEntity();
			// 判断执行结果返回状态
			int status = closeableHttpResponse.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				responseBody = entity != null ? EntityUtils.toString(entity)
						: null;
			} else {
				throw new ClientProtocolException(
						"Unexpected response status: " + status);
			}
			// 判断返回类型是否为JSON
			ContentType contentType = ContentType.getOrDefault(entity);
			if (!contentType.getMimeType().equals(
					ContentType.APPLICATION_JSON.getMimeType())) {
				throw new ClientProtocolException("Unexpected content type: "
						+ contentType);
			}
			EntityUtils.consume(entity);
		} finally {
            close();
		}

		if (argClasses.length > 0) {
            // 解决反序列化为集合失败问题
            objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			JavaType javaType = objectMapper.getTypeFactory()
					.constructParametricType(clazz, argClasses);
			return objectMapper.readValue(responseBody, javaType);
		} else {
			return objectMapper.readValue(responseBody, clazz);
		}

	}

    /**
     * 关闭连接与响应对象
     * @throws IOException
     */
    private static void close() throws IOException {
        if(closeableHttpResponse != null){
            closeableHttpResponse.close();
        }
        if(closeableHttpClient != null){
            closeableHttpClient.close();
        }
    }
}
