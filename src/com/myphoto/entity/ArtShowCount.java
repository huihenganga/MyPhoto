package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//画展
@Entity
@Table(name = "ARTSHOW_COUNT")
public class ArtShowCount {
	private Integer id;         
	private Integer artShowId;		//作品ID
	private Integer collectCount=0;	//收藏数量
	
	public ArtShowCount() {
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

	public Integer getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(Integer collectCount) {
		this.collectCount = collectCount;
	}
}