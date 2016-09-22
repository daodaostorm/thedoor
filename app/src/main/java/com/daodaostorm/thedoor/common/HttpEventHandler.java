
package com.daodaostorm.thedoor.common;

public abstract class HttpEventHandler <T> {
	
	public abstract void HttpSucessHandler(T result);
	
	public abstract void HttpFailHandler();
	
}
