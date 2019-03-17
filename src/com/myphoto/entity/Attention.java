package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//用户关注关联表
@Entity
@Table(name = "ATTENTION")
public class Attention {
	private Integer id;
	private String attentionBy;		//关注人
	private String attentionTo;		//被关注人
	private Date attentionTime;		//关注时间
	private Integer status;			//关注状态：0取消关注；1关注

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAttentionBy() {
		return attentionBy;
	}
	public void setAttentionBy(String attentionBy) {
		this.attentionBy = attentionBy;
	}
	public String getAttentionTo() {
		return attentionTo;
	}
	public void setAttentionTo(String attentionTo) {
		this.attentionTo = attentionTo;
	}
	public Date getAttentionTime() {
		return attentionTime;
	}
	public void setAttentionTime(Date attentionTime) {
		this.attentionTime = attentionTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}