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
	private Integer productId ;			//作品Id
	private Integer parentReplyId;		//父评论的id
	private String parentRelyer;		//父评论人：父评论人的用户信息{User：id，headImage,nickName}
	private String parentReplyConetent;	//父评论内容
	private String content;				//评论的内容
	private String replyer;				//评论人：评论人的用户信息{User：id，headImage,nickName}
	private Long commentTime;			//评论的时间
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getParentReplyId() {
		return parentReplyId;
	}
	public void setParentReplyId(Integer parentReplyId) {
		this.parentReplyId = parentReplyId;
	}
	public String getParentRelyer() {
		return parentRelyer;
	}
	public void setParentRelyer(String parentRelyer) {
		this.parentRelyer = parentRelyer;
	}
	public String getParentReplyConetent() {
		return parentReplyConetent;
	}
	public void setParentReplyConetent(String parentReplyConetent) {
		this.parentReplyConetent = parentReplyConetent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReplyer() {
		return replyer;
	}
	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}
	public Long getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Long commentTime) {
		this.commentTime = commentTime;
	}
}