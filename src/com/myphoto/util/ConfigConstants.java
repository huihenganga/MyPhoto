package com.myphoto.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*******************************************************************************
 * 
 * @author
 * @常量配置
 * 
 ******************************************************************************/
public abstract class ConfigConstants {

	private static Logger log = LogManager.getLogger(ConfigConstants.class);

	private static Properties properties = new Properties();

	private static final String DEFAULT_FILE = "config/config.properties";

	static {
		init();
	}

	public static final String SYSTEM_CODING = getProperty("system.i18n.encoding");// 字符集

	public static final String VERIFY_CODE_DIR = getProperty("verify.code.dir");// 验证码路径

	public static final String TEMPLATE_FILE_DIR = getProperty("template.file.dir");// 模板文件路径

	public static final String EXPORT_TEMP_DIR = getProperty("export.temp.dir");// 存放导出文件的临时目录

	// 静态读入属性文件到properties变量中
	private static void init() {
		InputStream in = null;
		try {
			in = ConfigConstants.class.getClassLoader().getResourceAsStream(
					DEFAULT_FILE);
			if (in != null) {
				properties.load(in);
			}
		} catch (IOException e) {
			log.error("load Constants file error!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}


	public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static Integer getIntProperty(String key) {
		return Integer.valueOf(getProperty(key));
	}

	public static Integer getIntProperty(String key, String defaultValue) {
		return Integer.valueOf(getProperty(key, defaultValue));
	}

	public static void main(String[] args) {
		// try {
		// System.out.println(getProperty("broadband.agentId"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
	}
}
