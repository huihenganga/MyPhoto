package com.myphoto.entity.base;

import javax.servlet.http.HttpServletRequest;


import java.io.Serializable;
import java.util.List;
/**
 * 
 * 
 * Copyright(C) 
 * 
 * Module: 
 * 
 * @author 
 * @version v1.0
 * @description:<分页实体类>
 * @date:May 13, 2014 3:35:50 PM
 * @log:
 */
@SuppressWarnings({"rawtypes"})
public class PageObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7950617263678779519L;
	

	private int pageSize = 15; // 每页的记录数

	private int curPage = 1; // 当前页

	private List data; // 当前页中存放的记录

	private int dataCount; // 总记录数

	private int pageCount;// 总页数


	public PageObject() {}

	private PageObject(int pageSize){
		this.pageSize=pageSize;
	}
	
	public PageObject(int curPage,int pageSize){
		this.curPage=curPage;
		this.pageSize=pageSize;
	}

	//获取当前页数
	public int getCurPage() {
		if (curPage < 1) {
			curPage = 1;
		}
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}

	/**
	 * 获取总页数
	 * @return
	 */
	public int getPageCount() {
		if (dataCount > 0) {
			pageCount = dataCount % pageSize == 0 ? (dataCount / pageSize) : (dataCount / pageSize + 1);
		}
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * 获取每页的记录数
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 该页是否有下一页.
	 */
	public boolean hasNextPage() {
		return this.curPage < this.getPageCount();
	}

	/**
	 * 该页是否有上一页.
	 */
	public boolean hasPrePage() {
		return this.curPage > 1;
	}

	
	/**
	 * 数据查询开始位置
	 * @return
	 */
	public int getBeginPoint() {
		return (getCurPage() - 1) * getPageSize();
	}

	/**
	 * 实例化
	 * @param request
	 * @return
	 */
	public static PageObject getInstance(HttpServletRequest request) {
		PageObject pageObject = new PageObject();
		pageObject.reqProperty(request);
		return pageObject;
	}
	
	/***
	 * 实例化，传每页显示记录数
	 * @param request
	 * @param pageSize
	 * @return
	 */
	public static PageObject getInstance(HttpServletRequest request,int pageSize){
		PageObject pageObject=new PageObject(pageSize);
		pageObject.reqProperty(request);
		return pageObject;
	}
	
	public void reqProperty(HttpServletRequest request) {
		String curPage = null, pageSize = null,dataCount=null;

		curPage = request.getParameter("page");;//请求的页码
		if (curPage != null && curPage != "") {
			try {
				this.curPage = Integer.valueOf(curPage).intValue();
			} catch (NumberFormatException ex) {}
		}

		pageSize = request.getParameter("rows");//每页显示记录数
		if (pageSize != null && pageSize != "") {
			try {
				this.pageSize = Integer.valueOf(pageSize).intValue();
			} catch (NumberFormatException ex) {}
		}
		
		dataCount = request.getParameter("total");//总记录数
		if (dataCount != null && dataCount != "") {
			try {
				this.dataCount = Integer.valueOf(dataCount).intValue();
			} catch (NumberFormatException ex) {}
		}
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
