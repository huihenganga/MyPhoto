package com.myphoto.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//评论表
@Entity
@Table(name = "COMMENT")
public class Comment {
	private Integer id;         
	private Integer rootId ;			//作品Id
	private Integer parentReplyId;		//父评论的id
	private Integer parentRelyer;		//父评论人：父评论人的用户信息{User：id，headImage,nickName}
	private String content;				//评论的内容
	private Integer replyer;			//评论人：评论人的用户信息{User：id，headImage,nickName}
	private Long commentTime;			//评论的时间
	
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
	public Integer getParentReplyId() {
		return parentReplyId;
	}
	public void setParentReplyId(Integer parentReplyId) {
		this.parentReplyId = parentReplyId;
	}
	public Integer getParentRelyer() {
		return parentRelyer;
	}
	public void setParentRelyer(Integer parentRelyer) {
		this.parentRelyer = parentRelyer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getReplyer() {
		return replyer;
	}
	public void setReplyer(Integer replyer) {
		this.replyer = replyer;
	}
	public Long getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Long commentTime) {
		this.commentTime = commentTime;
	}
}