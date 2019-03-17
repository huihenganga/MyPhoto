package com.myphoto.service;

import java.util.List;

import com.myphoto.entity.Attention;
import com.myphoto.entity.Collection;
import com.myphoto.entity.RUserArtShow;
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
	
	/**
	 * @author wcj
	 * 2019-3-13上午12:23:03
	 * @param beCollecId
	 * @param id
	 * @param type
	 * @return
	 * Attention
	 */
	public Collection getCollectCidAndUidAndType(Integer beCollecId, String userId);
	/**
	 * @author wcj
	 * 2019-3-13上午12:31:10
	 * @param tem
	 * void
	 */
	public void saveOrUpdateCollection(Collection tem);
	/**
	 * @author wcj
	 * 2019-3-13下午5:09:34
	 * @param pageObject
	 * @param userId
	 * @return
	 * PageObject
	 */
	public PageObject getAttentionProductList(PageObject pageObject, String userId);
	/**
	 * @author wcj
	 * 2019-3-13下午5:50:08
	 * @param artShowId
	 * @param userId
	 * @return
	 * RUserArtShow
	 */
	public RUserArtShow getCollectArtShow(Integer artShowId, String userId);
	/**
	 * @author wcj
	 * 2019-3-13下午5:50:28
	 * @param tem
	 * void
	 */
	public void saveOrUpdateRUserArtShow(RUserArtShow tem);
	/**
	 * @author wcj
	 * 2019-3-13下午11:47:30
	 * @param pageObject
	 * @param userId
	 * @return
	 * PageObject
	 */
	public PageObject getRecommendProductList(PageObject pageObject,
			String userId);
	/**
	 * @author wcj
	 * 2019-3-15上午12:44:44
	 * @param pageObject
	 * @param userId
	 * @return
	 * PageObject
	 */
	public PageObject getCollectProductList(PageObject pageObject, String userId);
	/**
	 * @author wcj
	 * 2019-3-15下午10:36:32
	 * @param pageObject
	 * @param userId
	 * @return
	 * PageObject
	 */
	public PageObject getCollectArtShowList(PageObject pageObject, String userId);
	/**
	 * @author wcj
	 * 2019-3-15下午11:43:34
	 * @param pageObject
	 * @param userId
	 * @return
	 * PageObject
	 */
	public PageObject getProductListBuUserId(PageObject pageObject,
			String userId);
	/**
	 * @author wcj
	 * 2019-3-16下午9:20:28
	 * @param pageObject
	 * @return
	 * PageObject
	 */
	public PageObject getHotProductList(PageObject pageObject);
	/**
	 * @author wcj
	 * 2019-3-16下午9:38:24
	 * @param pageObject
	 * @return
	 * PageObject
	 */
	public PageObject getArtShowList(PageObject pageObject);

}
