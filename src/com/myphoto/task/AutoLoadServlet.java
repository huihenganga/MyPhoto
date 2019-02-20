package com.myphoto.task;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.logicalcobwebs.proxool.ProxoolFacade;

public class AutoLoadServlet extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init();
	}
	@Override
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		ProxoolFacade.shutdown();
	}

}
