package com.myphoto.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.servlet.http.HttpServletRequest;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author
 * @version
 * @see
 * @description: 公共用户相关Controller
 * @log:
 */

public class PubUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PubUtil.class);

	public static String driverClassName = "";
	public static String url = "";
	public static String username = "";
	public static String password = "";
	public static String docRoleName = "医生";
	public static Integer docRoleId = 31;
	public static JsonConfig config = null;
	public static JsonConfig config2 = null;
	public static String wxAppId = "";
	public static String wxSecret = "";
	public static String wxMchId = "";
	public static String unMchId = "";
	public static String jpushId = "";
	public static String jpushSecret = "";
	public static boolean jpushProduct = false;
	static {
		try {
			Properties prop = new Properties();
			InputStream in = new ClassPathResource("config/jdbc.properties")
					.getInputStream();
			prop.load(in);
			driverClassName = prop.getProperty("jdbc.driverClassName").trim();
			url = prop.getProperty("jdbc.url").trim();
			username = prop.getProperty("jdbc.username").trim();
			password = prop.getProperty("jdbc.password").trim();

			prop = new Properties();
			in = new ClassPathResource("config/config.properties")
					.getInputStream();
			prop.load(in);
			/*wxAppId =  prop.getProperty("wx.AppID").trim();
			wxSecret =  prop.getProperty("wx.AppSecret").trim();
			wxMchId =  prop.getProperty("wx.mchId").trim();
			unMchId =  prop.getProperty("un.mchId").trim();
			jpushId =  prop.getProperty("jpush.appid").trim();
			jpushSecret =  prop.getProperty("jpush.secret").trim();
			String prodcut = prop.getProperty("jpush.product").trim();
			if("1".equals(prodcut)){
				jpushProduct=true;
			}*/
			config = new JsonConfig();// JSON转换DATE处理（date类型不经过处理报错）
			config.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd"));
			config.setJsonPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					return value == null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 带日期+时间的转换
	 * 
	 * @param date
	 * @return dateTimeString
	 */
	public static String dateTimeToString(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String dateToString(Date date,String formmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(formmt);
		if (date == null) {
			return "";
		}
	
		return sdf.format(date);
	}
	/**
	 * @Description 字符串转换为date
	 * @param date
	 * @return String
	 * @throws ParseException 
	 * @throws
	 * @author 罗渲
	 * @date 2015-10-9  下午3:58:56
	 */
	public static Date strToDate(String str,String type) {
		try {
			if(str==null||str.trim().equals("")){
				return null;
			}
			if (StringUtil.isBlank(str)) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(type);
			return sdf.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * 一段文字,把其中中文转化为拼音(大写).其他(数字和标点英文跳过)
	 */
	public static String getStringPinYin(String str) {
		StringBuilder sb = new StringBuilder();
		String tempPinyin = null;
		for (int i = 0; i < str.length(); ++i) {
			tempPinyin = getCharacterPinYin(str.charAt(i));
			if (tempPinyin != null) {
				sb.append(tempPinyin.substring(0, 1).toUpperCase());
			}
		}
		return sb.toString();
	}

	private static String getCharacterPinYin(char c) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String[] pinyin;
		try {
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
			// 如果c不是汉字，toHanyuPinyinStringArray会返回null
			if (pinyin == null)
				return null;

			// 只取一个发音，如果是多音字，仅取第一个发音
			return pinyin[0];
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 单日期的转换,不含时间
	 * 
	 * @param date
	 * @return dateString
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null) {
			return "";
		}

		return sdf.format(date);
	}
	public static String dateToString3(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null) {
			return "";
		}

		return sdf.format(date);
	}
	/**
	 * 单日期的转换,不含时间
	 * 
	 * @param date
	 * @return dateString
	 */
	public static String dateToString2(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(date);
	}

	public static String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						macAddress = str.substring(
								str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}

	public static String getHttpPostResult(String url,
			List<NameValuePair> params) {
		String body = "";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost method = new HttpPost(url);
		try {
			method.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			// 设置编码
			CloseableHttpResponse response = client.execute(method);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				body = "-1";
			} else {
				body = EntityUtils.toString(response.getEntity());
			}
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			body = "0";
		} finally {
			// 关闭连接 ,释放资源
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return body;
	}


	
	public static JsonConfig getJsonConfig(String dateFormat) {
		if (config == null) {
			config = new JsonConfig();// JSON转换DATE处理（date类型不经过处理报错）
			config.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor(dateFormat));
			config.setJsonPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					return value == null;
				}
			});
		}
		return config;
	}
	public static JsonConfig getJsonConfig() {
		if (config == null) {
			config = new JsonConfig();// JSON转换DATE处理（date类型不经过处理报错）
			config.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd"));
			config.setJsonPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					return value == null;
				}
			});
		}
		return config;
	}

	public static JsonConfig getJsonConfig2() {
		if (config2 == null) {
			config2 = new JsonConfig();// JSON转换DATE处理（date类型不经过处理报错）
			config2.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("MM月dd日"));
			config2.setJsonPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					return value == null;
				}
			});
		}
		return config2;
	}
	
	/**
	 * @param dateType(yyyy-MM-dd,yyyy年MM月dd日)
	 * @return
	 */
	public static JsonConfig getJsonConfig3(String dateType) {
		JsonConfig config3 = new JsonConfig();// JSON转换DATE处理（date类型不经过处理报错）
			config3.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor(dateType));
			config3.setJsonPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					return value == null;
				}
			});
		return config3;
	}
	
	// public static JsonConfig getJsonConfig1(Class rootClass) {
	//
	// JsonConfig config1 = new JsonConfig();// JSON转换DATE处理（date类型不经过处理报错）
	// config1.registerJsonValueProcessor(Date.class,
	// new DateJsonValueProcessor("yyyy-MM-dd"));
	// config1.setJsonPropertyFilter(new PropertyFilter() {
	// @Override
	// public boolean apply(Object source, String name, Object value) {
	// return value == null;
	// }
	// });
	// config1.setRootClass(rootClass);
	// return config1;
	// }

	@SuppressWarnings("all")
	public static List convertJsonArray(JSONArray array, String rootClass) {
		List yspbList = new ArrayList();
		try {
			JsonConfig config1 = new JsonConfig();// JSON转换DATE处理（date类型不经过处理报错）
			config1.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
			config1.setJsonPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					return value == null;
				}
			});
			config1.setRootClass(Class.forName(rootClass));
			JSONUtils.getMorpherRegistry().registerMorpher(
					new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss",
							"yyyy-MM-dd HH:mm:ss" }));
			for (int i = 0; i < array.size(); i++) {
				yspbList.add(JSONObject.toBean(array.getJSONObject(i), config1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return yspbList;
	}
	public static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
			X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}

				public void verify(String arg0, SSLSocket arg1)
						throws IOException {
				}

				public void verify(String arg0, String[] arg1, String[] arg2)
						throws SSLException {
				}

				public void verify(String arg0, X509Certificate arg1)
						throws SSLException {
				}
			};
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext, hostnameVerifier);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}
	@SuppressWarnings("all")
	public static Object convertJsonObject(JSONObject object, String rootClass) {
		Object o = new Object();
		try {
			JsonConfig config1 = new JsonConfig();// JSON转换DATE处理（date类型不经过处理报错）
			config1.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd"));
			config1.setJsonPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					return value == null;
				}
			});
			config1.setRootClass(Class.forName(rootClass));
			JSONUtils.getMorpherRegistry().registerMorpher(
					new DateMorpher(new String[] { "yyyy-MM-dd",
							"yyyy-MM-dd HH:mm:ss" }));
			o = JSONObject.toBean(object, config1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 远程验证openId是否有绑定用户,返回绑定的userId
	 * 
	 * @param openId
	 * @return
	 */
	public static int getUserIdByOpenId(String openId) {
		int userId = 0;
		try {
			String uri = ConfigConstants.getProperty("health.uri");
			String orgId = ConfigConstants.getProperty("hospital.org.id");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("openId", openId));
			params.add(new BasicNameValuePair("orgId", orgId));
			String postData = getHttpPostResult(uri + "/token/openidcheck",
					params);
			if (postData.length() < 5) {
				userId = -1;
			} else {
				JSONObject json = JSONObject.fromObject(postData);
				userId = json.getInt("userId");

			}
		} catch (Exception e) {
			e.printStackTrace();
			userId = -1;
		}
		return userId;
	}

	/**
	 * 远程验证userId是否绑定了本机构的微信,返回绑定的openId
	 * 
	 * @param openId
	 * @return
	 */
	public static String getOpenIdByUserId(int userId) {
		String openId = "";
		try {
			String uri = ConfigConstants.getProperty("health.uri");
			String orgId = ConfigConstants.getProperty("hospital.org.id");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", userId + ""));
			params.add(new BasicNameValuePair("orgId", orgId));
			String postData = getHttpPostResult(uri + "/token/openidget",
					params);
			if (postData.length() < 5) {
				openId = "";
			} else {
				JSONObject json = JSONObject.fromObject(postData);
				openId = json.getString("openId");

			}
		} catch (Exception e) {
			e.printStackTrace();
			openId = "";
		}
		return openId;
	}

	/**
	 * 向御健康添加一条推送记录 暂时先不管返回成功失败,纯往御健康发送
	 * 
	 * @return
	 */
	public static void addAppPush(String msgid, String title, Integer delay,
			Integer userId, Integer type) {
		String uri = ConfigConstants.getProperty("health.uri");
		String orgId = ConfigConstants.getProperty("hospital.org.id");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 1:检验单 2:体检报告 3:每日清单 4:提醒...
		// 先推送APP推送消息,御健康统一管理推送,这边只需要往益健康添加即可
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("title", title));
		params.add(new BasicNameValuePair("orgId", orgId + ""));
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("content", "您有一条新消息<" + title
				+ ">.请查收"));
		params.add(new BasicNameValuePair("userId", userId + ""));
		params.add(new BasicNameValuePair("delay", delay + ""));
		params.add(new BasicNameValuePair("remark", msgid));
		PubUtil.getHttpPostResult(uri + "/pub/addmsg", params);
	}

	/**
	 * 根据日期获得星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		// String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}
	
	/**
	 * 根据日期获得星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate2(Date date) {
		String[] weekDaysName = { "周日", "周一", "周二", "周三", "周四", "周五",
				"周六" };
		// String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	public static String FloatToString(Float f,int size) {
		String format=".0";
		for(int  i=0;i<size;i++){
			format=format+"0";
		}
		DecimalFormat decimalFormat=new DecimalFormat(format);//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String p=decimalFormat.format(f);//format 返回的是字符串
		return p;
	}
}
