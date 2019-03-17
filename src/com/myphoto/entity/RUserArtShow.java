package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//用户-画展关系表（收藏表）
@Entity
@Table(name = "RUSERARTSHOW")
public class RUserArtShow {
	private Integer id;         
	private Integer artShowId;		//画展id
	private String userId;			//用户id
	private Date collectTime;		//收藏时间
	private Integer status;			//收藏状态：0取消收藏；1收藏
	
	public RUserArtShow() {
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

	public Integer getArtShowId() {
		return artShowId;
	}

	public void setArtShowId(Integer artShowId) {
		this.artShowId = artShowId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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