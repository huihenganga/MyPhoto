package com.myphoto.contants;


/**<p>名称：DescribableEnum.java</p>
 * <p>描述：可描述枚举</p>
 * <pre>
 */
public enum DescribableEnum implements Describable
{
	// 1-10000 系统描述枚举
	LOGIN_ERROR("-1", "登录超时"),
	SYSTEM_ERROR("0", "系统异常,请稍后再试"),
	SUCCESS("1", "成功"),
	FAIL("2", "失败"),
	PARAMES_ERROR("3", "参数异常,请检查!"),
	SOA_CONNECT_ERROR("0","SOA连接失败"),
	// 10001-20000  用户错误描述
	USER_EXIST("10001", "该微信号已注册过,请检查!"),
	USER_NOT_EXIST("10002", "该微信号未注册过,请检查!"),
	USER_PASSWORD_ERROR("10003", "登录失败,请确认用户名和密码是否正确!"),
	
	OLD_PASSWORD_ERROR("10004", "旧密码验证失败"),
	
	SMS_COUNT_ERROR("10007", "24小时内发送的验证短信数量超限,请稍后再试!"),
	
	SMS_NOT_EXIST("10005", "验证码失效,请选择重新发送!"),
	SMSCODE_ERROR("10006", "验证码错,请确认!"),
	
	HAVE_BIND("20001", "该就诊卡您已绑定,不能重复绑定!"),
	BRDA_SEARCH_FAIL("20002", "就诊卡查询失败!")
	;
	
	private String code;// 描述编码
	private String msg;// 描述信息
	
	private DescribableEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
