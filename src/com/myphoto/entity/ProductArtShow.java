package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//作品关联画展
@Entity
@Table(name = "PRODUCTARTSHOW")
public class ProductArtShow {
	private Integer id;         
	private Integer productId;		//作品id
	private Integer displayId;		//画展id
	private Date rTime;				//关联时间
	
	public ProductArtShow() {
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
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getDisplayId() {
		return displayId;
	}
	public void setDisplayId(Integer displayId) {
		this.displayId = displayId;
	}

	public Date getrTime() {
		return rTime;
	}

	public void setrTime(Date rTime) {
		this.rTime = rTime;
	}
}