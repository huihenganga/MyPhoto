package com.myphoto.service;

import java.util.List;

import com.myphoto.entity.PriMenu;
import com.myphoto.entity.PriRole;
import com.myphoto.entity.PriUser;
import com.myphoto.entity.base.PageObject;

public interface PriUserService {

	// 后台用户
	public void saveOrUpdatePriUser(PriUser t);

	public PriUser getPriUserById(Integer id);

	public PriUser getPriUserByUserName(String userName);

	public PriUser getPubUserByToken(String token);

	public PageObject getPriUserPO(PageObject pageObject, PriUser t);

	// 角色
	
	public void saveOrUpdatePriRole(PriRole t,String[] menuids);

	public PriRole getPriRoleById(Integer id);

	public PageObject getPriRolePO(PageObject pageObject, PriRole t);

	public List<PriRole> getPriRoleList();

	// 菜单
	public String getMenuIdsByRoleId(Integer roleId);
	
	public List<PriMenu> getAllMenu();
	
	public List<PriMenu> getUserMenu(Integer roleId);

	public List<PriMenu> getMenuListByLv(Integer lv);

	public void saveOrUpdatePriMenu(PriMenu t);

	public void deleteMenu(PriMenu t);

	public PriMenu getPriMenuById(Integer id);

	public PageObject getPriMenuPO(PageObject pageObject, PriMenu t);

	//
}
