package com.fxa.zgjm.net;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.entity.InputStreamEntity;

import android.content.Context;

/**
 * 
 * @author fxa 
 *
 */
public class RequestClientProxy implements RequestClient {

	protected static String TAG = RequestClientProxy.class.getName();

	protected Context context;

	protected RequestClient client;

	RequestClientImpl requestClientImpl = null;

	private static RequestClientProxy instance = null;

	private RequestClientProxy(Context context) {
		requestClientImpl = RequestClientImpl.getInstance();
		ProxyHandler handler = new ProxyHandler(requestClientImpl);
		handler.setContext(context);
		client = (RequestClient) Proxy.newProxyInstance(requestClientImpl
				.getClass().getClassLoader(), requestClientImpl.getClass()
				.getInterfaces(), handler);
	}

	static Object clock = new Object();

	public static RequestClientProxy getInstance(Context context) {
		synchronized (clock) {
			if (instance == null) {
				instance = new RequestClientProxy(context);
			}
			return instance;
		}

	}

	@Override
	public String post(String url, InputStreamEntity reqEntity)
			throws NetException {
		return client.post(url, reqEntity);
	}

	@Override
	public String post(String url, List<NameValuePair> paramList)
			throws NetException {
		return client.post(url, paramList);
	}

	@Override
	public String post(String url, List<NameValuePair> paramList, String charset)
			throws NetException {
		return client.post(url, paramList, charset);
	}

	@Override
	public String get(String url) {
		return client.get(url);
	}

	@Override
	public String get(String url, Map<String, Object> httpParams) {
		return client.get(url, httpParams);
	}

}
