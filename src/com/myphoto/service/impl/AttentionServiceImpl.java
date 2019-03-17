package com.myphoto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myphoto.dao.ArtShowDao;
import com.myphoto.dao.AttentionDao;
import com.myphoto.dao.CollectionDao;
import com.myphoto.dao.ProductArtShowDao;
import com.myphoto.dao.ProductCountDao;
import com.myphoto.dao.ProductDao;
import com.myphoto.dao.RUserArtShowDao;
import com.myphoto.dao.UserDao;
import com.myphoto.dao.VProdCollectCountDao;
import com.myphoto.entity.ArtShow;
import com.myphoto.entity.Attention;
import com.myphoto.entity.Collection;
import com.myphoto.entity.Product;
import com.myphoto.entity.ProductArtShow;
import com.myphoto.entity.ProductCount;
import com.myphoto.entity.RUserArtShow;
import com.myphoto.entity.User;
import com.myphoto.entity.VProdCollectCount;
import com.myphoto.entity.base.PageObject;
import com.myphoto.service.AttentionService;
import com.myphoto.util.StringUtil;

@Service
@Transactional
@SuppressWarnings("all")
public class AttentionServiceImpl implements AttentionService {

	@Autowired
	private AttentionDao attentionDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private CollectionDao collectionDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private RUserArtShowDao rUserArtShowDao;
	@Autowired
	private ProductCountDao productCountDao;
	@Autowired
	private ArtShowDao artShowDao;
	@Autowired
	private ProductArtShowDao productArtShowDao;
	@Autowired
	private VProdCollectCountDao vProdCollectCountDao;

	@Override
	public Integer getAttentionCountByUserId(String userId) {
		Long count = attentionDao
				.findCountBySql(
						"select count(*) from Attention where attentionBy=? and status=1",
						userId);
		return count.intValue();
	}

	@Override
	public Integer getFansCountByUserId(String userId) {
		Long count = attentionDao
				.findCountBySql(
						"select count(*) from Attention where attentionTo=? and status=1",
						userId);
		return count.intValue();
	}

	@Override
	public void saveOrUpdateAttention(Attention t) {
		attentionDao.saveOrUpdate(t);
	}

	@Override
	public Attention getAttentionByTwoUserId(String beAttentionId,
			String attentionBy) {
		return attentionDao.findFirstByHql(
				"from Attention where attentionBy=? and attentionTo=?",
				attentionBy, beAttentionId);
	}

	@Override
	public PageObject getAttentionByUserId(PageObject pageObject, String userId) {
		String str = "";
		List<Attention> list = new ArrayList<Attention>();
		list = attentionDao.findByHql(
				" from Attention where 1=1 and status=1 and attentionBy=? "
						+ "order by attentionTime desc ", userId);

		str = "";
		if (null != list && list.size() > 0) {
			str += "(";
			for (int i = 0; i < list.size() - 1; i++) {
				str = str + "'" + list.get(i).getAttentionTo() + "',";
			}
			str = str + "'" + list.get(list.size() - 1).getAttentionTo();
			str += "')";
		}
		if (!"".equals(str)) {
			String orderStr = str.replace("(", "field(id,");
			StringBuffer hql = new StringBuffer(
					"from User t where 1=1 and id in " + str + " order by "
							+ orderStr);
			pageObject = userDao.findForPageByHql(pageObject, hql.toString());
			List<User> uList = pageObject.getData();
			List l = new ArrayList();
			for (int i = 0; i < uList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", uList.get(i).getId());
				map.put("nickName", uList.get(i).getNickName());
				map.put("headImage", uList.get(i).getHeadImage());
				Attention attention1 = attentionDao
						.findFirstByHql(
								"from Attention where status=1 and attentionBy=? and attentionTo=?",
								userId, uList.get(i).getId());
				map.put("attentionTime", attention1.getAttentionTime()
						.getTime());
				l.add(map);
			}
			pageObject.setData(l);
			return pageObject;
		} else {
			pageObject = new PageObject();
			return pageObject;
		}
	}

