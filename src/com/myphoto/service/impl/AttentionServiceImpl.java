package com.myphoto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myphoto.dao.AttentionDao;
import com.myphoto.dao.UserDao;
import com.myphoto.entity.Attention;
import com.myphoto.entity.User;
import com.myphoto.entity.base.PageObject;
import com.myphoto.service.AttentionService;

@Service
@Transactional
@SuppressWarnings("all")
public class AttentionServiceImpl implements AttentionService {

	@Autowired
	private AttentionDao attentionDao;
	@Autowired
	private UserDao userDao;

	@Override
	public Integer getAttentionCountByUserId(String userId) {
		Long count = attentionDao.findCountBySql(
						"select count(*) from Attention where attentionBy=? and status=1",userId );
		return count.intValue();
	}

	@Override
	public Integer getFansCountByUserId(String userId) {
		Long count = attentionDao.findCountBySql(
						"select count(*) from Attention where attentionTo=? and status=1",userId );
		return count.intValue();
	}
	
	@Override
	public void saveOrUpdateAttention(Attention t) {
		attentionDao.saveOrUpdate(t);
	}

	@Override
	public Attention getAttentionByTwoUserId(String beAttentionId, String attentionBy) {
		return attentionDao.findFirstByHql(
				"from Attention where attentionBy=? and attentionTo=?",attentionBy,beAttentionId);
	}
	
	@Override
	public PageObject getAttentionByUserId(PageObject pageObject,String userId) {
		String str = "";
		List<Attention> list= new ArrayList<Attention>();
		list= attentionDao.findByHql(" from Attention where 1=1 and status=1 and attentionBy=? " +
				"order by attentionTime desc ",userId);
			
		str = "";
		if(null!=list&&list.size()>0){
			str+="(";
			for(int i=0;i<list.size()-1;i++){
				str=str+list.get(i).getAttentionTo()+",";
			}
			str=str+list.get(list.size()-1).getAttentionTo();
			str+=")";
		}
		if(!"".equals(str)){
			String orderStr = str.replace("(", "field(id,");
			StringBuffer hql = new StringBuffer("from User t where 1=1 and id in "+str+" order by "+orderStr);
			pageObject = userDao.findForPageByHql(pageObject, hql.toString());
			List<User> uList = pageObject.getData();
			List l = new ArrayList();
			for(int i=0;i<uList.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", uList.get(i).getId());
				map.put("nickName", uList.get(i).getNickName());
				map.put("headImage", uList.get(i).getHeadImage());
				l.add(map);
			}
			pageObject.setData(l);
			return pageObject;
		}else{
			pageObject = new PageObject();
			return pageObject;
		}
	}
	
	@Override
	public PageObject getFansByUserId(PageObject pageObject, String userId) {
		List<Attention> list= attentionDao.findByHql(" from Attention where 1=1 and status=1 and attentionTo=? " +
				"order by attentionTime desc ",userId);
		String str = "";
		if(list!=null&&list.size()>0){
			str+="(";
			for(int i=0;i<list.size()-1;i++){
				str=str+list.get(i).getAttentionBy()+",";
			}
			str=str+list.get(list.size()-1).getAttentionBy();
			str+=")";
		}
		if(!"".equals(str)){
			String orderStr = str.replace("(", "field(id,");
			StringBuffer hql = new StringBuffer("from User t where 1=1 and id in "+str+" order by "+orderStr);
			pageObject = userDao.findForPageByHql(pageObject, hql.toString());
			List<User> uList = pageObject.getData();
			List l = new ArrayList();
			for(int i=0;i<uList.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", uList.get(i).getId());
				map.put("nickName", uList.get(i).getNickName());
				map.put("headImage", uList.get(i).getHeadImage());
				Attention attention = attentionDao.findFirstByHql(
						"from Attention where status=1 and attentionBy=? and attentionTo=?",userId,uList.get(i).getId());
				if(attention!=null&&attention.getStatus()==1){
					map.put("attentionStatus",1);
				}else{
					map.put("attentionStatus",0);
				}
				Attention attention1 = attentionDao.findFirstByHql(
						"from Attention where status=1 and attentionBy=? and attentionTo=?",uList.get(i).getId(),userId);
				map.put("attentionTime",attention1.getAttentionTime().getTime());
				l.add(map);
				//pageObject = new PageObject();
			}
			pageObject.setData(l);
		}else{
		}
		return pageObject;
	}
	
}
