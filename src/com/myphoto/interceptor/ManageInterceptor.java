package com.myphoto.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.myphoto.entity.PriRole;
import com.myphoto.entity.PriUser;
import com.myphoto.exception.LoginException;
import com.myphoto.service.PriUserService;
import com.myphoto.util.LoginUser;
import com.myphoto.util.SessionContainer;

public class ManageInterceptor implements HandlerInterceptor {

	@Autowired
	private PriUserService priUserService;

	/**
	 * 完成页面的render后调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {

	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在调用controller具体方法前拦截 这边添加验证是否登录的代码
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		SessionContainer container = (SessionContainer) request.getSession()
				.getAttribute("CONTAINER_SESSION");
		// 如果session中无数据.看是否有cookie
		if (container == null || container.getLoginUser() == null) {
			Cookie[] cookies = request.getCookies();// 这样便可以获取一个cookie数组
			String userId = "";
			String roleId = "";
			if (cookies == null) {
				throw new LoginException("登录超时,请重新登录");
			}
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("userId")) {
					userId = cookie.getValue();
				}
				if (cookie.getName().equals("roleId")) {
					roleId = cookie.getValue();
				}
			}
			if (userId.equals("")||roleId.equals("")) {
				throw new LoginException("登录超时,请重新登录");
			}
			PriUser priUser = priUserService.getPriUserById(Integer.parseInt(userId));
			PriRole priRole = priUserService.getPriRoleById(Integer.parseInt(roleId));
			if(priUser==null||priRole==null){
				throw new LoginException("登录超时,请重新登录");
			}
			LoginUser loginUser = new LoginUser();
			loginUser.setUserId(priUser.getId());
			loginUser.setName(priUser.getName());
			loginUser.setRoleId(priRole.getId());
			loginUser.setRoleName(priRole.getName());
			loginUser.setLoginIP(request.getRemoteAddr());
			container = new SessionContainer();
			container.setLoginUser(loginUser);
			request.getSession().setAttribute("CONTAINER_SESSION", container);
		}
		return true;
	}
}
