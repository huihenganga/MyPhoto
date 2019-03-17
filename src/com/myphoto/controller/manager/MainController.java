package com.myphoto.controller.manager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myphoto.entity.PriMenu;
import com.myphoto.entity.PriRole;
import com.myphoto.entity.PriUser;
import com.myphoto.service.PriUserService;
import com.myphoto.util.LoginUser;
import com.myphoto.util.MD5;
import com.myphoto.util.SessionContainer;
import com.tencent.cloud.CosStsClient;


@Controller
@RequestMapping("/admin")
public class MainController extends BaseManageController {
	private static Logger log = LogManager.getLogger(MainController.class);
	@Autowired
	private PriUserService priUserService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "admin/index";
	}

	@RequestMapping("/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(111);
		return "admin/test";
	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request,
			HttpServletResponse response, String userName, String password,
			Integer roleId) {
		PriUser priUser = priUserService.getPriUserByUserName(userName);
		if (priUser != null) {
			if (MD5.getInstance().getMD5(password).equals(priUser.getPassword())) {
				/*if (roleId == null || roleId == 0) {
					roleId = priUser.getPriRole().getId();
				}*/
				// 设置登录session信息
				PriRole priRole = priUserService.getPriRoleById(roleId);
				LoginUser loginUser = new LoginUser();
				loginUser.setUserId(priUser.getId());
				loginUser.setName(priUser.getName());
				loginUser.setRoleId(priRole.getId());
				loginUser.setRoleName(priRole.getName());
				loginUser.setLoginIP(request.getRemoteAddr());
				SessionContainer container = new SessionContainer();
				container.setLoginUser(loginUser);
				request.getSession().setAttribute("CONTAINER_SESSION",
						container);
				// 把登陆用户名/角色存入cookie
				/*try {
					String path = "/";
					Cookie cookie = new Cookie("userId", priUser.getId() + "");
					cookie.setPath(path);
					cookie.setMaxAge(-1);// 有效期为一个月
					response.addCookie(cookie);
					cookie = new Cookie("roleId", priUser.getPriRole().getId() + "");
					cookie.setPath(path);
					cookie.setMaxAge(-1);// 有效期为一个月
					response.addCookie(cookie);
				} catch (Exception ex) {
					log.error(ex);
				}*/
				// TODO 登录日志
				return "redirect:main";
			} else {
				request.setAttribute("s", "5");// 登陆密码不正确
				return "admin/index";
			}

		} else {
			request.setAttribute("s", "4");// 无此用户名
			return "admin/index";
		}

	}

	@RequestMapping("/main")
	public String main(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer mainMenu = new StringBuffer();
		List<Integer> subMenuIds = new ArrayList<Integer>();
		List<PriMenu> list = priUserService.getUserMenu(getSessionContainer(
				request, response).getLoginUser().getRoleId());
		request.setAttribute("name", getSessionContainer(request, response)
				.getLoginUser().getName());
		request.setAttribute("time",
				new SimpleDateFormat("yyyy年MM月dd日  E ").format(new Date()));
		// 初始化一级菜单
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getMenuLevel() == 1) {
				subMenuIds.add(list.get(i).getId());
			}
		}
		// 一级菜单循环
		for (int i = 0; i < subMenuIds.size(); i++) {
			mainMenu.append("<div title=\"" + list.get(i).getName()
					+ "\" style=\"padding:10px\">");
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getpMenuId().intValue() == subMenuIds.get(i)
						.intValue()) {
					mainMenu.append("<p><a href=\"javascript:void(0)\" onClick=\"addTab('"
							+ list.get(j).getName()
							+ "','"
							+ list.get(j).getUrl()
							+ "')\">"
							+ list.get(j).getName() + "</a></p>");
				}
				// 循环最后
				if (j == (list.size() - 1)) {
					mainMenu.append("</div>");
				}
			}

		}
		request.setAttribute("menuHtml", mainMenu.toString());
		return "admin/main";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		getSessionContainer(request, response).setLoginUser(null);
		try {
			String path = "/";
			Cookie cookie = new Cookie("userId", "");
			cookie.setPath(path);
			cookie.setMaxAge(0);// 有效期为一个月
			response.addCookie(cookie);
			cookie = new Cookie("roleId", "");
			cookie.setPath(path);
			cookie.setMaxAge(0);// 有效期为一个月
			response.addCookie(cookie);
		} catch (Exception ex) {
			log.error(ex);
		}
		return "admin/index";
	}

	char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	@RequestMapping("/imgcode")
	public void imgcode(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 定义图像buffer
		int width = 70;// 定义图片的width
		int height = 22;// 定义图片的height
		int codeCount = 4;// 定义图片上显示验证码的个数
		int xx = 13;
		int fontHeight = 18;
		int codeY = 19;
		response.setHeader("Cache-Control", "no-cache");
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// Graphics2D gd = buffImg.createGraphics();
		// Graphics2D gd = (Graphics2D) buffImg.getGraphics();
		Graphics gd = buffImg.getGraphics();
		// 创建一个随机数生成器类
		Random random = new Random();
		// 将图像填充为白色
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);
		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		// 设置字体。
		gd.setFont(font);
		// 画边框。
		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);
		// 随机产生10条干扰线，使图象中的认证码不易被其它程序探测到。
		gd.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(10);
			int yl = random.nextInt(10);
			gd.drawLine(x, y, x + xl, y + yl);
		}

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String code = String.valueOf(codeSequence[random.nextInt(34)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(200);
			green = random.nextInt(200);
			blue = random.nextInt(200);

			// 用随机产生的颜色将验证码绘制到图像中。
			gd.setColor(new Color(red, green, blue));
			gd.drawString(code, (i + 1) * xx, codeY);

			// 将产生的四个随机数组合在一起。
			randomCode.append(code);
		}
		// 将四位数字的验证码保存到Session中。
		HttpSession session = request.getSession();
		// System.out.print(randomCode);
		session.setAttribute("code", randomCode.toString());

		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		response.setContentType("image/jpeg");

		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.close();
	}
	
	public static void main(String[] args) {
		/*String s = getFriendTime(System.currentTimeMillis()-60 * 60*1000);
		System.out.println(s);*/
		
//		for(int i=0;i<10;i++){
//			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//			System.out.println(uuid);
//		}
		

        TreeMap<String, Object> config = new TreeMap<String, Object>();

        try {
            // 固定密钥
            config.put("SecretId", "AKID2xiI9wAyDk5PYVqrGj4DByZ0GLEzZxAB");
            // 固定密钥
            config.put("SecretKey", "74tW2NWTFpDQTK3bxc9XkpmY9YlmbEm9");

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", "huajia-1255442634");
            // 换成 bucket 所在地区
            config.put("region", "ap-guangzhou");

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的目录，例子：* 或者 a/* 或者 a.jpg
            config.put("allowPrefix", "*");

            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            JSONObject credential = CosStsClient.getCredential(config);
            System.out.println(credential);
        } catch (Exception e) {
            throw new IllegalArgumentException("no valid secret !");
        }
		
	}
}
