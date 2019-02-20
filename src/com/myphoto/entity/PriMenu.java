package com.myphoto.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

//菜单表
@Entity
@Table(name="PRI_MENU")
public class PriMenu {
	private Integer id;//ID,主键
	private String name;//菜单名称
	private Integer menuLevel;//菜单级别,	1:一级菜单2:二级菜单
	private Integer status=1;   //1在用,0禁用
	private Integer pMenuId;//父节点ID,一级菜单父节点ID为1,二级菜单为实际父节点ID
	private String pMenuName;//备注
    private Integer orderId;//排序号
	private String url;//菜单地址
	private String remark;//备注
	private Date createDate;//创建日期
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	
	public Integer getpMenuId() {
		return pMenuId;
	}
	public void setpMenuId(Integer pMenuId) {
		this.pMenuId = pMenuId;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Transient
	public String getpMenuName() {
		return pMenuName;
	}
	public void setpMenuName(String pMenuName) {
		this.pMenuName = pMenuName;
	}

	

}
