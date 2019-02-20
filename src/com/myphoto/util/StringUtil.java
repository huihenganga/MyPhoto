package com.myphoto.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * Copyright(C)
 * 
 * Module:
 * 
 * @author
 * @version
 * @see
 * @description: 字符串工具类
 * @log:
 */
@SuppressWarnings("all")
public class StringUtil {
	private static Log log = LogFactory.getLog(StringUtil.class);

	public static final boolean isBlank(String str) {
		if (str == null)
			return true;
		str = str.trim();
		if ("".equals(str)) {
			return true;
		}
		return false;
	}

	public static final boolean isNumber(String s) {
		if (s != null && !"".equals(s.trim())) {
			return s.matches("^[0-9]*$");
		} else {
			return false;
		}
	}

	public static final String escapeSQLParam(String param) {
		int stringLength = param.length();
		StringBuffer buf = new StringBuffer((int) (stringLength * 1.1));
		for (int i = 0; i < stringLength; ++i) {
			char c = param.charAt(i);

			switch (c) {
			case 0: /* Must be escaped for 'mysql' */
				buf.append('\\');
				buf.append('0');

				break;

			case '\n': /* Must be escaped for logs */
				buf.append('\\');
				buf.append('n');

				break;

			case '\r':
				buf.append('\\');
				buf.append('r');

				break;

			case '\\':
				buf.append('\\');
				buf.append('\\');

				break;

			case '\'':
				buf.append('\\');
				buf.append('\'');

				break;

			case '"': /* Better safe than sorry */
				buf.append('\\');
				buf.append('"');

				break;

			case '\032': /* This gives problems on Win32 */
				buf.append('\\');
				buf.append('Z');

				break;

			default:
				buf.append(c);
			}
		}
		return buf.toString();
	}

	/**
	 * 转换所有html代码
	 * 
	 * @param str
	 * @return
	 */
	public static final String escapeHTMLTags(String str) {
		if (str == null) {
			return null;
		}
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}

	/**
	 * 去掉所有html标签
	 * 
	 * @param str
	 * @return
	 */
	public static final String clearHTMLTags(String str) {
		if (str == null) {
			return null;
		}
		str = str.replaceAll("<.*?>", "");
		return str;
	}

	/**
	 * 清除脚本
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public static String clearScript(String str) {
		if (str == null) {
			return null;
		}
		// 动网论坛图片事件代码
		String pstr;
		pstr = "onclick=\"javascript:window.open\\(this.src\\);\" ";
		pstr += "style=\"CURSOR: pointer\" onmousewheel=\"return bbimg\\(this\\)\" ";
		pstr += "onload=\"javascript:if\\(this.width>screen.width-500\\)";
		pstr += "this.style.width=screen.width\\-500;\"";
		str = str.replaceAll(pstr, "border=\"0\"");
		// 脚本块
		pstr = "<script.*?</script.*?>";
		str = replaceScript(str, pstr, "");
		// 标签里的事件
		// pstr =
		// "(<[\\w]*?\\s.*?)(onload|onclick|onchange|onerror|onmouse)=[\"]?.{2,}?[\"]? (.*?>)";
		// str = replaceScript(str, pstr, "$1$3");

		// pstr =
		// "<img.[^>]*?(script|&#[x[0-9]]|window\\.|showModalDialog).*?>";
		// str = replaceScript(str, pstr, "");

		pstr = "<iframe.*?>";
		str = replaceScript(str, pstr, "");
		return str;
	}

	public static String replaceScript(String content, String regex,
			String replacement) {
		int flags = Pattern.DOTALL | Pattern.CASE_INSENSITIVE;
		Pattern p = Pattern.compile(regex, flags);
		return p.matcher(content).replaceAll(replacement);
	}

	/**
	 * 把内容中的关键字加strong
	 * 
	 * @param content
	 * @param keywoprd
	 * @return
	 */
	public static String doRedKeyword(String content, String[] keyword) {
		if (StringUtils.isEmpty(content)) {
			return "";
		}
		if (keyword == null)
			return content;
		// int pos = -1;
		for (int i = 0; i < keyword.length; i++) {
			content = content.replace(keyword[i], "<strong>" + keyword[i]
					+ "</strong>");
		}

		return content;
	}

