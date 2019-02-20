package com.myphoto.controller.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.myphoto.entity.User;
import com.myphoto.exception.LoginException;
import com.myphoto.service.UserService;
import com.myphoto.util.BarberShopSessionContext;
import com.myphoto.util.SessionContainer;


@Component
public class BaseManageController {

	private static Logger log = LogManager.getLogger(BaseManageController.class);
	
	@Autowired
	private UserService userService;
 	
	
	@ExceptionHandler
	public String exp(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//判断是否是接口,接口返回json,非接口返回jsp,判断依据,uri是否含有_
			boolean isApi = false;
			String uri = request.getRequestURI();
			if(uri.contains("_")){
				isApi=true;
			}
			if (ex instanceof LoginException) {//登录异常
				if(!isApi){
					request.setAttribute("msg", ex.getMessage());
					return "redirect:/error/noSession.jsp";
				}else{
					map.put("ret", "-1");
					map.put("msg", ex.getMessage());
					write(response,JSONObject.fromObject(map).toString());
					return null;	
				}
				
			}
			if(!isApi){
				request.setAttribute("msg", ex.getMessage());
				ex.printStackTrace();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				ex.printStackTrace(new PrintStream(baos));  
				String exception = baos.toString();  
				log.error("捕获到非自定义异常:"+ex.getClass().getName()+","+",msg:"+ex.getMessage()+"\r\n"+exception);
				return "redirect:/error/500.jsp";
			}else{
				map.put("ret", "0");
				map.put("msg", "系统异常,请稍后再试!");
				write(response,JSONObject.fromObject(map).toString());
				ex.printStackTrace();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				ex.printStackTrace(new PrintStream(baos));  
				String exception = baos.toString();  
				log.error("捕获到非自定义异常:"+ex.getClass().getName()+","+",msg:"+ex.getMessage()+"\r\n"+exception);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	public void write(HttpServletResponse response,String obj){
    	Writer writer;
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			writer = response.getWriter();
	    	writer.write(obj);
	    	writer.flush();
	    	writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public User getPubUserFromSession(String token){
		HttpSession session = BarberShopSessionContext.getInstance().getSession(token);
		User user = userService.getUserByOpenId((String) session.getAttribute("openId"));
		return user;
	}
	
	/**
	 * @description: 取session容器 SessionContainer对象
	 * @param:
	 * @return:
	 * @throws:
	 */
	public SessionContainer getSessionContainer(HttpServletRequest request,
			HttpServletResponse response) {
		SessionContainer container = (SessionContainer) request.getSession()
				.getAttribute("CONTAINER_SESSION");
		if (container == null) {
			container = new SessionContainer();
			request.getSession().setAttribute("CONTAINER_SESSION", container);
		}
		return container;
	}
}
