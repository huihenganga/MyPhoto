package com.myphoto.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myphoto.dao.ArtShowCountDao;
import com.myphoto.dao.ArtShowDao;
import com.myphoto.dao.ArtShowHotVODao;
import com.myphoto.dao.ArtShowRecomentVODao;
import com.myphoto.dao.ProductArtShowDao;
import com.myphoto.dao.ProductCountDao;
import com.myphoto.dao.ProductDao;
import com.myphoto.dao.ShareBeLookLogDao;
import com.myphoto.dao.ShareLogDao;
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
import com.myphoto.service.ProductService;

@Service
@Transactional
@SuppressWarnings("all")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductCountDao productCountDao;
	@Autowired
	private ArtShowDao artShowDao;
	@Autowired
	private ProductArtShowDao productArtShowDao;
	@Autowired
	private ArtShowCountDao artShowCountDao;
	@Autowired
	private ShareLogDao shareLogDao;
	@Autowired
	private ShareBeLookLogDao shareBeLookLogDao;
	@Autowired
	private ArtShowRecomentVODao artShowRecomentVODao;
	@Autowired
	private ArtShowHotVODao artShowHotVODao;

	@Override
	public List<Product> getProductByUserId(String userId) {
		/*
		 * return productDao.findByHql(
		 * "select new Video(v.id,v.videoCount) from Video v where v.shareId=? and v.publishTime>?"
		 * ,userId, new Date(System.currentTimeMillis() - 10080* 60000));
		 */
		return productDao.findByHql(" from Product v where v.shareId=? ",
				userId);
	}

	@Override
	public PageObject getProductPO(PageObject pageObject, Integer userId) {
		StringBuffer hql = new StringBuffer(
				"from ProductVO where 1=1 and shareId=" + userId);
		return productDao.findForPageByHql(pageObject, hql.toString());
	}

	@Override
	public PageObject getLikeProductPO(PageObject pageObject, Integer userId) {
		StringBuffer hql = new StringBuffer(
				"from Product v,ProductLikeLog vll "
						+ "where 1=1 and v.id=vll.ProductId and vll.liker="
						+ userId);
		hql.append(" order by id desc");
		return productDao.findForPageByHql(pageObject, hql.toString());
	}

	@Override
	public void saveOrUpdateProduct(Product t) {
		productDao.saveOrUpdate(t);
	}

	@Override
	public Product getProductByProductId(Integer productId) {
		return productDao.findFirstByHql("from Product where id=?", productId);
	}

	@Override
	public Integer getDynamicCountById(Integer circleId) {
		Long count = productDao.findCountBySql(
				"select count(*) from Product where circleId=?", circleId);
		return count.intValue();
	}

	@Override
	public ProductCount getProductCountByProductId(Integer productId) {
		return productCountDao.findFirstByHql(
				"from ProductCount where productId=?", productId);
	}

	@Override
	public ArtShow getArtShowByArtShowtId(Integer displayId) {
		return artShowDao.findFirstByHql("from ArtShow where id=?", displayId);
	}

	@Override
	public ProductArtShow getProductArtShowByTwoId(Integer productId,
			Integer displayId) {
		return productArtShowDao.findFirstByHql(
				"from ProductArtShow where productId=? and displayId=?",
				productId, displayId);
	}

	@Override
	public void saveOrUpdateProductArtShow(ProductArtShow productArtShow) {
		productArtShowDao.saveOrUpdate(productArtShow);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.ProductService#deleteProductArtShow(java.lang.Integer
	 * , java.lang.Integer)
	 */
	@Override
	public void deleteProductArtShow(Integer displayId, Integer productId) {
		productArtShowDao
				.updateBySql(
						"delete from ProductArtShow where displayId=? and productId=? ",
						displayId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.ProductService#getProductArtShowProductId(java.lang
	 * .Integer)
	 */
	@Override
	public ProductArtShow getProductArtShowProductId(Integer productId) {
		return productArtShowDao.findFirstByHql(
				"from ProductArtShow where productId=?", productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.ProductService#saveOrUpdaProductCount(com.myphoto
	 * .entity.ProductCount)
	 */
	@Override
	public void saveOrUpdaProductCount(ProductCount pc) {
		productCountDao.saveOrUpdate(pc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.ProductService#getArtShowCountByArtShowId(java.lang
	 * .Integer)
	 */
	@Override
	public ArtShowCount getArtShowCountByArtShowId(Integer artShowId) {
		return artShowCountDao.findFirstByHql(
				"from ArtShowCount where artShowId=?", artShowId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.ProductService#saveOrUpdaArtShowCount(com.myphoto
	 * .entity.ArtShowCount)
	 */
	@Override
	public void saveOrUpdaArtShowCount(ArtShowCount pc) {
		artShowCountDao.saveOrUpdate(pc);
	}

	@Override
	public PageObject searchArtShowByName(PageObject pageObject, String str) {
		StringBuffer hql = new StringBuffer("from ArtShow where title like '%"
				+ str + "%'");
		PageObject pg = artShowDao.findForPageByHql(pageObject, hql.toString());
		return pg;
	}

	@Override
	public ShareLog getShareLogByShareId(String shareUserId,
			Integer productId) {
		ShareLog shareLog = shareLogDao
				.findFirstByHql(
						"from ShareLog where shareUserId=? and prodcutId=?",
						shareUserId, productId);
		return shareLog;
	}

	@Override
	public void saveOrUpdateShareLog(ShareLog t) {
		shareLogDao.saveOrUpdate(t);
	}

	@Override
	public ShareBeLookLog getShareBeLookLog(String shareUserId,
			String watchUserId, Integer productId) {
		ShareBeLookLog shareBeLookLog = shareBeLookLogDao
				.findFirstByHql(
						"from ShareBeLookLog where shareUserId=? and lookUserId=? and prodcutId=?",
						shareUserId, watchUserId, productId);
		return shareBeLookLog;
	}

	@Override
	public void saveOrUpdateShareBeLookLog(ShareBeLookLog t) {
		shareBeLookLogDao.saveOrUpdate(t);
	}

	@Override
	public List<ArtShowRecomentVO> getRecomentArtShow() {
		List<ArtShowRecomentVO> list = artShowRecomentVODao.findByHql(" from ArtShowRecomentVO order by createTime desc ");
		if (list.size() > 8) {
			list = list.subList(0,8);
		}
		return list;
	}

	@Override
	public List<ArtShowHotVO> getHotArtShow() {
		List<ArtShowHotVO> list = artShowHotVODao.findByHql(" from ArtShowHotVO order by createTime desc ");
		if (list.size() > 4) {
			list = list.subList(0,4);
		}
		return list;
	}

}