	@Override
	public PageObject getFansByUserId(PageObject pageObject, String userId) {
		List<Attention> list = attentionDao.findByHql(
				" from Attention where 1=1 and status=1 and attentionTo=? "
						+ "order by attentionTime desc ", userId);
		String str = "";
		if (list != null && list.size() > 0) {
			str += "(";
			for (int i = 0; i < list.size() - 1; i++) {
				str = str + "'" + list.get(i).getAttentionBy() + "',";
			}
			str = str + "'" + list.get(list.size() - 1).getAttentionBy();
			str += "')";
		}
		if (!"".equals(str)) {
			String orderStr = str.replace("(", "field(id,");
			StringBuffer hql = new StringBuffer(
					"from User t where 1=1 and id in " + str + " order by "
							+ orderStr);
			pageObject = userDao.findForPageByHql(pageObject, hql.toString());
			List<User> uList = pageObject.getData();
			List l = new ArrayList();
			for (int i = 0; i < uList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", uList.get(i).getId());
				map.put("nickName", uList.get(i).getNickName());
				map.put("headImage", uList.get(i).getHeadImage());
				Attention attention = attentionDao
						.findFirstByHql(
								"from Attention where status=1 and attentionBy=? and attentionTo=?",
								userId, uList.get(i).getId());
				if (attention != null && attention.getStatus() == 1) {
					map.put("attentionStatus", 1);
				} else {
					map.put("attentionStatus", 0);
				}
				Attention attention1 = attentionDao
						.findFirstByHql(
								"from Attention where status=1 and attentionBy=? and attentionTo=?",
								uList.get(i).getId(), userId);
				map.put("attentionTime", attention1.getAttentionTime()
						.getTime());
				l.add(map);
				// pageObject = new PageObject();
			}
			pageObject.setData(l);
		} else {
		}
		return pageObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.AttentionService#getCollectCidAndUidAndType(java.
	 * lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public Collection getCollectCidAndUidAndType(Integer beCollecId,
			String userId) {
		return collectionDao.findFirstByHql(
				"from Collection where collectionId=? and collectBy=?",
				beCollecId, userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.AttentionService#saveOrUpdateCollection(com.myphoto
	 * .entity.Collection)
	 */
	@Override
	public void saveOrUpdateCollection(Collection tem) {
		collectionDao.saveOrUpdate(tem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.AttentionService#getAttentionList(com.myphoto.entity
	 * .base.PageObject, java.lang.Integer)
	 */
	@Override
	public PageObject getAttentionProductList(PageObject pageObject,
			String userId) {
		List<Attention> list = attentionDao.findByHql(
				" from Attention v where 1=1 and v.status=1 and"
						+ " v.attentionBy=?", userId);
		String str = "";
		if (list.size() > 0) {
			str += "(";
			for (int i = 0; i < list.size() - 1; i++) {
				// if(list.get(i).getAttentionTo()!=null&&list.get(i).getAttentionTo()>0){
				// str=str+list.get(i).getAttentionTo()+",";
				// }
				if (!StringUtil.isBlank(list.get(i).getAttentionTo())) {
					str = str + "'" + list.get(i).getAttentionTo() + "',";
				}
			}
			if (!StringUtil.isBlank(list.get(list.size() - 1).getAttentionTo())) {
				str = str + "'" + list.get(list.size() - 1).getAttentionTo()
						+ "'";
			}
			str = str.endsWith(",") ? str.substring(0, str.length() - 1) : str;
			str += ")";
		}
		if (!str.equals("()") && !str.equals("")) {
			StringBuffer hql = new StringBuffer(
					"from Product where 1=1 and shareId in" + str);
			hql.append(" order by publishTime desc");

			PageObject pg = productDao.findForPageByHql(pageObject,
					hql.toString());
			List<Product> dateCount = productDao.findByHql(hql.toString());
			List<Product> list3 = pageObject.getData();
			List list2 = new ArrayList();
			if (list3.size() > 0) {
				for (int i = 0; i < list3.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					User u = userDao.findFirstByHql("from User where id=?",
							list3.get(i).getShareId());
					Attention attention = attentionDao.findFirstByHql(
							" from Attention "
									+ "where attentionBy=? and attentionTo=?",
							userId, list3.get(i).getShareId());
					Collection collection = collectionDao.findFirstByHql(
							" from Collection "
									+ "where collectBy=? and collectionId=?",
							userId, list3.get(i).getId());
					ProductCount pc = productCountDao.findFirstByHql(
							" from ProductCount " + "where productId=?", list3
									.get(i).getId());
					map.put("id", list3.get(i).getId());
					map.put("title", list3.get(i).getTitle());
					map.put("coveUrl", list3.get(i).getCoveUrl());
					map.put("publishTime", list3.get(i).getPublishTime()
							.getTime());
					map.put("description", list3.get(i).getDescription());
					map.put("message", list3.get(i).getMessage());
					map.put("background", list3.get(i).getBackground());
					if (null != pc) {
						map.put("beCollect", pc.getCollectCount());
					} else {
						map.put("beCollect", 0);
					}
					if (null != u) {
						map.put("nickName", u.getNickName());
						map.put("avater", u.getHeadImage());
					}
					if (null != attention && attention.getStatus() == 1) {
						map.put("attentionStatus", 1);
					} else {
						map.put("attentionStatus", 0);
					}
					if (null != collection && collection.getStatus() == 1) {
						map.put("collectionStatus", 1);
					} else {
						map.put("collectionStatus", 0);
					}
					list2.add(map);
				}
			}
			pageObject.setData(list2);
			pageObject.setDataCount(dateCount.size());
			return pageObject;
		} else {
			pageObject = new PageObject();
			List l = new ArrayList();
			pageObject.setData(l);
			pageObject.setDataCount(l.size());
			return pageObject;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.AttentionService#getCollectArtShow(java.lang.Integer,
	 * java.lang.String)
	 */
	@Override
	public RUserArtShow getCollectArtShow(Integer artShowId, String userId) {
		return rUserArtShowDao.findFirstByHql(
				"from RUserArtShow where artShowId=? and userId=?", artShowId,
				userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.AttentionService#saveOrUpdateRUserArtShow(com.myphoto
	 * .entity.RUserArtShow)
	 */
	@Override
	public void saveOrUpdateRUserArtShow(RUserArtShow tem) {
		rUserArtShowDao.saveOrUpdate(tem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.AttentionService#getRecommendProductList(com.myphoto
	 * .entity.base.PageObject, java.lang.Integer)
	 */
	@Override
	public PageObject getRecommendProductList(PageObject pageObject,
			String userId) {
		StringBuffer hql = new StringBuffer(
				"from Product order by publishTime desc");
		PageObject pg = productDao.findForPageByHql(pageObject, hql.toString());
		List<Product> dateCount = productDao.findByHql(hql.toString());
		List<Product> list = pageObject.getData();
		List list2 = new ArrayList();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				User u = userDao.findFirstByHql("from User where id=?", list
						.get(i).getShareId());
				Attention attention = attentionDao.findFirstByHql(
						" from Attention "
								+ "where attentionBy=? and attentionTo=?",
						userId, list.get(i).getShareId());
				Collection collection = collectionDao.findFirstByHql(
						" from Collection "
								+ "where collectBy=? and collectionId=?",
						userId, list.get(i).getId());
				ProductCount pc = productCountDao.findFirstByHql(
						" from ProductCount " + "where productId=?", list
								.get(i).getId());
				map.put("id", list.get(i).getId());
				map.put("title", list.get(i).getTitle());
				map.put("coveUrl", list.get(i).getCoveUrl());
				map.put("publishTime", list.get(i).getPublishTime().getTime());
				map.put("description", list.get(i).getDescription());
				map.put("message", list.get(i).getMessage());
				map.put("background", list.get(i).getBackground());
				if (null != pc) {
					map.put("beCollect", pc.getCollectCount());
				} else {
					map.put("beCollect", 0);
				}
				if (null != u) {
					map.put("nickName", u.getNickName());
					map.put("avater", u.getHeadImage());
				}
				if (null != attention && attention.getStatus() == 1) {
					map.put("attentionStatus", 1);
				} else {
					map.put("attentionStatus", 0);
				}
				if (null != collection && collection.getStatus() == 1) {
					map.put("collectionStatus", 1);
				} else {
					map.put("collectionStatus", 0);
				}
				list2.add(map);
			}
		}
		pageObject.setData(list2);
		pageObject.setDataCount(dateCount.size());
		return pageObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myphoto.service.AttentionService#getCollectProductList(com.myphoto
	 * .entity.base.PageObject, java.lang.String)
	 */
	@Override
	public PageObject getCollectProductList(PageObject pageObject, String userId) {
		List<Collection> list = collectionDao.findByHql(
				" from Collection v where 1=1 and v.status=1 and"
						+ " v.collectBy=?", userId);
		String str = "";
		if (list.size() > 0) {
			str += "(";
			for (int i = 0; i < list.size() - 1; i++) {
				// if(list.get(i).getAttentionTo()!=null&&list.get(i).getAttentionTo()>0){
				// str=str+list.get(i).getAttentionTo()+",";
				// }
				if (null != (list.get(i).getCollectionId())
						&& 0 != (list.get(i).getCollectionId())) {
					str = str + list.get(i).getCollectionId() + ",";
				}
			}
			if (null != list.get(list.size() - 1).getCollectionId()
					&& 0 != list.get(list.size() - 1).getCollectionId()) {
				str = str + list.get(list.size() - 1).getCollectionId();
			}
			str = str.endsWith(",") ? str.substring(0, str.length() - 1) : str;
			str += ")";
		}
		if (!str.equals("()") && !str.equals("")) {
			StringBuffer hql = new StringBuffer(
					"from Product where 1=1 and id in" + str);
			hql.append(" order by publishTime desc");

			PageObject pg = productDao.findForPageByHql(pageObject,
					hql.toString());
			List<Product> dateCount = productDao.findByHql(hql.toString());
			List<Product> list3 = pageObject.getData();
			List list2 = new ArrayList();
			if (list3.size() > 0) {
				for (int i = 0; i < list3.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					User pc = userDao.findFirstByHql("from User where id=?",
							list3.get(i).getShareId());
					Attention attention = attentionDao.findFirstByHql(
							" from Attention "
									+ "where attentionBy=? and attentionTo=?",
							userId, list3.get(i).getShareId());
					Collection collection = collectionDao.findFirstByHql(
							" from Collection "
									+ "where collectBy=? and collectionId=?",
							userId, list3.get(i).getId());
					map.put("id", list3.get(i).getId());
					map.put("title", list3.get(i).getTitle());
					map.put("coveUrl", list3.get(i).getCoveUrl());
					map.put("publishTime", list3.get(i).getPublishTime()
							.getTime());
					map.put("description", list3.get(i).getDescription());
					map.put("message", list3.get(i).getMessage());
					map.put("background", list3.get(i).getBackground());
					if (null != pc) {
						map.put("nickName", pc.getNickName());
						map.put("avater", pc.getHeadImage());
					}
					if (null != attention && attention.getStatus() == 1) {
						map.put("attentionStatus", 1);
					} else {
						map.put("attentionStatus", 0);
					}
					if (null != collection && collection.getStatus() == 1) {
						map.put("collectionStatus", 1);
					} else {
						map.put("collectionStatus", 0);
					}
					list2.add(map);
				}
			}
			pageObject.setData(list2);
			pageObject.setDataCount(dateCount.size());
			return pageObject;
		} else {
			pageObject = new PageObject();
			List l = new ArrayList();
			pageObject.setData(l);
			pageObject.setDataCount(l.size());
			return pageObject;
		}
	}

	@Override
	public PageObject getCollectArtShowList(PageObject pageObject, String userId) {
		List<RUserArtShow> list = rUserArtShowDao.findByHql(
				" from RUserArtShow v where 1=1 and v.status=1 and"
						+ " v.userId=?", userId);
		String str = "";
		if (list.size() > 0) {
			str += "(";
			for (int i = 0; i < list.size() - 1; i++) {
				// if(list.get(i).getAttentionTo()!=null&&list.get(i).getAttentionTo()>0){
				// str=str+list.get(i).getAttentionTo()+",";
				// }
				if (null != (list.get(i).getArtShowId())
						&& 0 != (list.get(i).getArtShowId())) {
					str = str + list.get(i).getArtShowId() + ",";
				}
			}
			if (null != list.get(list.size() - 1).getArtShowId()
					&& 0 != list.get(list.size() - 1).getArtShowId()) {
				str = str + list.get(list.size() - 1).getArtShowId();
			}
			str = str.endsWith(",") ? str.substring(0, str.length() - 1) : str;
			str += ")";
		}
		if (!str.equals("()") && !str.equals("")) {
			StringBuffer hql = new StringBuffer(
					"from ArtShow where 1=1 and id in" + str);
			hql.append(" order by createTime desc");

			PageObject pg = artShowDao.findForPageByHql(pageObject,
					hql.toString());
			List<ArtShow> dateCount = artShowDao.findByHql(hql.toString());
			List<ArtShow> list3 = pageObject.getData();
			List list2 = new ArrayList();
			if (list3.size() > 0) {
				for (int i = 0; i < list3.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					RUserArtShow rUserArtShow = rUserArtShowDao.findFirstByHql(
							" from RUserArtShow "
									+ "where userId=? and artShowId=?", userId,
							list3.get(i).getId());
					Long count = productArtShowDao
							.findCountBySql(
									"select count(*) from ProductArtShow where displayId=? ",
									list3.get(i).getId());
					map.put("id", list3.get(i).getId());
					map.put("title", list3.get(i).getTitle());
					if (count > 0) {
						map.put("beCollect", list3.get(i).getDescription());
					} else {
						map.put("beCollect", 0);
					}
					if (null != rUserArtShow && rUserArtShow.getStatus() == 1) {
						map.put("collectionTIme", rUserArtShow.getCollectTime()
								.getTime());
						map.put("collectionStatus", 1);
					} else {
						map.put("collectionTIme", "");
						map.put("collectionStatus", 0);
					}
					list2.add(map);
				}
			}
			pageObject.setData(list2);
			pageObject.setDataCount(dateCount.size());
			return pageObject;
		} else {
			pageObject = new PageObject();
			List l = new ArrayList();
			pageObject.setData(l);
			pageObject.setDataCount(l.size());
			return pageObject;
		}
	}

	@Override
	public PageObject getProductListBuUserId(PageObject pageObject,
			String userId) {
		StringBuffer hql = new StringBuffer("from Product where shareId='"
				+ userId);
		hql.append("' order by publishTime desc");
		PageObject pg = productDao.findForPageByHql(pageObject, hql.toString());
		List<Product> dateCount = productDao.findByHql(hql.toString());
		List<Product> list = pageObject.getData();
		List list2 = new ArrayList();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Collection collection = collectionDao.findFirstByHql(
						" from Collection "
								+ "where collectBy=? and collectionId=?",
						userId, list.get(i).getId());
				ProductCount pc = productCountDao.findFirstByHql(
						" from ProductCount " + "where productId=?", list
								.get(i).getId());
				ProductArtShow productArtShow = productArtShowDao
						.findFirstByHql(" from ProductArtShow "
								+ "where productId=?", list.get(i).getId());
				map.put("artShowTitle", "未参展");
				if (null != productArtShow) {
					ArtShow artShow = artShowDao.findFirstByHql(
							" from ArtShow " + "where id=?",
							productArtShow.getDisplayId());
					if (null != artShow) {
						map.put("artShowTitle", artShow.getTitle());
					}
				}
				map.put("id", list.get(i).getId());
				map.put("title", list.get(i).getTitle());
				map.put("coveUrl", list.get(i).getCoveUrl());
				map.put("publishTime", list.get(i).getPublishTime().getTime());
				// map.put("description", list.get(i).getDescription());
				// map.put("message", list.get(i).getMessage());
				// map.put("background", list.get(i).getBackground());
				if (null != pc) {
					map.put("beCollect", pc.getCollectCount());
				} else {
					map.put("beCollect", 0);
				}
				if (null != collection && collection.getStatus() == 1) {
					map.put("collectionStatus", 1);
				} else {
					map.put("collectionStatus", 0);
				}
				list2.add(map);
			}
		}
		pageObject.setData(list2);
		pageObject.setDataCount(dateCount.size());
		return pageObject;
	}

	@Override
	public PageObject getHotProductList(PageObject pageObject) {
		StringBuffer hql = new StringBuffer("from VProdCollectCount");
		PageObject pg = vProdCollectCountDao.findForPageByHql(pageObject,
				hql.toString());
		return pg;
	}

	@Override
	public PageObject getArtShowList(PageObject pageObject, String userId) {
		StringBuffer hql = new StringBuffer(
				"from ArtShow order by createTime desc");
		PageObject pg = artShowDao.findForPageByHql(pageObject, hql.toString());

		List<ArtShow> dateCount = artShowDao.findByHql(hql.toString());
		List<ArtShow> list = pageObject.getData();
		List list2 = new ArrayList();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Long artShowProdCount = productArtShowDao.findCountBySql(
						"select count(*)"
								+ " from ProductArtShow where displayId=?",
						list.get(i).getId());
				map.put("artShowProdCount", artShowProdCount);
				map.put("collectStatus", 0);
				if (!StringUtil.isBlank(userId)) {
					RUserArtShow rUserArtShow = rUserArtShowDao
							.findFirstByHql(
									" from RUserArtShow where status=1 and userId=? and artShowId=?",
									userId, list.get(i).getId());
					if(null!=rUserArtShow){
						map.put("collectStatus", 1);
					}
				}
				map.put("id", list.get(i).getId());
				map.put("title", list.get(i).getTitle());
				map.put("description", list.get(i).getDescription());
				map.put("ImgUrl", list.get(i).getImgUrl());
				map.put("createTime", list.get(i).getCreateTime());
				list2.add(map);
			}
		}
		pageObject.setData(list2);
		pageObject.setDataCount(dateCount.size());

		return pageObject;
	}

	@Override
	public List<VProdCollectCount> getTop3Product() {
		List<VProdCollectCount> listtem = vProdCollectCountDao
				.findByHql("from VProdCollectCount p where order by count desc");
		if (listtem.size() > 4) {
			listtem = listtem.subList(0, 4);
		}
		return listtem;
	}
}
