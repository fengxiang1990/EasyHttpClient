package com.fxa.zgjm.net;

/**
 * 
 * @author fxa 
 *
 */
public class NetException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String TAG = NetException.class.getName();

	public NetException(String msg) {
		super(msg);
	}

}
