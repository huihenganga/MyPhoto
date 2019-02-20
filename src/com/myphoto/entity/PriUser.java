package com.myphoto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PRI_USER")
public class PriUser {
	private Integer id;//
	private String name;// 姓名
	private Integer status = 1;// 状态1正常,0禁用
	private String statusValue;// 状态中文输出
	private String userName;// 登录名
	private String password;// 密码
	private String ygdm;// 员工代码(如果后台用户有绑定医生信息的话保存)
	private Date createDate;//
	private String remark;//
	private String token;
	private String roleName;// 角色显示名

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getYgdm() {
		return ygdm;
	}

	public void setYgdm(String ygdm) {
		this.ygdm = ygdm;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}// 登录验证token

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Transient
	public String getStatusValue() {
		if (this.status != null) {
			this.statusValue = this.status == 1 ? "正常" : "禁用";
		} else {
			this.statusValue = "无";
		}
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue=statusValue;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
