package com.fxa.zgjm.net;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.entity.InputStreamEntity;

/**
 * 
 * @author fxa 
 *
 */
public  interface RequestClient {
		
	public  String post(String url,InputStreamEntity reqEntity) throws NetException;

	public  String post(String url,List<NameValuePair> paramList) throws NetException;
	
	public  String post(String url,List<NameValuePair> paramList,String charset) throws NetException;
	
	public  String get(String url);
	
	public  String get(String url,Map<String,Object> httpParams);
		
    
}
