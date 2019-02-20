package com.myphoto.controller.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import com.myphoto.entity.User;
import com.myphoto.exception.LoginException;
import com.myphoto.service.UserService;
import com.myphoto.util.SessionContainer;


@Component
public class BaseAppController {

	private static Logger log = LogManager.getLogger(BaseAppController.class);
	
	@Autowired
	private UserService userService;
 	
	
	@ExceptionHandler
	public String exp(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if (ex instanceof LoginException) {//登录异常
				map.put("ret", "-1");
				map.put("msg", ex.getMessage());
				write(response,JSONObject.fromObject(map).toString());
				return null;	
			} 
			map.put("ret", "0");
			map.put("msg", "系统异常,请稍后再试!");
			//非自定义异常转存去log文件,提供调试依据
			ex.printStackTrace();
			write(response,JSONObject.fromObject(map).toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			ex.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			log.error("捕获到非自定义异常:"+ex.getClass().getName()+","+",msg:"+ex.getMessage()+"\r\n"+exception);
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
	public String getOpenIdFromSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		return (String) session.getAttribute("openId");
	}
	public User getUserFromSession(HttpServletRequest request){
		HttpSession session = request.getSession();
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
	
	public String savePic(HttpServletRequest request,
			HttpServletResponse response, MultipartFile file, Integer uid) {
		String realPath = request.getSession().getServletContext()
				.getRealPath("/");
		String PATH = "\\upload\\userImage\\";
		if (file == null) {
			return "no.jpg";
		}
		String fileName = "";
		String logImageName = "";
		if (file.isEmpty()) {
			System.out.println("文件未上传");
		} else {
			String _fileName = file.getOriginalFilename();
			String suffix = _fileName.substring(_fileName.lastIndexOf("."));
			// /**使用UUID生成文件名称**/
			logImageName = UUID.randomUUID().toString() + suffix;
			String path = realPath + PATH + uid + "\\";
			File f = new File(path);
			if (!f.exists() && !f.isDirectory()) {
				f.mkdirs();
			}
			fileName = path + logImageName;
			File restore = new File(fileName);
			try {
			//	restore.createNewFile();
				file.transferTo(restore);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		// 返回默认的图片地址
		return PATH + uid + "\\" + logImageName;
	}
}
