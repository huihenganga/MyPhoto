package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//用户分享作品被查看记录
@Entity
@Table(name = "SHAREBELOOKLOG")
public class ShareBeLookLog {
	private Integer id;
	private String shareUserId;		//分享人
	private String lookUserId;		//被分享人
	private Integer prodcutId;		//被分享作品Id
	private Date lookTime;			//分享时间

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getShareUserId() {
		return shareUserId;
	}
	public void setShareUserId(String shareUserId) {
		this.shareUserId = shareUserId;
	}
	public Integer getProdcutId() {
		return prodcutId;
	}
	public void setProdcutId(Integer prodcutId) {
		this.prodcutId = prodcutId;
	}
	public String getLookUserId() {
		return lookUserId;
	}
	public void setLookUserId(String lookUserId) {
		this.lookUserId = lookUserId;
	}
	public Date getLookTime() {
		return lookTime;
	}
	public void setLookTime(Date lookTime) {
		this.lookTime = lookTime;
	}
}