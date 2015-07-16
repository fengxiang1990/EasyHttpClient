package com.fxa.zgjm.net;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author fxa 
 *
 */
public class RequestClientImpl implements RequestClient {

	DefaultHttpClient httpClient;

	public static int CONNECTION_TIME_OUT = 1000 * 10;
    public static int SOCKET_TIME_OUT = 1000 * 10;
	public static int  MAX_TOTAL_CONNECTION = 10;
	public static int SOCKET_BUFFER_SIZE = 1024*8;
	public static int KEEP_ALIVE = 30 *1000;
	public static int RETRY_COUNT = 3;
	
	private static RequestClientImpl instance = new RequestClientImpl();

	private RequestClientImpl() {
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setTimeout(params, CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIME_OUT);
		ConnManagerParams.setMaxConnectionsPerRoute(params,
				new ConnPerRouteBean(10));
		ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTION);
		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
				params, schemeRegistry), params);
		httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

			@Override
			public long getKeepAliveDuration(HttpResponse arg0, HttpContext arg1) {
				return KEEP_ALIVE;
			}

		});
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, true));
	}

	public static RequestClientImpl getInstance() {
		synchronized (instance) {
			return instance;
		}

	}

	public String post(String url, List<NameValuePair> paramList,String charset) {
		synchronized (httpClient) {
			try {
				HttpPost post = new HttpPost(url);
				post.setEntity(new UrlEncodedFormEntity(paramList, charset));
				HttpResponse httpResponse = httpClient.execute(post);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					return result;
				}
			} catch (Exception e) {
				throw new NetException("Net Connection Exception");
			}
		}
		return null;
	}

	@Override
	public String get(String url) {
		synchronized (httpClient) {
			try {
				HttpGet get = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(get);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					return result;
				}
			} catch (Exception e) {
				throw new NetException("Net Connection Exception");
			}
		}
		return null;
	}

	@Override
	public String get(String url, Map<String, Object> httpParams) {
		synchronized (httpClient) {
			try {
				StringBuilder sb = new StringBuilder(url);
				sb.append("?");
				Set<String> keySet = httpParams.keySet();
				Iterator<String> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					sb.append(key)
							.append("=")
							.append(URLEncoder.encode(
									String.valueOf(httpParams.get(key)),
									"UTF-8")).append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
				HttpGet get = new HttpGet(sb.toString());
				HttpResponse httpResponse = httpClient.execute(get);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					return result;
				}

			} catch (Exception e) {
				throw new NetException("Net Connection Exception");
			}
		}
		return null;
	}

	/*@Override
	public DefaultHttpClient getHttpClient() {
		return httpClient;
	}*/

	@Override
	public String post(String url, InputStreamEntity reqEntity)
			throws NetException {
		synchronized (httpClient) {
			try {
				HttpPost post = new HttpPost(url);
				post.setEntity(reqEntity);
				HttpResponse httpResponse = httpClient.execute(post);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					return result;
				}
			} catch (Exception e) {
				throw new NetException("Net Connection Exception");
			}
		}
		return null;
	}

	@Override
	public String post(String url, List<NameValuePair> paramList)
			throws NetException {
		synchronized (httpClient) {
			try {
				HttpPost post = new HttpPost(url);
				post.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
				HttpResponse httpResponse = httpClient.execute(post);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					return result;
				}
			} catch (Exception e) {
				throw new NetException("Net Connection Exception");
			}
		}
		return null;
	}

}
