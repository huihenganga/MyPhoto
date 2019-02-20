package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//作品
@Entity
@Table(name = "PRODUCT")
public class Product {
	private Integer id;         
	private String title;			//视频标题
	private String coveUrl;			//视频封面地址
	private String shareId; 		//分享人
	private Date publishTime;		//发布时间
	private String description; 	//具体描述
	
	public Product() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCoveUrl() {
		return coveUrl;
	}
	public void setCoveUrl(String coveUrl) {
		this.coveUrl = coveUrl;
	}
	public String getShareId() {
		return shareId;
	}
	public void setShareId(String shareId) {
		this.shareId = shareId;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}