package com.myphoto.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myphoto.contants.DescribableEnum;
import com.myphoto.contants.JsonResult;
import com.myphoto.entity.Attention;
import com.myphoto.entity.Product;
import com.myphoto.entity.ProductCount;
import com.myphoto.entity.User;
import com.myphoto.entity.base.PageObject;
import com.myphoto.service.AttentionService;
import com.myphoto.service.ProductService;
import com.myphoto.service.UserService;
import com.myphoto.util.PubUtil;
import com.myphoto.util.StringUtil;

@Controller
@RequestMapping("/ws")
@SuppressWarnings("all")
public class PubUserInfoController extends BaseAppController {
	private static Logger log = LogManager
			.getLogger(PubUserInfoController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private AttentionService attentionService;
	@Autowired
	private ProductService productService;

	/**
	 * 用户注册接口
	 * @author 吴垂久
	 * @date 2019年2月17日
	 * @version 1.0.0
	 * @param request
	 * @param token
	 * @param nickName
	 * @param headImage
	 * @param openId
	 * @return JSONObject
	 */
	@RequestMapping("/userReg")
	@ResponseBody
	public JSONObject userReg(HttpServletRequest request, String openId,
			String nickName, String headImage, String token) {
		JsonResult json;
		User t = userService.getUserByOpenId(openId);
		if (t != null) {
			json = new JsonResult(DescribableEnum.USER_EXIST);
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
		t = new User();
		t.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		t.setOpenId(openId);
		t.setNickName(nickName);
		t.setHeadImage(headImage);
		t.setToken(token);
		userService.saveOrUpdateUser(t);
		json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("id", t.getId());
		json.addData("openId", t.getOpenId());
		json.addData("nickName", t.getNickName());
		json.addData("headImage", t.getHeadImage());
		json.addData("token", t.getToken());
		json.addData("createDate", t.getCreateDate());
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 用户关注、取消关注其他用户接口
	 * @author 吴垂久
	 * @date 2019年2月17日
	 * @version 1.0.0
	 * @param request
	 * @param response
	 * @param beAttentionId
	 * @param type
	 * @param userId
	 * @return JSONObject
	 */
	@RequestMapping("/addOrDelAttention")
	@ResponseBody
	public JSONObject addOrDelAttention(HttpServletRequest request,
			HttpServletResponse response, String beAttentionId, Integer type,
			String userId) {
		User user = userService.getUserById(userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		;
		if (StringUtil.isBlank(beAttentionId)) {
			if (beAttentionId.equals(user.getId())) {
				Attention tem = attentionService.getAttentionByTwoUserId(
						beAttentionId, user.getId());
				if (null != tem) {
					if (type == 0) {
						if (tem.getStatus() != 0) {
							tem.setStatus(0);
							tem.setAttentionTime(new Date());
							attentionService.saveOrUpdateAttention(tem);
						}
					} else {
						if (tem.getStatus() != 1) {
							tem.setStatus(1);
							tem.setAttentionTime(new Date());
							attentionService.saveOrUpdateAttention(tem);
						}
					}
				} else {
					if (type == 1) {
						Attention attention = new Attention();
						attention.setAttentionBy(user.getId());
						attention.setAttentionTo(beAttentionId);
						attention.setAttentionTime(new Date());
						attention.setStatus(1);
						attentionService.saveOrUpdateAttention(attention);
					}
				}
			} else {
				json = new JsonResult(DescribableEnum.FAIL);
				json.setMsg("不能关注自己！");
				return JSONObject.fromObject(json, PubUtil.getJsonConfig());
			}
		}
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 获取用户关注用户列表
	 * @author wcj 2019-2-17下午5:29:20
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param userId
	 * @return JSONObject
	 */
	@RequestMapping("/getAttentionByUserId")
	@ResponseBody
	public JSONObject getAttentionByUserId(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize,
			String userId) {
		PageObject pageObject = attentionService.getAttentionByUserId(
				new PageObject(pageNum, pageSize), userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		if (pageObject.getData() != null) {
			json.addData("list", pageObject.getData());
			json.addData("total", pageObject.getDataCount());
			json.addData("nowPage", pageNum);
			json.addData("nowList", pageObject.getData().size());
			json.addData("totalPage",
					(pageObject.getDataCount() + pageSize - 1) / pageSize);
		} else {
			json.addData("list", new ArrayList());
			json.addData("total", 0);
			json.addData("nowPage", 1);
			json.addData("nowList", 0);
			json.addData("totalPage", 0);
		}
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 获取用户的粉丝用户列表
	 * @author wcj 2019-2-17下午5:29:44
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param userId
	 * @return JSONObject
	 */
	@RequestMapping("/getFansByUserId")
	@ResponseBody
	public JSONObject getFansByUserId(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize,
			String userId) {
		PageObject pageObject = attentionService.getFansByUserId(
				new PageObject(pageNum, pageSize), userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		if (pageObject.getData() != null) {
			json.addData("list", pageObject.getData());
			json.addData("total", pageObject.getDataCount());
			json.addData("nowPage", pageNum);
			json.addData("nowList", pageObject.getData().size());
			json.addData("totalPage",
					(pageObject.getDataCount() + pageSize - 1) / pageSize);
		} else {
			json.addData("list", new ArrayList());
			json.addData("total", 0);
			json.addData("nowPage", 1);
			json.addData("nowList", 0);
			json.addData("totalPage", 0);
		}
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 获取用户信息接口
	 * @author wcj 2019-2-19下午11:37:53
	 * @param request
	 * @param response
	 * @param userId
	 * @return JSONObject
	 */
	@RequestMapping("/getUserInfo")
	@ResponseBody
	public JSONObject getUserInfo(HttpServletRequest request,
			HttpServletResponse response, String userId) {
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		User user = userService.getUserById(userId);
		Integer attentionCount = attentionService
				.getAttentionCountByUserId(userId);
		Integer fansCount = attentionService.getFansCountByUserId(userId);
		json.addData("id", userId);
		json.addData("createDate", user.getCreateDate().getTime());
		json.addData("headImage", user.getHeadImage());
		json.addData("nickName", user.getNickName());
		json.addData("token", user.getToken());
		json.addData("attentionCount", attentionCount);
		json.addData("fansCount", fansCount);
		List<Product> productList = productService.getProductByUserId(userId);
		if (productList.size() > 0) {
			json.addData("productionCount", productList.size());
		} else {
			json.addData("productionCount", 0);
		}
		Integer collectCount = 0;
		Integer pageView = 0;
		if (productList.size() > 0) {
			Integer productId = 0;
			ProductCount productCount = null;
			for (int i = 0; i < productList.size(); i++) {
				productId = productList.get(i).getId();
				productCount = productService
						.getProductCountByProductId(productId);
				if (productCount!=null) {
					collectCount +=productCount.getCollectCount();
					pageView +=productCount.getPageView();
				}
			}
		}
		json.addData("collectCount", 0);
		json.addData("pageView", 0);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 发布作品接口
	 * @author wcj 2019-2-19下午10:59:34
	 * @param request
	 * @param response
	 * @param title
	 * @param coveUrl
	 * @param description
	 * @param userId
	 * @return JSONObject
	 */
	@RequestMapping("/publishProduct")
	@ResponseBody
	public JSONObject publishProduct(HttpServletRequest request,
			HttpServletResponse response, String title, String coveUrl,
			String description,String message,Integer displayId, String userId) {
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		User user = userService.getUserById(userId);
		Product product = new Product();
		product.setTitle(title);
		product.setCoveUrl(coveUrl);
		product.setShareId(user.getId());
		product.setPublishTime(new Date());
		product.setDescription(description);
		product.setMessage(message);
		product.setDisplayId(displayId);
		productService.saveOrUpdateProduct(product);
		json.addData("productId", product.getId());
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
}