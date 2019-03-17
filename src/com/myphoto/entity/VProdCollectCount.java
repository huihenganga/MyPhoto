package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//一周内点赞作品
@Entity
@Table(name = "VPRODCOLLECTCOUNT")
public class VProdCollectCount {
	private Integer collectionId;         
	private String title;			//标题
	private String coveUrl;			//封面地址
	private String shareId; 		//分享人
	private Date publishTime;		//发布时间
	private String description; 	//具体描述
	private String message; 		//留言
	private String background; 		//背景墙颜色
	private Integer count;			//一周内的点赞数量
	
	public VProdCollectCount() {
		super();
	}
	
	@Id
	public Integer getCollectionId() {
		return collectionId;
	}
	
	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
}