package com.myphoto.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//内容点赞日志
@Entity
@Table(name = "contentlikelog")
public class ContentLikeLog {
	private Integer contentId;         
	private Integer rootId;		//作品ID
	private Integer liker; 		//点赞人
	private Long liketime;		//点赞时间
	private Integer status;		//点赞状态：0取消关注；1关注
	private Integer beLiker; 	//被点赞人
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getContentId() {
		return contentId;
	}
	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}
	public Integer getRootId() {
		return rootId;
	}
	public void setRootId(Integer rootId) {
		this.rootId = rootId;
	}
	public Integer getLiker() {
		return liker;
	}
	public void setLiker(Integer liker) {
		this.liker = liker;
	}
	public Long getLiketime() {
		return liketime;
	}
	public void setLiketime(Long liketime) {
		this.liketime = liketime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBeLiker() {
		return beLiker;
	}
	public void setBeLiker(Integer beLiker) {
		this.beLiker = beLiker;
	}
}