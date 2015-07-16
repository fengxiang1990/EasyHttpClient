package com.fxa.zgjm.net;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.fxa.zgjm.util.ToastUtil;

/**
 * 
 * @author fxa 
 *
 */
public class ProxyHandler implements InvocationHandler {

	public static boolean isLogOut = true;

	static String TAG = ProxyHandler.class.getName();

	private Object requestClientImpl;

	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public ProxyHandler(Object requestClientImpl) {
		this.requestClientImpl = requestClientImpl;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object object = null;
		String methodName = method.getName();
		if (methodName.equals("post") || methodName.equals("get")) {
			if (checkNet()) {
				try {
					object = method.invoke(requestClientImpl, args);
					Object url = args[0];
					onExecuted(url,object);
				} catch (RuntimeException e) {
					onException(e);
				}
			}
		}
		return object;
	}

	public boolean checkNet() {
		if (NetCheck.isNetworkConnected(context)) {
			return true;
		} else {
			try {
				Looper.prepare();
				ToastUtil.show(context, "Net connection error");
				Looper.loop();
			} catch (RuntimeException e) {
				e.printStackTrace();
				ToastUtil.show(context, "Net connection error");
			}
		}
		return false;
	}

	public void onException(RuntimeException exception) {
		Log.e(TAG, "onException");
		if (exception instanceof NetException) {
			if (isLogOut) {
				Log.e(TAG, exception.getMessage());
			}
		}
	}



	public void onExecuted(Object url, Object result) {
		
		if (isLogOut) {
			Log.i(TAG, String.valueOf(url));
			Log.i(TAG, String.valueOf(result));
		}
	}

}
