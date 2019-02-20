package com.myphoto.contants;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;
import com.myphoto.entity.base.PageObject;

/**
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("all")
public class JsonResult implements Serializable
{
	private String msg; // 接口调用结果描述信息
	private String ret; // 接口调用结果描述编码
	private PageObject page; // 接口返回的分页对象
	private Map<String, Object> data; // 接口返回的数据
	
	public JsonResult(String ret,String msg)
	{
		this.msg = msg;
		this.ret = ret;
	}
	
	public JsonResult(Describable describable)
	{
		this.msg = describable.getMsg();
		this.ret = describable.getCode();
	}
	
	public JsonResult(Describable describable, PageObject page)
	{
		this.msg = describable.getMsg();
		this.ret = describable.getCode();
		this.page = page;
	}
	
	public JsonResult(Describable describable, Map<String, Object> data)
	{
		this.msg = describable.getMsg();
		this.ret = describable.getCode();
		this.data = data;
	}
	
	public JsonResult(Describable describable, PageObject page, Map<String, Object> data)
	{
		this.msg = describable.getMsg();
		this.ret = describable.getCode();
		this.page = page;
		this.data = data;
	}
	
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public String getRet()
	{
		return ret;
	}
	public void setRet(String ret)
	{
		this.ret = ret;
	}
	public PageObject getPage()
	{
		return page;
	}
	public void setPage(PageObject page)
	{
		this.page = page;
	}
	public Map<String, Object> getData()
	{
		return data;
	}
	public void setData(Map<String, Object> data)
	{
		this.data = data;
	}
	
	public void addData(String valueName, Object value)
	{
		if (null == data)
		{
			data = Maps.newHashMap();
		}
		this.data.put(valueName, value);
	}
}
