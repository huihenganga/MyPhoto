package com.myphoto.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myphoto.dao.ProductCountDao;
import com.myphoto.dao.ProductDao;
import com.myphoto.entity.Product;
import com.myphoto.entity.ProductCount;
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
	
	@Override
	public List<Product> getProductByUserId(String userId) {
		/*return productDao.findByHql("select new Video(v.id,v.videoCount) from Video v where v.shareId=? and v.publishTime>?",userId,
				new Date(System.currentTimeMillis() - 10080* 60000));*/
		return productDao.findByHql(" from Product v where v.shareId=? ",userId);
	}

	@Override
	public PageObject getProductPO(PageObject pageObject,Integer userId) {
		StringBuffer hql = new StringBuffer("from ProductVO where 1=1 and shareId="+userId);
		return productDao.findForPageByHql(pageObject, hql.toString());
	}
	
	@Override
	public PageObject getLikeProductPO(PageObject pageObject,Integer userId) {
		StringBuffer hql = new StringBuffer("from Product v,ProductLikeLog vll " +
				"where 1=1 and v.id=vll.ProductId and vll.liker="+userId);
		hql.append(" order by id desc");
		return productDao.findForPageByHql(pageObject, hql.toString());
	}

	@Override
	public void saveOrUpdateProduct(Product t) {
		productDao.saveOrUpdate(t);
	}

	@Override
	public Product getProductByProductId(Integer productId) {
		return productDao.findFirstByHql(
				"from Video where id=?",productId);
	}
	
	@Override
	public Integer getDynamicCountById(Integer circleId) {
		Long count = productDao.findCountBySql(
				"select count(*) from Product where circleId=?",circleId );
		return count.intValue();
	}

	@Override
	public ProductCount getProductCountByProductId(Integer productId) {
		return productCountDao.findFirstByHql(
				"from ProductCount where productId=?",productId);
	}
	
}
