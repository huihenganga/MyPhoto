package com.myphoto.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.myphoto.contants.AppContents;
import com.myphoto.contants.DescribableEnum;
import com.myphoto.contants.JsonResult;
import com.myphoto.entity.ArtShow;
import com.myphoto.entity.ArtShowCount;
import com.myphoto.entity.Attention;
import com.myphoto.entity.Collection;
import com.myphoto.entity.Product;
import com.myphoto.entity.ProductArtShow;
import com.myphoto.entity.ProductCount;
import com.myphoto.entity.RUserArtShow;
import com.myphoto.entity.User;
import com.myphoto.entity.base.PageObject;
import com.myphoto.service.AttentionService;
import com.myphoto.service.ProductService;
import com.myphoto.service.UserService;
import com.myphoto.util.HttpClientHelper;
import com.myphoto.util.MD5;
import com.myphoto.util.PubUtil;
import com.myphoto.util.Signature;
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
	 * 
	 * @author 吴垂久
	 * @date 2019年2月17日
	 * @version 1.0.0
	 * @param request
	 * @param nickName
	 * @param headImage
	 * @param openId
	 * @return JSONObject
	 */
	@RequestMapping("/userReg")
	@ResponseBody
	public JSONObject userReg(HttpServletRequest request,
			String nickName, String headImage, String jsCode) {
		JsonResult json;
		
		String appId= AppContents.APPID;
		String secret= AppContents.SECRET;
		String jscode2Code=AppContents.JSCODE2CODE;		 
	    String url = new StringBuilder().append(jscode2Code)
	            .append("?appid="+ appId)
	            .append("&secret="+secret)
	            .append("&js_code="+jsCode)
	            .append("&grant_type=authorization_code")
	            .toString();
	 
	    String result = null;
		try {
			result = HttpClientHelper.doGet(url,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if(result == null ) {//请求失败
	        //TODO throw new UnExceptedException("获取会话失败");
	    	json = new JsonResult(DescribableEnum.FAIL);
			json.setMsg("系统异常，请稍后再试！");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	    }
	    com.alibaba.fastjson.JSONObject jsonObj = JSON.parseObject(result);
	    
	    System.out.println(jsonObj.toString());
	    
	    String openId = jsonObj.getString("openid");
	    
		System.out.println(openId);
		
	    String sessionKey = jsonObj.getString("session_key");
	    
		System.out.println(sessionKey);
		
		if(openId == null ) {//请求失败
			//TODO throw new UnExceptedException("获取会话失败");
			json = new JsonResult(DescribableEnum.FAIL);
			json.setMsg("系统异常，请稍后再试！");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
		User t = userService.getUserByOpenId(openId);
		String id = MD5.getInstance().getMD5(openId
				+ "wondball");
		if (null==t) {
			t = new User();
			t.setId(id);
			t.setOpenId(openId);
			t.setSessionKey(sessionKey);
			t.setNickName(nickName);
			t.setHeadImage(headImage);
			userService.saveOrUpdateUser(t);
		}
		json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("id", t.getId());
		json.addData("createDate", t.getCreateDate());
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 用户关注、取消关注其他用户接口
	 * 
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
		if (!StringUtil.isBlank(beAttentionId)) {
			if (!beAttentionId.equals(user.getId())) {
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
	 * 
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
	 * 
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
	 * 
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
				if (productCount != null) {
					collectCount += productCount.getCollectCount();
					pageView += productCount.getPageView();
				}
			}
		}
		json.addData("collectCount", collectCount);
		json.addData("pageView", pageView);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 发布作品接口
	 * 
	 * @author wcj 2019-2-19下午10:59:34
	 * @param request
	 * @param response
	 * @param title
	 * @param coveUrl
	 * @param description
	 * @param userId
	 * @param background
	 * @return JSONObject
	 */
	@RequestMapping("/publishProduct")
	@ResponseBody
	public JSONObject publishProduct(HttpServletRequest request,
			HttpServletResponse response, String title, String coveUrl,
			String description, String message,
			String userId, String background) {
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		User user = userService.getUserById(userId);
		Product product = new Product();
		product.setTitle(title);
		product.setCoveUrl(coveUrl);
		product.setShareId(user.getId());
		product.setPublishTime(new Date());
		product.setDescription(description);
		product.setMessage(message);
		product.setBackground(background);
		productService.saveOrUpdateProduct(product);
		json.addData("productId", product.getId());
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 设置作品信息
	 * 
	 * @author wcj 2019-2-21下午11:00:10
	 * @param request
	 * @param response
	 * @param title
	 * @param coveUrl
	 * @param description
	 * @param message
	 * @param displayId
	 * @param productId
	 * @return JSONObject
	 */
	@RequestMapping("/setProduct")
	@ResponseBody
	public JSONObject setProduct(HttpServletRequest request,
			HttpServletResponse response, String title,
			String description, String message,
			Integer productId) {
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		Product product = productService.getProductByProductId(productId);
		product.setTitle(title);
		product.setDescription(description);
		product.setMessage(message);
		productService.saveOrUpdateProduct(product);
		json.addData("productId", product.getId());
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	/**
	 * 设置画展
	 * @author wcj
	 * 2019-2-27下午10:54:14
	 * @param request
	 * @param response
	 * @param displayId
	 * @param productId
	 * @return
	 * JSONObject
	 */
/*	@RequestMapping("/setArtShow")
	@ResponseBody
	public JSONObject setArtShow(HttpServletRequest request,
			HttpServletResponse response,Integer displayId,
			Integer productId,Integer status) {
		Product product = productService.getProductByProductId(productId);
		ArtShow artShow = productService.getArtShowByArtShowtId(displayId);
		JsonResult json ;
		if(null==product){
			json = new JsonResult("2", "找不到该作品");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
		if(null==artShow){
			json = new JsonResult("2", "找不到该画展");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
		ProductArtShow productArtShow = productService.getProductArtShowByTwoId(productId,displayId);
		if(0==status&&null!=productArtShow){
			if(((System.currentTimeMillis()  - productArtShow.getrTime().getTime())<604800000)){
				json = new JsonResult("2", "参展7天后才能取消");
				return JSONObject.fromObject(json, PubUtil.getJsonConfig());
			}else{
				json = new JsonResult(DescribableEnum.SUCCESS);
				productService.saveOrUpdateProductArtShow(productArtShow);
				return JSONObject.fromObject(json, PubUtil.getJsonConfig());
			}
		}else if(1==status&&null==productArtShow){
			productArtShow = new ProductArtShow();			
			productArtShow.setProductId(productId);
			productArtShow.setDisplayId(displayId);
			productArtShow.setrTime(new Date());
			json = new JsonResult(DescribableEnum.SUCCESS);
			productService.saveOrUpdateProductArtShow(productArtShow);
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}else if(0==status&&null==productArtShow){
			json = new JsonResult("2", "该作品未参加该画展");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}else{
			json = new JsonResult("2", "该作品已经参加该画展");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
	}*/
	
	/**
	 * 取消作品画展
	 * @author wcj
	 * 2019-3-11下午10:05:52
	 * @param request
	 * @param response
	 * @param displayId
	 * @param productId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/delProductArtShow")
	@ResponseBody
	public JSONObject delProductArtShow(HttpServletRequest request,
			HttpServletResponse response,Integer displayId,
			Integer productId) {
		Product product = productService.getProductByProductId(productId);
		ArtShow artShow = productService.getArtShowByArtShowtId(displayId);
		JsonResult json ;
		if(null==product){
			json = new JsonResult("2", "找不到该作品");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
		if(null==artShow){
			json = new JsonResult("2", "找不到该画展");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
		ProductArtShow productArtShow = productService.getProductArtShowByTwoId(productId,displayId);
		if(null!=productArtShow){
			if(((System.currentTimeMillis()  - productArtShow.getrTime().getTime())<604800000)){
				json = new JsonResult("2", "参展7天后才能取消");
				return JSONObject.fromObject(json, PubUtil.getJsonConfig());
			}else{
				productService.deleteProductArtShow(displayId,productId);
				json = new JsonResult(DescribableEnum.SUCCESS);
				return JSONObject.fromObject(json, PubUtil.getJsonConfig());
			}
		}else{
			json = new JsonResult("2", "该作品未参加该画展");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
	}
	
	/**
	 * 设置作品画展
	 * @author wcj
	 * 2019-3-11下午10:38:36
	 * @param request
	 * @param response
	 * @param displayId
	 * @param productId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/setProductArtShow")
	@ResponseBody
	public JSONObject setProductArtShow(HttpServletRequest request,
			HttpServletResponse response,Integer displayId,
			Integer productId) {
		Product product = productService.getProductByProductId(productId);
		ArtShow artShow = productService.getArtShowByArtShowtId(displayId);
		JsonResult json ;
		if(null==product){
			json = new JsonResult("2", "找不到该作品");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
		if(null==artShow){
			json = new JsonResult("2", "找不到该画展");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
		ProductArtShow productArtShow = productService.getProductArtShowByTwoId(productId,displayId);
		if(null!=productArtShow){
			json = new JsonResult("2", "该作品已经参加该画展");
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}else{
			ProductArtShow t = new ProductArtShow();
			t.setDisplayId(displayId);
			t.setProductId(productId);
			t.setrTime(new Date());
			productService.saveOrUpdateProductArtShow(t);
			json = new JsonResult(DescribableEnum.SUCCESS);
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
	}
	
	/**
	 * 获取签名接口
	 * @author wcj
	 * 2019-3-11下午10:30:23
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 * @throws Exception
	 * JSONObject
	 */
	@RequestMapping("/getSignature")
	@ResponseBody
	public JSONObject getSignature(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResult json;
		Signature sign = new Signature();
		org.json.JSONObject s = sign.getUploadSign2();
		org.json.JSONObject s2 = (org.json.JSONObject) s.get("credentials");
		if(null==s.get("credentials")){
			json = new JsonResult(DescribableEnum.FAIL);
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}else{
			json = new JsonResult(DescribableEnum.SUCCESS);
			json.addData("startTime", s.get("startTime"));
			json.addData("requestId", s.get("requestId"));
			json.addData("expiredTime", s.get("expiredTime"));
			json.addData("sessionToken", s2.get("sessionToken"));
			json.addData("tmpSecretId", s2.get("tmpSecretId"));
			json.addData("tmpSecretKey", s2.get("tmpSecretKey"));
			return JSONObject.fromObject(json, PubUtil.getJsonConfig());
		}
	}
	
	/**
	 * 收藏、取消收藏作品
	 * @author wcj
	 * 2019-3-13下午5:34:32
	 * @param request
	 * @param response
	 * @param beCollecId
	 * @param status
	 * @param userId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/addOrDelCollectProduct")
	@ResponseBody
	public JSONObject addOrDelCollectProduct(HttpServletRequest request,
			HttpServletResponse response, Integer beCollecId, Integer status,
			String userId) {
		User user = userService.getUserById(userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		Collection tem = attentionService.getCollectCidAndUidAndType(
				beCollecId, userId);
		ProductCount pc = productService
				.getProductCountByProductId(beCollecId);
		if (null != tem) {
			if (status == 0) {
				if (tem.getStatus() != 0) {
					tem.setStatus(0);
					tem.setCollectTime(new Date());
					attentionService.saveOrUpdateCollection(tem);
					if(pc!=null){
						if(pc.getCollectCount()!=null&&pc.getCollectCount()>1){
							pc.setCollectCount(pc.getCollectCount()-1);
						}else{
							pc.setCollectCount(0);
						}
						productService.saveOrUpdaProductCount(pc);
					}else{
					}
				}
			} else {
				if (tem.getStatus() != 1) {
					tem.setStatus(1);
					tem.setCollectTime(new Date());
					attentionService.saveOrUpdateCollection(tem);
					if(pc!=null){
						if(pc.getCollectCount()!=null&&pc.getCollectCount()>0){
							pc.setCollectCount(pc.getCollectCount()+1);
						}else{
							pc.setCollectCount(1);
						}
						productService.saveOrUpdaProductCount(pc);
					}else{
						Product product = productService.getProductByProductId(beCollecId);
						ProductCount productCount= new ProductCount();
						productCount.setCollectCount(1);
						productCount.setProductId(beCollecId);
						productCount.setPublisherId(product.getShareId());
						productService.saveOrUpdaProductCount(productCount);
					}
				}
			}
		} else {
			if (status == 1) {
				Collection collection = new Collection();
				collection.setCollectBy(userId);
				collection.setCollectionId(beCollecId);
				collection.setCollectTime(new Date());
				collection.setStatus(1);
				attentionService.saveOrUpdateCollection(collection);
				if(pc!=null){
					if(pc.getCollectCount()!=null&&pc.getCollectCount()>0){
						pc.setCollectCount(pc.getCollectCount()+1);
					}else{
						pc.setCollectCount(1);
					}
					productService.saveOrUpdaProductCount(pc);
				}else{
					Product product = productService.getProductByProductId(beCollecId);
					ProductCount productCount= new ProductCount();
					productCount.setCollectCount(1);
					productCount.setProductId(beCollecId);
					productCount.setPublisherId(product.getShareId());
					productService.saveOrUpdaProductCount(productCount);
				}
			}
		}
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	/**
	 * 收藏、取消收藏画展
	 * @author wcj
	 * 2019-3-13下午5:34:32
	 * @param request
	 * @param response
	 * @param artShowId
	 * @param status
	 * @param userId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/addOrDelCollectionArtShow")
	@ResponseBody
	public JSONObject addOrDelCollectionArtShow(HttpServletRequest request,
			HttpServletResponse response, Integer artShowId, Integer status,
			String userId) {
		User user = userService.getUserById(userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		RUserArtShow tem = attentionService.getCollectArtShow(
				artShowId, userId);
		ArtShowCount pc = productService
				.getArtShowCountByArtShowId(artShowId);
		if (null != tem) {
			if (status == 0) {
				if (tem.getStatus() != 0) {
					tem.setStatus(0);
					tem.setCollectTime(new Date());
					attentionService.saveOrUpdateRUserArtShow(tem);
					if(pc!=null){
						if(pc.getCollectCount()!=null&&pc.getCollectCount()>1){
							pc.setCollectCount(pc.getCollectCount()-1);
						}else{
							pc.setCollectCount(0);
						}
						productService.saveOrUpdaArtShowCount(pc);
					}else{
					}
				}
			} else {
				if (tem.getStatus() != 1) {
					tem.setStatus(1);
					tem.setCollectTime(new Date());
					attentionService.saveOrUpdateRUserArtShow(tem);
					if(pc!=null){
						if(pc.getCollectCount()!=null&&pc.getCollectCount()>0){
							pc.setCollectCount(pc.getCollectCount()+1);
						}else{
							pc.setCollectCount(1);
						}
						productService.saveOrUpdaArtShowCount(pc);
					}else{
						ArtShowCount artShowCount= new ArtShowCount();
						artShowCount.setCollectCount(1);
						artShowCount.setArtShowId(artShowId);
						productService.saveOrUpdaArtShowCount(artShowCount);
					}
				}
			}
		} else {
			if (status == 1) {
				RUserArtShow rUserArtShow = new RUserArtShow();
				rUserArtShow.setArtShowId(artShowId);
				rUserArtShow.setUserId(userId);
				rUserArtShow.setCollectTime(new Date());
				rUserArtShow.setStatus(1);
				attentionService.saveOrUpdateRUserArtShow(rUserArtShow);
				if(pc!=null){
					if(pc.getCollectCount()!=null&&pc.getCollectCount()>0){
						pc.setCollectCount(pc.getCollectCount()+1);
					}else{
						pc.setCollectCount(1);
					}
					productService.saveOrUpdaArtShowCount(pc);
				}else{
					ArtShowCount artShowCount= new ArtShowCount();
					artShowCount.setCollectCount(1);
					artShowCount.setArtShowId(artShowId);
					productService.saveOrUpdaArtShowCount(artShowCount);

				}
			}
		}
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}

	/**
	 * 获取推荐作品列表
	 * @author wcj
	 * 2019-3-13下午11:45:38
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/getRecommendProductList")
	@ResponseBody
	public JSONObject getRecommendProductList(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize,String userId) {
		PageObject pageObject = attentionService.getRecommendProductList(new PageObject(
				pageNum, pageSize),userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("list", pageObject.getData());
		json.addData("total", pageObject.getDataCount());
		json.addData("nowPage", pageNum);
		json.addData("nowList", pageObject.getData().size());
		json.addData("totalPage",(pageObject.getDataCount()+pageSize-1)/pageSize);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	/**
	 * 获取关注用户作品列表
	 * @author wcj
	 * 2019-3-13下午5:35:12
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/getAttentionProductList")
	@ResponseBody
	public JSONObject getAttentionProductList(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize,String userId) {
		PageObject pageObject = attentionService.getAttentionProductList(new PageObject(
				pageNum, pageSize),userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("list", pageObject.getData());
		json.addData("total", pageObject.getDataCount());
		json.addData("nowPage", pageNum);
		json.addData("nowList", pageObject.getData().size());
		json.addData("totalPage",(pageObject.getDataCount()+pageSize-1)/pageSize);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	/**
	 * 作品详情
	 * @author wcj
	 * 2019-3-13下午11:34:49
	 * @param request
	 * @param response
	 * @param userId
	 * @param productId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/getProductDetails")
	@ResponseBody
	public JSONObject getProductDetails(HttpServletRequest request,
			HttpServletResponse response, String userId,Integer productId) {
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		Product product = productService.getProductByProductId(productId);
		ProductCount pc = productService
				.getProductCountByProductId(productId);
		if(null!=pc){
			json.addData("collectCount", pc.getCollectCount());
			json.addData("pageView", pc.getPageView());
			json.addData("commentCount", pc.getCommentCount());
		}else{
			ProductCount productCount= new ProductCount();
			productCount.setPageView(1);
			productCount.setProductId(productId);
			productCount.setPublisherId(product.getShareId());
			productService.saveOrUpdaProductCount(productCount);
			json.addData("collectCount", 0);
			json.addData("pageView", 1);
			json.addData("commentCount", 0);
		}
		String shareId = product.getShareId();
		User shareUser = userService.getUserByOpenId(shareId);
		ProductArtShow productArtShow = productService.getProductArtShowProductId(productId);
		json.addData("title", product.getTitle());
		json.addData("coveUrl", product.getCoveUrl());
		json.addData("shareUserId", product.getShareId());
		json.addData("shareUserNickName", shareUser.getNickName());
		json.addData("shareUserAvater", shareUser.getHeadImage());
		json.addData("publishTime", product.getPublishTime());
		json.addData("description", product.getDescription());
		json.addData("message", product.getMessage());
		json.addData("background", product.getBackground());
		json.addData("artShow", "未参加画展");
		if(null!=productArtShow){
			ArtShow artShow = productService.getArtShowByArtShowtId(productArtShow.getDisplayId());
			if(null!=artShow){
				json.addData("artShow", artShow.getTitle());
			}
		}
		//TODO 价格、卖出数量、评论
		Collection collection = attentionService.getCollectCidAndUidAndType(productId, userId);
		if(null!=collection&&collection.getStatus()==1){
			json.addData("collectionStatus", 1);
		}else{
			json.addData("collectionStatus", 0);
		}
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	/**
	 * 获取用户收藏作品列表
	 * @author wcj
	 * 2019-3-15上午1:10:58
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/getCollectProductList")
	@ResponseBody
	public JSONObject getCollectProductList(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize,String userId) {
		PageObject pageObject = attentionService.getCollectProductList(new PageObject(
				pageNum, pageSize),userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("list", pageObject.getData());
		json.addData("total", pageObject.getDataCount());
		json.addData("nowPage", pageNum);
		json.addData("nowList", pageObject.getData().size());
		json.addData("totalPage",(pageObject.getDataCount()+pageSize-1)/pageSize);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	/**
	 * 获取用户收藏画展列表
	 * @author wcj
	 * 2019-3-15下午10:35:38
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/getCollectArtShowList")
	@ResponseBody
	public JSONObject getCollectArtShowList(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize,String userId) {
		PageObject pageObject = attentionService.getCollectArtShowList(new PageObject(
				pageNum, pageSize),userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("list", pageObject.getData());
		json.addData("total", pageObject.getDataCount());
		json.addData("nowPage", pageNum);
		json.addData("nowList", pageObject.getData().size());
		json.addData("totalPage",(pageObject.getDataCount()+pageSize-1)/pageSize);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	/**
	 * 我的发布作品列表
	 * @author wcj
	 * 2019-3-15下午11:43:17
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/getProductListBuUserId")
	@ResponseBody
	public JSONObject getProductListBuUserId(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize,String userId) {
		PageObject pageObject = attentionService.getProductListBuUserId(new PageObject(
				pageNum, pageSize),userId);
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("list", pageObject.getData());
		json.addData("total", pageObject.getDataCount());
		json.addData("nowPage", pageNum);
		json.addData("nowList", pageObject.getData().size());
		json.addData("totalPage",(pageObject.getDataCount()+pageSize-1)/pageSize);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	

	/**
	 * 获取热门作品（一周内点赞最多的）
	 * @author wcj
	 * 2019-3-16下午9:19:57
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/getHotProductList")
	@ResponseBody
	public JSONObject getHotProductList(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize) {
		PageObject pageObject = attentionService.getHotProductList(new PageObject(
				pageNum, pageSize));
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("list", pageObject.getData());
		json.addData("total", pageObject.getDataCount());
		json.addData("nowPage", pageNum);
		json.addData("nowList", pageObject.getData().size());
		json.addData("totalPage",(pageObject.getDataCount()+pageSize-1)/pageSize);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	/**
	 * 画展列表
	 * @author wcj
	 * 2019-3-16下午9:37:59
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * JSONObject
	 */
	@RequestMapping("/getArtShowList")
	@ResponseBody
	public JSONObject getArtShowList(HttpServletRequest request,
			HttpServletResponse response, Integer pageNum, Integer pageSize) {
		PageObject pageObject = attentionService.getArtShowList(new PageObject(
				pageNum, pageSize));
		JsonResult json = new JsonResult(DescribableEnum.SUCCESS);
		json.addData("list", pageObject.getData());
		json.addData("total", pageObject.getDataCount());
		json.addData("nowPage", pageNum);
		json.addData("nowList", pageObject.getData().size());
		json.addData("totalPage",(pageObject.getDataCount()+pageSize-1)/pageSize);
		return JSONObject.fromObject(json, PubUtil.getJsonConfig());
	}
	
	@RequestMapping("/test")
	@ResponseBody
	public void test(HttpServletRequest request,
			HttpServletResponse response,String str) {
		String id = MD5.getInstance().getMD5(str);
		System.out.println(MD5.getInstance().getMD5(str+"wondball"));
	}
	
}