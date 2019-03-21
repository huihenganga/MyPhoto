package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//内容点赞日志
@Entity
@Table(name = "contentlikelog")
public class ContentLikeLog {
	private Integer id;         
	private Integer rootId;		//作品ID
	private String liker; 		//点赞人
	private Date liketime;		//点赞时间
	private Integer status;		//点赞状态：0取消关注；1关注
	private String beLiker; 	//被点赞人
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRootId() {
		return rootId;
	}
	public void setRootId(Integer rootId) {
		this.rootId = rootId;
	}
	public String getLiker() {
		return liker;
	}
	public void setLiker(String liker) {
		this.liker = liker;
	}
	public Date getLiketime() {
		return liketime;
	}
	public void setLiketime(Date liketime) {
		this.liketime = liketime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBeLiker() {
		return beLiker;
	}
	public void setBeLiker(String beLiker) {
		this.beLiker = beLiker;
	}
}