	/**
	 * 转换正则表达式特殊字符
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public static String escapePattern(String content) {
		// TODO 这个不要使用正则表达式,或者要测试一下正则表达式的性能
		if (content == null) {
			return null;
		}
		content = content.replaceAll("\\\\", "\\\\\\\\");
		content = content.replaceAll("\\?", "\\\\?");
		content = content.replaceAll("\\+", "\\\\+");
		content = content.replaceAll("\\*", "\\\\*");
		content = content.replaceAll("\\[", "\\\\[");
		content = content.replaceAll("\\]", "\\\\]");
		content = content.replaceAll("\\{", "\\\\{");
		content = content.replaceAll("\\}", "\\\\}");
		content = content.replaceAll("\\(", "\\\\(");
		content = content.replaceAll("\\)", "\\\\)");
		content = content.replaceAll("\\-", "\\\\-");
		content = content.replaceAll("\\$", "\\\\\\$");
		return content;
	}

	/**
	 * 固定位数
	 * 
	 * @param number
	 *            int
	 * @param minlen
	 *            int
	 * @return String
	 */
	public static String fixed(int number, int minlen) {
		String result = String.valueOf(number);
		while (result.length() < minlen) {
			result = "0" + result;
		}
		return result;
	}

	public static String formatProperty(String title) {
		if (title == null) {
			return null;
		}
		title = title.replaceAll("\"", "%22").replaceAll("'", "%27");
		return title;
	}

	public static String getTitle(String title, int num) {
		if (title == null) {
			return "";
		}
		title = StringUtil.subString(title, num);

		if (title == null) {
			return null;
		}
		title = title.replaceAll("<", "&lt;");
		title = title.replaceAll(">", "&gt;");
		return title;
	}

