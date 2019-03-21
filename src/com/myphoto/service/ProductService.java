package com.myphoto.service;

import java.util.List;

import com.myphoto.entity.ArtShow;
import com.myphoto.entity.ArtShowCount;
import com.myphoto.entity.ArtShowHotVO;
import com.myphoto.entity.ArtShowRecomentVO;
import com.myphoto.entity.Product;
import com.myphoto.entity.ProductArtShow;
import com.myphoto.entity.ProductCount;
import com.myphoto.entity.ShareBeLookLog;
import com.myphoto.entity.ShareLog;
import com.myphoto.entity.base.PageObject;

public interface ProductService {
	
	public List<Product> getProductByUserId(String userId);
	
	public PageObject getProductPO(PageObject pageObject,Integer userId);
	
	public PageObject getLikeProductPO(PageObject pageObject,Integer userId);

	public void saveOrUpdateProduct(Product t);

	public Product getProductByProductId(Integer productId);

	public Integer getDynamicCountById(Integer circleId);

	public ProductCount getProductCountByProductId(Integer productId);

	public ArtShow getArtShowByArtShowtId(Integer displayId);

	public ProductArtShow getProductArtShowByTwoId(Integer productId,
			Integer displayId);

	public void saveOrUpdateProductArtShow(ProductArtShow productArtShow);

	/**
	 * @author wcj
	 * 2019-3-11下午10:33:20
	 * @param displayId
	 * @param productId
	 * void
	 */
	public void deleteProductArtShow(Integer displayId, Integer productId);

	/**
	 * @author wcj
	 * 2019-3-13下午6:12:44
	 * @param productId
	 * @return
	 * ProductArtShow
	 */
	public ProductArtShow getProductArtShowProductId(Integer productId);

	/**
	 * @author wcj
	 * 2019-3-13下午11:18:28
	 * @param pc
	 * void
	 */
	public void saveOrUpdaProductCount(ProductCount pc);

	/**
	 * @author wcj
	 * 2019-3-14上午1:16:19
	 * @param artShowId
	 * @return
	 * ArtShowCount
	 */
	public ArtShowCount getArtShowCountByArtShowId(Integer artShowId);

	/**
	 * @author wcj
	 * 2019-3-14上午1:21:58
	 * @param pc
	 * void
	 */
	public void saveOrUpdaArtShowCount(ArtShowCount pc);

	/**
	 * @author wcj
	 * 2019-3-17下午6:59:29
	 * @param pageObject
	 * @param str
	 * @return
	 * PageObject
	 */
	public PageObject searchArtShowByName(PageObject pageObject, String str);

	/**
	 * @author wcj
	 * 2019-3-17下午7:46:39
	 * @param shareUserId
	 * @param watchUserId
	 * @param productId
	 * @return
	 * ShareLog
	 */
	public ShareLog getShareLogByShareId(String shareUserId,
			 Integer productId);

	/**
	 * @author wcj
	 * 2019-3-17下午7:52:26
	 * @param t
	 * void
	 */
	public void saveOrUpdateShareLog(ShareLog t);

	/**
	 * @author wcj
	 * 2019-3-17下午7:59:22
	 * @param shareUserId
	 * @param watchUserId
	 * @param productId
	 * @return
	 * ShareBeLookLog
	 */
	public ShareBeLookLog getShareBeLookLog(String shareUserId,
			String watchUserId, Integer productId);

	/**
	 * @author wcj
	 * 2019-3-17下午7:59:28
	 * @param t
	 * void
	 */
	public void saveOrUpdateShareBeLookLog(ShareBeLookLog t);

	/**
	 * @author wcj
	 * 2019-3-21下午11:00:00
	 * @param artShowId
	 * @return
	 * List<ArtShowRecomentVO>
	 */
	public List<ArtShowRecomentVO> getRecomentArtShow();

	/**
	 * @author wcj
	 * 2019-3-21下午11:09:01
	 * @return
	 * List<ArtShowHotVO>
	 */
	public List<ArtShowHotVO> getHotArtShow();
}
