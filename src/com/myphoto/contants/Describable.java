package com.myphoto.contants;

/**
 * <p>名称：Describable.java</p>
 * <p>描述：可以被描述的抽象接口</p>
 */
public interface Describable
{ 
	/**
	 * 描述：  
	 * <pre>描述编码</pre>
	 * @return
	 */
	String getCode();
	
	/**
	 * 描述：
	 * <pre>描述信息</pre>
	 * @return
	 */
	String getMsg();
}
