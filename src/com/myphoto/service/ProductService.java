package com.myphoto.service;

import java.util.List;

import com.myphoto.entity.Product;
import com.myphoto.entity.ProductCount;
import com.myphoto.entity.base.PageObject;

public interface ProductService {
	
	public List<Product> getProductByUserId(String userId);
	
	public PageObject getProductPO(PageObject pageObject,Integer userId);
	
	public PageObject getLikeProductPO(PageObject pageObject,Integer userId);

	public void saveOrUpdateProduct(Product t);

	public Product getProductByProductId(Integer productId);

	public Integer getDynamicCountById(Integer circleId);

	public ProductCount getProductCountByProductId(Integer productId);
}
