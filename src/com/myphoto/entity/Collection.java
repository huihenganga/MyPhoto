package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//用户-画作关系表（收藏表）
@Entity
@Table(name = "COLLECTION")
public class Collection {
	private Integer id;
	private String collectBy;		//收藏人
	private Integer collectionId;	//被收藏的id
	private Date collectTime;		//收藏时间
	private Integer status;			//收藏状态：0取消收藏；1收藏

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCollectBy() {
		return collectBy;
	}
	public void setCollectBy(String collectBy) {
		this.collectBy = collectBy;
	}
	public Integer getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}