	/**
	 * 过滤特殊字符，返回<code>String</code>类型.
	 * <p>
	 * 
	 * 遇到特殊字符就中断截取,值只能是[a-zA-Z0-9].
	 * 
	 * @param string
	 * @return 没有该参数则返回空字符串，不返回null.
	 */
	public String getSimpleString(String string) {
		if (string == null) {
			return "";
		}
		String regex = "[a-zA-Z0-9]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		if (m.find()) {
			return m.group();
		}
		return "";
	}

	public static String subString(String str, int len) {
		if (str == null) {
			return "";
		}
		// len = len * 2;
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c < 255) {
				counter++;
			} else {
				counter = counter + 2;
			}
			if (counter > len) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	private static String subString(String str, int start, int len) {
		if (str == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		int counter = 0;
		for (int i = start; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c < 255) {
				counter++;
			} else {
				counter = counter + 2;
			}
			if (counter > len) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	// 转成 '1','2','3'
	public static String toIn(String strs[]) {
		try {
			String strout = "";
			for (int i = 0; i < strs.length; i++) {
				if (strs[i] != null && strs[i].length() > 0) {
					strout += "'" + strs[i] + "',";
				}
			}
			if (strout.length() > 0) {
				strout = strout.substring(0, strout.length() - 1);
			}
			return strout;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将字符串分解成字符串数组.
	 * <p>
	 * 
	 * @param str
	 * @return NONE
	 */
	public static String[] split(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		int SIZE = 125;
		int pages = (str.length() + SIZE - 1) / SIZE;
		String[] contents = new String[pages];
		int start = 0;
		for (int i = 0; i < pages; i++) {
			if (i == (pages - 1)) {
				contents[i] = str.substring(start);
			} else {
				contents[i] = str.substring(start, start + SIZE);
			}
			start += SIZE;
		}
		return contents;
	}

	/**
	 * 分段
	 * 
	 * @param content
	 * @param coptem
	 * @return
	 */
	public static String[] patchMa(String content, String coptem) {
		StringBuffer contentBatch = new StringBuffer();
		Pattern pa = Pattern.compile(coptem);
		Matcher ma = pa.matcher(content);
		int pos = 0;
		int ii = 0;

		while (ma.find(pos)) {
			contentBatch.append(ma.group(1));
			contentBatch.append("#!#");
			ii = ii + 1;
			pos = ma.end();
		}
		return contentBatch.toString().split("#!#");
	}

	/**
	 * Used by the hash method.
	 */
	private static Map digests = Collections.synchronizedMap(new HashMap());

	/**
	 * Hashes a String using the Md5 algorithm and returns the result as a
	 * String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes a
	 * bottleneck in your code, you may wish to maintain a pool of MessageDigest
	 * objects instead of using this method.
	 * <p/>
	 * A hash is a one-way function -- that is, given an input, an output is
	 * easily computed. However, given the output, the input is almost
	 * impossible to compute. This is useful for passwords since we can store
	 * the hash and a hacker will then have a very hard time determining the
	 * original password.
	 * <p/>
	 * In Jive, every time a user logs in, we simply take their plain text
	 * password, compute the hash, and compare the generated hash to the stored
	 * hash. Since it is almost impossible that two passwords will generate the
	 * same hash, we know if the user gave us the correct password or not. The
	 * only negative to this system is that password recovery is basically
	 * impossible. Therefore, a reset password method is used instead.
	 * 
	 * @param data
	 *            the String to compute the hash of.
	 * @return a hashed version of the passed-in String
	 */
	public static String hash(String data) {
		return hash(data, "MD5");
	}

	/**
	 * Hashes a String using the specified algorithm and returns the result as a
	 * String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes a
	 * bottleneck in your code, you may wish to maintain a pool of MessageDigest
	 * objects instead of using this method.
	 * <p/>
	 * A hash is a one-way function -- that is, given an input, an output is
	 * easily computed. However, given the output, the input is almost
	 * impossible to compute. This is useful for passwords since we can store
	 * the hash and a hacker will then have a very hard time determining the
	 * original password.
	 * <p/>
	 * In Jive, every time a user logs in, we simply take their plain text
	 * password, compute the hash, and compare the generated hash to the stored
	 * hash. Since it is almost impossible that two passwords will generate the
	 * same hash, we know if the user gave us the correct password or not. The
	 * only negative to this system is that password recovery is basically
	 * impossible. Therefore, a reset password method is used instead.
	 * 
	 * @param data
	 *            the String to compute the hash of.
	 * @param algorithm
	 *            the name of the algorithm requested.
	 * @return a hashed version of the passed-in String
	 */
	public static String hash(String data, String algorithm) {
		try {
			return hash(data.getBytes("utf-8"), algorithm);
		} catch (java.io.UnsupportedEncodingException e) {
			System.err.println(e);
		}
		return data;
	}

	/**
	 * Hashes a byte array using the specified algorithm and returns the result
	 * as a String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes a
	 * bottleneck in your code, you may wish to maintain a pool of MessageDigest
	 * objects instead of using this method.
	 * <p/>
	 * A hash is a one-way function -- that is, given an input, an output is
	 * easily computed. However, given the output, the input is almost
	 * impossible to compute. This is useful for passwords since we can store
	 * the hash and a hacker will then have a very hard time determining the
	 * original password.
	 * <p/>
	 * In Jive, every time a user logs in, we simply take their plain text
	 * password, compute the hash, and compare the generated hash to the stored
	 * hash. Since it is almost impossible that two passwords will generate the
	 * same hash, we know if the user gave us the correct password or not. The
	 * only negative to this system is that password recovery is basically
	 * impossible. Therefore, a reset password method is used instead.
	 * 
	 * @param bytes
	 *            the byte array to compute the hash of.
	 * @param algorithm
	 *            the name of the algorithm requested.
	 * @return a hashed version of the passed-in String
	 */
	public static String hash(byte[] bytes, String algorithm) {
		synchronized (algorithm.intern()) {
			MessageDigest digest = (MessageDigest) digests.get(algorithm);
			if (digest == null) {
				try {
					digest = MessageDigest.getInstance(algorithm);
					digests.put(algorithm, digest);
				} catch (NoSuchAlgorithmException nsae) {
					System.err.println("Failed to load the " + algorithm
							+ " MessageDigest. "
							+ "Jive will be unable to function normally."
							+ nsae);
					return null;
				}
			}
			// Now, compute hash.
			digest.update(bytes);
			return encodeHex(digest.digest());
		}
	}

	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p/>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 * 
	 * @param bytes
	 *            an array of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	public static String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;

		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * 
	 * @description: 对时间空值的格式的验证
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String dateNullValidate(Date strValue) {
		String dateStr;
		if (strValue != null) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			dateStr = format.format(strValue).toString();
		} else {
			dateStr = "";
		}
		return dateStr;
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 根据url获取网站域名，格式为http[s]://demainName/
	 * 
	 * @description: <方法描述>
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String parseHost(String url) {
		if (url == null) {
			return "";
		}
		String[] splits = url.split("/");
		if (url.length() > 2) {
			return splits[0] + "//" + splits[2] + "/";
		}
		return "";
	}

	/**
	 * 将JSON字符串处理成javascript可以识别的
	 * 
	 * @param ors
	 * @return
	 */
	public static String dealString4JSON(String ors) {
		ors = ors == null ? "" : ors;
		StringBuffer buffer = new StringBuffer(ors);
		// 在替换的时候不要使用 String.replaceAll("\\","\\\\"); 这样不会达到替换的效果
		// 因为这些符号有特殊的意义,在正则表达式里要用到
		int i = 0;
		while (i < buffer.length()) {
			if (buffer.charAt(i) == '\'' || buffer.charAt(i) == '\\') {
				buffer.insert(i, '\\');
				i += 2;
			} else {
				i++;
			}
		}
		return buffer.toString().replaceAll("\r", "").replaceAll("\n", "");
	}

	public static void main(String[] args) {

		// String str = "你好123哈哈哈hello";
		// System.out.println(str.length() + " " + str.getBytes().length);
		// System.out.println(StringUtil.hash("wani3344"));

		// System.out.println();
		// System.out.println(hash("11111111"));
		Date date = new Date();
		System.out.println(formatDate(date, "abc"));
	}

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sf = null;
		try {
			sf = new SimpleDateFormat("yyyyMMddHHmmss");
			if (pattern != null && !pattern.equals("")) {
				sf = new SimpleDateFormat(pattern);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			sf = new SimpleDateFormat("yyyyMMddHHmmss");
		}
		return sf.format(date);
	}

	/**
	 * @description: 把数字格式化成2位小数，不足补0
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String moneyFormat(Number number) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(number);
	}

	/***
	 * 
	 * @description: 获得当前时间
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date).toString();
	}

	/***
	 * 
	 * @description: 获得当前时间的年份
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getYear() {
		String date = getDate();
		return date.substring(0, 4);
	}

	/***
	 * 
	 * @description: 获得当前时间的月份
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getMonth() {
		String date = getDate();
		return date.substring(5, 7);
	}

	/***
	 * 
	 * @description: 获得当前时间的天
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getDay() {
		String date = getDate();
		return date.substring(8, 10);
	}

	/***
	 * 
	 * @description: 獲得當前時間的年和月(格式：年-月)
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getYearAndMonth() {
		String date = getDate();
		return date.substring(0, 7);
	}

	/***
	 * 
	 * @description: 獲得當前時間(格式：年-月-日)
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getToday() {
		String date = getDate();
		return date.substring(0, 10);
	}

	/***
	 * 
	 * @description: 獲得當前時間(格式：時:分:秒)
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date).toString();
	}

	/***
	 * 
	 * @description: 獲得时间差(毫秒)
	 * @param:begin 开始时间 (格式：年-月-日 時:分:秒)
	 * @param end
	 *            结束时间 (格式：年-月-日 時:分:秒)
	 * @return:
	 * @throws:
	 */
	public static long timeAfter(String begin, String end) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = df.parse(begin);
			Date d2 = df.parse(end);
			return d1.getTime() - d2.getTime();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return 0;
	}

	/***
	 * 
	 * @description: 獲得N天前的当前時間(格式：年-月-日 時:分:秒)
	 * @param:days N天前
	 * @return:
	 * @throws:
	 */
	public static String preDay(int days) {
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DATE, days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String daysBefore = sdf.format(cal1.getTime());
		return daysBefore;
	}

	/**
	 * 取得上个月
	 * 
	 * @return 如200912
	 */
	public static String getPreviousMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1); // 取得上个月
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 
	 * @description: <取得当前时间的前一天>
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getPreviousDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 取得半个小时前的时间
	 * 
	 * @return 如2009-12-12 12:12:12
	 */
	public static String getHalfHourBefore() {
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Timestamp(
				calendar.getTimeInMillis() - 30 * 60 * 1000));
	}

	/**
	 * 得到指定时间的后一天
	 * 
	 * @description: <方法描述>
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getPreviousDay(String dateStr) {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// java.util.Date date =
			// DateFormat.getDateInstance().parse(dateStr);
			java.util.Date date = sdf.parse(dateStr);
			now.setTime(date);
			now.set(Calendar.DATE, now.get(Calendar.DATE) + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(now.getTime());
	}

	/**
	 * 得到指定时间的后两天
	 * 
	 * @description: <方法描述>
	 * @param:
	 * @return:
	 * @throws:
	 */
	public static String getAfterDay(String dateStr) {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// java.util.Date date =
			// DateFormat.getDateInstance().parse(dateStr);
			java.util.Date date = sdf.parse(dateStr);
			now.setTime(date);
			now.set(Calendar.DATE, now.get(Calendar.DATE) + 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(now.getTime());
	}

	/**
	 * 将指定的时间字符串转换为特点的时间格式字符串
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String formatDate(String dateStr, String format) {
		String dateString = "";
		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			Date date = StrToDate(dateStr, format);
			dateString = df.format(date);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return dateString;
	}

	/**
	 * 字符串转换成指定格式的日期
	 * 
	 * @param str
	 * @return date Tue Jan 25 21:22:00 CST 2011 格式
	 */
	public static Date StrToDate(String str, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 把字符串转换成utf编码
	 * 
	 * @param inPara
	 * @return
	 */
	public static String toUTF(String inPara) {
		char temChr;
		int ascChr;
		int i;
		String rtStr = new String("");
		if (inPara == null) {
			inPara = "";
		}
		for (i = 0; i < inPara.length(); i++) {
			temChr = inPara.charAt(i);
			ascChr = temChr + 0;
			// System.out.println(ascChr);
			// System.out.println(Integer.toBinaryString(ascChr));
			rtStr = rtStr + "&#x" + Integer.toHexString(ascChr) + ";";
		}
		return rtStr;
	}

	/**
	 * 根据提交的参数生产url参数穿
	 * 
	 * @param request
	 * @param excludeKey
	 * @return
	 */
	public static String getQueryString(HttpServletRequest request,
			String... excludeKey) {
		Map<String, String[]> param = request.getParameterMap();
		StringBuffer sb = new StringBuffer();
		String[] value;
		int i;
		boolean hasExclude = excludeKey != null && excludeKey.length > 0;
		String amp = "&amp;";
		try {
			outer: for (String key : param.keySet()) {
				if (hasExclude) {
					for (int j = 0; j < excludeKey.length; j++) {
						if (excludeKey[j].equals(key)) {
							continue outer;
						}
					}
				}
				value = param.get(key);
				if (param != null) {
					for (i = 0; i < value.length; i++) {
						sb.append(amp);
						sb.append(key);
						sb.append('=');
						sb.append(URLEncoder.encode(value[i], "UTF-8"));
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.length() > 0 ? sb.substring(5) : "";
	}

	/**
	 * @description 解释字符数字等简单类型的数字
	 * @param clazz
	 * @param req
	 * @param key
	 * @param <T>
	 * @return
	 */
	public static <T> T parseRequest(Class<T> clazz, HttpServletRequest req,
			String key) {
		String value = req.getParameter(key);
		if (value == null || value.length() == 0) {
			return null;
		}
		return parseValue(clazz, value);
	}

	/**
	 * @description:根据字符串生成对应的类型值
	 * @param: clazz 类型
	 * @param: value 字符串值
	 * @param <T>
	 * @return
	 */
	private static <T> T parseValue(Class<T> clazz, String value) {
		try {
			Constructor ctor = clazz.getConstructor(String.class);
			return (T) ctor.newInstance(value);
		} catch (NoSuchMethodException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} catch (InvocationTargetException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} catch (InstantiationException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return null;
	}

	/**
	 * @description: 在request中获取相应类型的值
	 * @param: req
	 * @param: key
	 * @param: defaultValue
	 * @param: <T>
	 * @return
	 */
	public static <T> T parseRequest(HttpServletRequest req, String key,
			T defaultValue) {
		String value = req.getParameter(key);
		if (value == null || value.length() == 0) {
			return defaultValue;
		} else {
			return parseValue((Class<T>) defaultValue.getClass(), value);
		}
	}

	/**
	 * 替换源字符串中指定下标的字符为新的字符
	 * 
	 * @param src
	 * @param flag
	 * @param indexs
	 * @return
	 */
	public static String hideCharactor(String src, String flag, int[] indexs) {
		if (StringUtils.isEmpty(src) || StringUtils.isEmpty(flag)) {
			return src;
		}
		Arrays.sort(indexs);
		StringBuilder b = null;
		for (int i : indexs) {
			if (i <= src.length() && i > 0) {
				b = new StringBuilder();
				b.append(src.substring(0, i - 1)).append(flag)
						.append(src.substring(i));
				src = b.toString();
			}
		}
		return src;
	}

	/**
	 * 屏蔽证件号后4位
	 * 
	 * @param indentify
	 * @return
	 */
	public static String hideIndentify(String indentify) {
		int[] a = new int[4];
		for (int i = 0; i < 4; i++) {
			a[i] = indentify.length() - i;
		}
		return hideCharactor(indentify, "*", a);
	}

	//
	// /**
	// * 读取配置文件property.properties中指定类型指定key的值
	// * 如配置文件中订单支付方式
	// order.payBSS.type=1:现金|2:现金支票|3:转账支票|4:信用卡|5:缴费卡|6:托收|7:代收|8:银行代扣
	// * @description: <方法描述>
	// * 读取第6个值：托收
	// * 调用方式 getMapValueByKey("order.payBSS.type","6"),返回托收
	// * @param:mapName 要读取的配置文件名称
	// * @param:key 要读取的指定配置文件名称中的值
	// * @return:
	// * @throws:
	// */
	// public static String getMapValueByKey(String mapName,String key){
	// String str ="";
	// try {
	// Map<String, String> mapValue = Property.getPropertyMap(mapName);
	// for(Object obj : mapValue.keySet()){
	// str = mapValue.get(key).toString();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return str;
	// }

	/**
	 * 
	 * @Title: includeString
	 * @Description: 判断orignStr中是否含有key
	 * @param @param orignStr
	 * @param @param key
	 * @param @return
	 * @return Boolean
	 * @author baizongwei
	 * @throws
	 */
	public static Boolean includeString(String orignStr, String key) {
		Boolean flag = false;
		if (orignStr.indexOf(key) != -1) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 去除字符串内的非数字字符，如“123FG34”，得到“12334”
	 * 
	 * @param str
	 * @return
	 */
	public static String filterUnNumber(String str) {
		// 只允数字
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		// 替换与模式匹配的所有字符（即非数字的字符将被""替换）
		return m.replaceAll("").trim();
	}

	/**
	 * @description: 生成指定位数的随机数字
	 * @param
	 * @return:
	 */
	public static String RandomNumber(int count) {
		Random r = new Random();
		String s = "";
		for (int i = 0; i < count; i++) {
			String rand = String.valueOf(r.nextInt(10));
			s += rand;
		}
		return s;
	}

	/**
	 * 对字符串进行去除前后空格处理
	 * 
	 * @param str
	 * @return
	 */
	public static String trimStr(String str) {
		if (str == null) {
			return str;
		} else {
			return str.trim();
		}
	}

	/**
	 * 匹配联通手机号
	 */
	private static String PATTERN_CUTMOBILENUM = "^1(3[0-2]|45|5[56]|8[56])[0-9]{8}$";
	private static Pattern pattern = Pattern.compile(PATTERN_CUTMOBILENUM);

	/**
	 * 判断是否联通的号码
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isLtMobile(String mobile) {
		boolean flag = false;
		if (mobile != null && !"".equals(mobile)) {
			Matcher matcher = pattern.matcher(mobile);
			flag = matcher.matches();
		}
		return flag;
	}

	public static String string2Json(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {

			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
