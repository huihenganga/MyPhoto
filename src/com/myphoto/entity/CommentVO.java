package com.myphoto.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//评论表
@Entity
@Table(name = "COMMENTVO")
public class CommentVO {
	private Integer id;         		//1
	private Integer productId ;			//内容Id6
	private Integer parentReplyId;		//父评论的id4
	private String parentReplyConetent;	//父评论内容
	private String parentRelyer;		//父评论人：父评论人的用户信息{User：id，headImage,nickName}5
	private String content;				//评论的内容3
	private String replyer;			//评论人：评论人的用户信息{User：id，headImage,nickName}7
	private Long commentTime;			//评论的时间2
	private String replyerHeadImage;	//8
	private String replyerNickName;		//9
	private String parentRelyerHeadImage;//10
	private String parentRelyerNickName;//11
	private Integer likeNum;			//评论点赞数量12
	private Integer likeState;			//评论点赞状态13
	
	
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
	public Long getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Long commentTime) {
		this.commentTime = commentTime;
	}
	public String getReplyerHeadImage() {
		return replyerHeadImage;
	}
	public void setReplyerHeadImage(String replyerHeadImage) {
		this.replyerHeadImage = replyerHeadImage;
	}
	public String getReplyerNickName() {
		return replyerNickName;
	}
	public void setReplyerNickName(String replyerNickName) {
		this.replyerNickName = replyerNickName;
	}
	public String getParentRelyerHeadImage() {
		return parentRelyerHeadImage;
	}
	public void setParentRelyerHeadImage(String parentRelyerHeadImage) {
		this.parentRelyerHeadImage = parentRelyerHeadImage;
	}
	public String getParentRelyerNickName() {
		return parentRelyerNickName;
	}
	public void setParentRelyerNickName(String parentRelyerNickName) {
		this.parentRelyerNickName = parentRelyerNickName;
	}
	public Integer getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}
	public Integer getLikeState() {
		return likeState;
	}
	public void setLikeState(Integer likeState) {
		this.likeState = likeState;
	}
	public String getParentRelyer() {
		return parentRelyer;
	}
	public void setParentRelyer(String parentRelyer) {
		this.parentRelyer = parentRelyer;
	}
	public String getReplyer() {
		return replyer;
	}
	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}
}