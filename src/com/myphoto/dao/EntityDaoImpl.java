package com.myphoto.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.myphoto.entity.base.PageObject;

/**
 * Dao层接口的公共实现类
 * 
 * @author Jason
 * @version 1.0
 * @param <T>
 *            范型，指实体类
 * @param <PK>
 *            范型，指实体类主键的数据类型，如Integer,Long,String
 * @see com.asfrom.netshop.dao.common.EntityDao
 * @date 2014-05-13
 */

@Repository
@Transactional
@SuppressWarnings("all")
public class EntityDaoImpl<T, PK extends Serializable> implements
		EntityDao<T, PK> {
	@Autowired
	private SessionFactory sessionFactory;

	protected Class<T> entityClass;// DAO所管理的Entity类型.

	/**
	 * 在构造函数中将泛型T.class赋给entityClass.
	 */

	public EntityDaoImpl() {
		entityClass = AnnotationUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 保存实体 包括添加和修改
	 * 
	 * @param t
	 *            实体对象
	 */
	public void saveOrUpdate(T t) {
		sessionFactory.getCurrentSession().saveOrUpdate(t);
	}
	
	/**
	 * 从session中剥离实体
	 * @param t
	 */
	public void evict(T t) {
		sessionFactory.getCurrentSession().evict(t);
	}


	/** 批量删除对象 **/
	public void deleteList(List<T> list) throws Exception {
		sessionFactory.getCurrentSession().delete(list);
	}

	/**
	 * 更新实体
	 * 
	 * @param t
	 *            实体对象
	 */
	public void merge(T t) {
		sessionFactory.getCurrentSession().merge(t);
	}

	/**
	 * 更新实体 可用于添加、修改、删除操作
	 * 
	 * @param hql
	 *            更新的HQL语句
	 * @param params
	 *            参数,可有项目或多项目,代替Hql中的"?"号
	 */
	public void updateBySql(String hql, Object... params) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(convertSql(hql, params));
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + "", params[i]);
		}
		query.executeUpdate();
	}


	/**
	 * 删除实体
	 * 
	 * @param t
	 *            实体对象
	 */
	public void delete(PK t) {
		sessionFactory.getCurrentSession().delete(get(t));
	}

	/**
	 * 删除实体
	 * 
	 * @param t
	 *            实体对象
	 */
	public void delete(T t) {
		sessionFactory.getCurrentSession().delete(t);
	}

	/**
	 * 单查实体
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param id
	 *            实体的ID
	 * @return 实体对象
	 */
	public T get(PK id) {
		return (T) sessionFactory.getCurrentSession().get(entityClass, id);
	}



	/**
	 * 按HQL条件查询列表
	 * 
	 * @param hql
	 *            查询语句,支持连接查询和多条件查询
	 * @param params
	 *            参数数组,代替hql中的"?"号
	 * @return 结果集List
	 */
	public List<T> findByHql(String hql, Object... params) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				convertSql(hql, params));
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + "", params[i]);
		}
		List<T> list = query.list();
		return list;

	}

	/**
	 * 按SQL条件查询列表
	 * 
	 * @param sql
	 *            查询语句,支持连接查询和多条件查询
	 * @param params
	 *            参数数组,代替hql中的"?"号
	 * @return 结果集List
	 */
	public List findBySql(String sql, Object... params) {
		String sqls = convertSql(sql, params);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqls);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + "", params[i]);
		}
		List list = query.list();
		return list;
	}
	/**
	 * 按SQL条件查询列表
	 * 
	 * @param sql
	 *            查询语句,支持连接查询和多条件查询
	 * @param params
	 *            参数数组,代替hql中的"?"号
	 * @return 结果集List
	 */
	public Long findCountBySql(String sql, Object... params) {
		String sqls = convertSql(sql, params);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqls);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + "", params[i]);
		}
		List list = query.list();
		Object o =list.get(0);
		if(o instanceof BigInteger){
			return ((BigInteger)o).longValue();
		}else if(o instanceof BigDecimal){
			return ((BigDecimal)o).longValue();
		}else if(o instanceof Long){
			return (Long)o;
		}else if(o instanceof Integer){
			return (Integer)o*1L;
		}else{
			return 0L;
		}
	}
	/**
	 * 按HQL条件查询列表第一个
	 * 
	 * @param hql
	 *            查询语句,支持连接查询和多条件查询
	 * @param params
	 *            参数数组,代替hql中的"?"号
	 * @return 结果集List
	 */
	@SuppressWarnings("unchecked")
	public T findFirstByHql(String hql, Object... params) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				convertSql(hql, params));
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + "", params[i]);
		}
		List<T> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 按HQL分页查询
	 * 
	 * @param firstResult
	 *            开始记录号
	 * @param maxResults
	 *            最大记录数
	 * @param hql
	 *            查询语句,支持连接查询和多条件查询
	 * @param params
	 *            参数数组,代替餐hql中的"?"号
	 * @return 封装List和total的Pager对象
	 */
	public PageObject findForPageByHql(PageObject pageObject, String hql,
			Object... params) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				convertSql(hql, params));
		String maxsizeStr = (" select count(*) " + convertSql(hql, params));
		Query q = sessionFactory.getCurrentSession().createQuery(maxsizeStr);
		if (params != null) {
			for (int position = 0; position < params.length; position++) {
				query.setParameter(position + "", params[position]);
				q.setParameter(position + "", params[position]);
			}
		}
		Long totalCounts = 0L;
		List l = q.list();
		if (l != null && !l.isEmpty()) {
			totalCounts = (Long) l.get(0);
		}
		// 用于分页查询
		if (pageObject.getPageSize() > 0) {
			query.setFirstResult((pageObject.getCurPage() - 1)
					* pageObject.getPageSize());
			query.setMaxResults(pageObject.getPageSize());
		}
		List<T> list = query.list();
		pageObject.setData(list);
		pageObject.setDataCount(totalCounts.intValue());
		return pageObject;
	}
		
	/**
	 * 按SQL分页查询
	 * 
	 * @param firstResult
	 *            开始记录号
	 * @param maxResults
	 *            最大记录数
	 * @param hql
	 *            查询语句,支持连接查询和多条件查询
	 * @param params
	 *            参数数组,代替餐hql中的"?"号
	 * @return 封装List和total的Pager对象
	 */
	public PageObject findForPageBySql(PageObject pageObject, String sql,
			String selectString) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(
				selectString + sql);
		String maxsizeStr = (" select count(*) " + sql);
		Query q = sessionFactory.getCurrentSession().createSQLQuery(maxsizeStr);
		Long totalCounts = 0L;
		List l = q.list();
		if (l != null && !l.isEmpty()) {
			if (l.get(0) instanceof BigInteger)
				totalCounts = ((BigInteger) l.get(0)).longValue();
			else if (l.get(0) instanceof BigDecimal)
				totalCounts = ((BigDecimal) l.get(0)).longValue();
		}
		// 用于分页查询
		if (pageObject.getPageSize() > 0) {
			query.setFirstResult((pageObject.getCurPage() - 1)
					* pageObject.getPageSize());
			query.setMaxResults(pageObject.getPageSize());
		}
		List<T> list = query.list();
		pageObject.setData(list);
		pageObject.setDataCount(totalCounts.intValue());
		return pageObject;
	}

	public PageObject findForPageBySql(PageObject pageObject, String sql) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		int frompos = sql.indexOf("from");
		String maxsizeStr = (" select count(*) " + sql.substring(frompos));
		Query q = sessionFactory.getCurrentSession().createSQLQuery(maxsizeStr);
		Long totalCounts = 0L;
		List l = q.list();
		if (l != null && !l.isEmpty()) {
			if (l.get(0) instanceof BigInteger)
				totalCounts = ((BigInteger) l.get(0)).longValue();
			else if (l.get(0) instanceof BigDecimal)
				totalCounts = ((BigDecimal) l.get(0)).longValue();
		}
		// 用于分页查询
		if (pageObject.getPageSize() > 0) {
			query.setFirstResult((pageObject.getCurPage() - 1)
					* pageObject.getPageSize());
			query.setMaxResults(pageObject.getPageSize());
		}
		List<T> list = query.list();
		pageObject.setData(list);
		pageObject.setDataCount(totalCounts.intValue());
		return pageObject;
	}
	//
	/** 清除缓存 **/
	public void clear() {
		sessionFactory.getCurrentSession().clear();
	}

	/** 刷新缓存 **/
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	// 格式化占位符,用?0 ?1 代替原先的单独?,?
	private String convertSql(String hql, Object... params) {
		hql = hql.trim();
		if (!hql.contains("?0")) {
			String[] hqls = hql.split("\\?");
			StringBuffer sqlsbf = new StringBuffer();
			for (int i = 0; i < hqls.length; i++) {
				if (i == hqls.length - 1 && !hql.endsWith("?")) {
					sqlsbf.append(hqls[i]);
				} else {
					sqlsbf.append(hqls[i] + "?" + i + " ");
				}
			}
			hql = sqlsbf.toString();
		}
		return hql;
	}
}
