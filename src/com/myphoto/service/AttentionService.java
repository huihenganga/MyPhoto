package com.myphoto.service;

import java.util.List;

import com.myphoto.entity.Attention;
import com.myphoto.entity.base.PageObject;


public interface AttentionService {
	//关注数量
	public Integer getAttentionCountByUserId(String userId);
	//粉丝数量
	public Integer getFansCountByUserId(String userId);
	
	public void saveOrUpdateAttention(Attention t);

	public Attention getAttentionByTwoUserId(String beAttentionId, String userId);

	public PageObject getAttentionByUserId(PageObject pageObject, String userId);
	
	public PageObject getFansByUserId(PageObject pageObject, String userId);

}
