# EasyHttpClient
一个简单实用的android http客户端,用动态代理的方式实现了 对http 访问的拦截 包括每次访问前检查网络，请求成功后打印日志等 
可自行修改，希望大家一起完善
客户端实用非常简单 
get 方法 ： RequestClientProxy.getInstance(getApplicationContext()).get(url);
post 方法：  RequestClientProxy.getInstance(getApplicationContext()).post(url,List<NameValuePair>);

 目前只有五个方法：
 
  public  String post(String url,InputStreamEntity reqEntity) throws NetException;

	public  String post(String url,List<NameValuePair> paramList) throws NetException;
	
	public  String post(String url,List<NameValuePair> paramList,String charset) throws NetException;
	
	public  String get(String url);
	
	public  String get(String url,Map<String,Object> httpParams);
