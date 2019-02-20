package com.myphoto.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myphoto.dao.PriMenuDao;
import com.myphoto.dao.PriRoleDao;
import com.myphoto.dao.PriRoleMenuDao;
import com.myphoto.dao.PriUserDao;
import com.myphoto.entity.PriMenu;
import com.myphoto.entity.PriRole;
import com.myphoto.entity.PriRoleMenu;
import com.myphoto.entity.PriUser;
import com.myphoto.entity.base.PageObject;
import com.myphoto.service.PriUserService;
import com.myphoto.util.StringUtil;

@Service
@Transactional
@SuppressWarnings("all")
public class PriUserServiceImpl implements PriUserService {

	@Autowired
	private PriUserDao priUserDao;
	@Autowired
	private PriRoleDao priRoleDao;
	@Autowired
	private PriRoleMenuDao priRoleMenuDao;
	@Autowired
	private PriMenuDao priMenuDao;

	@Override
	public void saveOrUpdatePriUser(PriUser t) {
		priUserDao.saveOrUpdate(t);

	}

	@Override
	public PriUser getPriUserById(Integer id) {
		return priUserDao.get(id);
	}

	@Override
	public PriUser getPriUserByUserName(String userName) {
		return priUserDao.findFirstByHql("from PriUser where userName=?",
				userName);
	}

	@Override
	public PriUser getPubUserByToken(String token) {
		return priUserDao.findFirstByHql("from PriUser where token=?", token);
	}

	@Override
	public PageObject getPriUserPO(PageObject pageObject, PriUser t) {
		StringBuffer hql = new StringBuffer("from PriUser where 1=1");
		if (!StringUtil.isBlank(t.getUserName()))
			hql.append(" and userName like '" + t.getUserName() + "%'");
		hql.append(" order by id desc");
		return priUserDao.findForPageByHql(pageObject, hql.toString());
	}

	@Override
	public PriRole getPriRoleById(Integer id) {
		return priRoleDao.get(id);
	}

	public void saveOrUpdatePriRole(PriRole role, String[] menuids) {

		priRoleDao.updateBySql("delete from PRI_ROLE_MENU where roleId = ?0",
				role.getId());
		priRoleDao.saveOrUpdate(role);
		addMenus(role.getId(), menuids);

	}

	public void addMenus(Integer roleId, String[] menuids) {
		PriRoleMenu prm;
		// 批量添加记录进角色、菜单关联表id
		int id;
		for (String pid : menuids) {
			if (pid == null || pid.length() == 0) {
				continue;
			}
			id = Integer.valueOf(pid);
			if (id <= 0) {
				continue;
			}
			prm = new PriRoleMenu();
			prm.setRoleId(roleId);
			prm.setMenuId(id);
			priRoleMenuDao.saveOrUpdate(prm);
			priRoleMenuDao.flush();
		}
	}

	@Override
	public String getMenuIdsByRoleId(Integer roleId) {
		try {
			List<PriMenu> list = getUserMenu(roleId);
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				String menuid = list.get(i).getId().toString();
				buf.append(',');
				buf.append(menuid);
			}
			buf.append(',');
			return buf.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<PriMenu> getAllMenu() {
		return priMenuDao.findByHql("from PriMenu where status=1 order by id asc");
	}

	@Override
	public List<PriMenu> getUserMenu(Integer roleId) {
		List<PriMenu> pmList = new ArrayList<PriMenu>();
		List list = priMenuDao
				.findByHql(
						"from PriRoleMenu a,PriMenu b where  b.id=a.menuId and a.roleId=? and b.id!=1 and b.status=1 order by b.orderId asc",
						roleId);
		for (int i = 0; i < list.size(); i++) {
			Object[] ob = (Object[]) list.get(i);
			if (ob[1] != null) {
				pmList.add((PriMenu) ob[1]);
			}
		}
		return pmList;
	}

	public List<PriMenu> getMenuListByLv(Integer lv) {
		return priMenuDao.findByHql(
				"from PriMenu  where menuLevel=? and status=1 order by orderId asc", lv);
	}

	@Override
	public PageObject getPriRolePO(PageObject pageObject, PriRole t) {
		StringBuffer hql = new StringBuffer("from PriRole where 1=1");
		if (!StringUtil.isBlank(t.getName()))
			hql.append("  and name like '" + t.getName() + "%'");
		hql.append(" order by id desc");
		return priRoleDao.findForPageByHql(pageObject, hql.toString());
	}

	public List<PriRole> getPriRoleList() {
		return priRoleDao.findByHql("from PriRole");
	}

	@Override
	public void saveOrUpdatePriMenu(PriMenu t) {
		priMenuDao.saveOrUpdate(t);
	}

	@Override
	public void deleteMenu(PriMenu t) {
		priMenuDao.delete(t.getId());
	}

	@Override
	public PriMenu getPriMenuById(Integer id) {
		return priMenuDao.get(id);
	}

	@Override
	public PageObject getPriMenuPO(PageObject pageObject, PriMenu t) {
		StringBuffer hql = new StringBuffer("from PriMenu a,PriMenu b where a.id>1 and a.pMenuId=b.id and a.status=1 and b.status=1 ");
		if (!StringUtil.isBlank(t.getName())){
			hql.append("  and a.name like '" + t.getName() + "%'");
		}
		hql.append(" order by a.orderId asc");
		pageObject = priMenuDao.findForPageByHql(pageObject, hql.toString());
		List<Object[]> list = pageObject.getData();
		List<PriMenu> res = new ArrayList<PriMenu>();
		for (int i = 0; i < list.size(); i++) {
			PriMenu t1 = (PriMenu) list.get(i)[0];
			PriMenu t2 = (PriMenu) list.get(i)[1];
			t1.setpMenuName(t2.getName());
			res.add(t1);
		}
		pageObject.setData(res);
		return pageObject;
	}
}
