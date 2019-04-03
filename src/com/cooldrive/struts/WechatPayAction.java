package com.cooldrive.struts;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooldrive.common.config.SystemConfig;
import com.cooldrive.common.config.WechatConfig;
import com.cooldrive.common.util.DateTimeUtil;
import com.cooldrive.common.util.HttpReqUtil;
import com.cooldrive.common.util.MD5Util;
import com.cooldrive.common.util.PayUtil;
import com.cooldrive.common.util.QRCodeGenerator;
import com.cooldrive.common.util.SignatureUtil;
import com.cooldrive.common.util.XmlUtil;
import com.cooldrive.db.HudOrderDeliverCompany;
import com.cooldrive.db.HudOrderDetails;
import com.cooldrive.db.HudOrderProducts;
import com.cooldrive.db.HudOrders;
import com.cooldrive.entity.HudOrderInfo;
import com.cooldrive.enums.HudOrderStatus;
import com.cooldrive.wechatauth.model.req.AuthTokenParams;
import com.cooldrive.wechatauth.model.resp.AuthAccessToken;
import com.cooldrive.wechatauth.service.WechatAuthService;
import com.cooldrive.wechatauth.service.impl.WechatAuthServiceImpl;
import com.cooldrive.wechatpay.model.rep.PayShortUrlParams;
import com.cooldrive.wechatpay.model.rep.UnifiedOrderParams;
import com.cooldrive.wechatpay.model.resp.JsPayResult;
import com.cooldrive.wechatpay.model.resp.PayShortUrlResult;
import com.cooldrive.wechatpay.model.resp.UnifiedOrderResult;
import com.cooldrive.wechatpay.service.impl.WechatPayServiceImpl;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 微信支付三种方式
 * 
 * @author phil
 * @date 2017年6月27日
 * 
 */
// @Controller
// @RequestMapping("/wxpay/")
public class WechatPayAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger
			.getLogger(WechatPayAction.class);

	public static int defaultWidthAndHeight = 200;

	// @Autowired
	private WechatPayServiceImpl wechatPayService;

	// @Autowired
	private WechatAuthServiceImpl wechatAuthService;

	private JSONObject jsresult;
	private JsPayResult jspayresult;

	public JsPayResult getJspayresult() {
		return jspayresult;
	}

	public void setJspayresult(JsPayResult jspayresult) {
		this.jspayresult = jspayresult;
	}

	public JSONObject getJsresult() {
		return jsresult;
	}

	public void setJsresult(JSONObject jsresult) {
		this.jsresult = jsresult;
	}

	public WechatPayServiceImpl getWechatPayService() {
		return wechatPayService;
	}

	public void setWechatPayService(WechatPayServiceImpl wechatPayService) {
		this.wechatPayService = wechatPayService;
	}

	public WechatAuthServiceImpl getWechatAuthService() {
		return wechatAuthService;
	}

	public void setWechatAuthService(WechatAuthServiceImpl wechatAuthService) {
		this.wechatAuthService = wechatAuthService;
	}

	/**
	 * 授权进入支付页面
	 * 
	 * @param request
	 * @param response
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "jspayview", method = { RequestMethod.GET })
	public String prePayPage(HttpServletRequest request,
			HttpServletResponse response, String url) throws Exception {
		AuthAccessToken authAccessToken = null;
		String code = request.getParameter("code");// redis保存
		if (code == null) {
			return "统一的错误页面";
		}
		String state = request.getParameter("state");
		if (state.equals(MD5Util.MD5Encode("ceshi", ""))) {
			AuthTokenParams authTokenParams = new AuthTokenParams();
			authTokenParams.setAppid(WechatConfig.APP_ID);
			authTokenParams.setSecret(WechatConfig.APP_SECRET);
			authTokenParams.setCode(code);
			authAccessToken = wechatAuthService.getAuthAccessToken(
					authTokenParams, WechatConfig.ACCESS_TOKEN_URL);
		}
		if (authAccessToken != null) {
			logger.info("正在支付的openid=" + authAccessToken.getOpenid());
			request.setAttribute("openId", authAccessToken.getOpenid());
		}
		return "wxpay/jspay";
	}

	
	
	
	//公众号 网页授权域名设置的微信授权回调接口
	public String queryOrderCallback() throws Exception {
		logger.info("query order call back!");
		AuthAccessToken authAccessToken = null;
		String code = request.getParameter("code");// redis保存
		if (code == null) {
			logger.info("wechat query order callback, code is null");
			return "error";
		} else {
			logger.info(" code:" + code);
		}
		String state = request.getParameter("state");
		if (state.equals(MD5Util.MD5Encode("cooldrivehud", ""))) {
			AuthTokenParams authTokenParams = new AuthTokenParams();
			authTokenParams.setAppid(WechatConfig.APP_ID);
			authTokenParams.setSecret(WechatConfig.APP_SECRET);
			authTokenParams.setCode(code);
			authAccessToken = wechatAuthService.getAuthAccessToken(
					authTokenParams, WechatConfig.ACCESS_TOKEN_URL);
		}
		if (authAccessToken != null) {
			String openId = authAccessToken.getOpenid();
			logger.info("get openid:" + openId);
			request.setAttribute("openId", openId);

		} else {
			logger.info("get openid failed");
		}

		return "success"; // redirect to "order_detail.jsp"; queryOrderCallback
	}
	
	
	
	// http://localhost:8080/WxPay/wechatCallback.action?code=12345&state=f211b35536e482d1049b836fb2d7437f
	// http://wx.cooldrivenavi.com:8081/WxPay/wechatCallback.action?code=12345&state=f211b35536e482d1049b836fb2d7437f
	// 微信回调方法，返回code, 通过code获得token, 再通过token获得 openId, 返回客户端
	public String wechatCallback() throws Exception {
		// response.setHeader("Access-Control-Allow-Origin", "*");

		// , String url
		logger.info("get wechat callback!");
		AuthAccessToken authAccessToken = null;
		String code = request.getParameter("code");// redis保存
		if (code == null) {
			logger.info("wechat callback, code is null");
			return "error";
		} else {
			logger.info(" code:" + code);
		}
		String state = request.getParameter("state");
		if (state.equals(MD5Util.MD5Encode("cooldrivehud", ""))) {
			AuthTokenParams authTokenParams = new AuthTokenParams();
			authTokenParams.setAppid(WechatConfig.APP_ID);
			authTokenParams.setSecret(WechatConfig.APP_SECRET);
			authTokenParams.setCode(code);
			authAccessToken = wechatAuthService.getAuthAccessToken(
					authTokenParams, WechatConfig.ACCESS_TOKEN_URL);
		}
		if (authAccessToken != null) {
			String openId = authAccessToken.getOpenid();
			logger.info("get openid:" + openId);
			request.setAttribute("openId", openId);

		} else {
			logger.info("get openid failed");
		}

		return "success"; // redirect to "confirm_item.jsp";
	}

	//扫描支付，微信回调
	
	public String qrCodePayCallback() throws Exception {
		logger.info("qrCode Pay Callback!");
		AuthAccessToken authAccessToken = null;
		String code = request.getParameter("code");// redis保存
		if (code == null) {
			logger.info("wechat callback, code is null");
			return "error";
		} else {
			logger.info(" code:" + code);
		}
		String state = request.getParameter("state");
		if (state.equals(MD5Util.MD5Encode("cooldrivehud", ""))) {
			AuthTokenParams authTokenParams = new AuthTokenParams();
			authTokenParams.setAppid(WechatConfig.APP_ID);
			authTokenParams.setSecret(WechatConfig.APP_SECRET);
			authTokenParams.setCode(code);
			authAccessToken = wechatAuthService.getAuthAccessToken(
					authTokenParams, WechatConfig.ACCESS_TOKEN_URL);
		}
		if (authAccessToken != null) {
			String openId = authAccessToken.getOpenid();
			logger.info("get openid:" + openId);
			request.setAttribute("openId", openId);

		} else {
			logger.info("get openid failed");
		}

		
		return "success"; // redirect to "qrcodepay_item.jsp";
	}
		
	
	// 得到统一下单的xml
	private static String getOrderRequestXml(UnifiedOrderParams reqBean) {

		SortedMap<Object, Object> param = new TreeMap<Object, Object>();
		param.put("appid", reqBean.getAppid());
		// param.put("attach", reqBean.getAttach());
		param.put("body", reqBean.getBody());
		// param.put("detail", reqBean.getDetail());
		// param.put("device_info", reqBean.getDevice_info());
		param.put("mch_id", reqBean.getMch_id());
		param.put("nonce_str", reqBean.getNonce_str());
		param.put("notify_url", reqBean.getNotify_url());
		param.put("openid", reqBean.getOpenid());
		param.put("out_trade_no", reqBean.getOut_trade_no());
		param.put("spbill_create_ip", reqBean.getSpbill_create_ip());
		param.put("total_fee", reqBean.getTotal_fee());
		param.put("trade_type", reqBean.getTrade_type());

		// get sign
		String sign = SignatureUtil.createSign(reqBean, WechatConfig.API_KEY,
				SystemConfig.CHARACTER_ENCODING);
		reqBean.setSign(sign);

		param.put("sign", reqBean.getSign());

		return getRequestXml(param);
	}


	// 把参数封装为xml
	private static String getRequestXml(SortedMap<Object, Object> params) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<Entry<Object, Object>> es = params.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it
					.next();
			String k = (String) entry.getKey();
			String v = (String) String.valueOf(entry.getValue());

			sb.append("<" + k + ">" + v + "</" + k + ">");

			// if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
			// sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			// } else {
			// sb.append("<" + k + ">" + v + "</" + k + ">");
			// }
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 微信内H5调起支付
	 * 
	 * @param request
	 * @param response
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	// @ResponseBody
	// @RequestMapping("wechatPay")
	@SuppressWarnings("unused")
	public void wechatPay() throws Exception {

		try {
			logger.info("call wechatPay()");

			String openId = request.getParameter("openId");

			String productStr = request.getParameter("products");
			JSONArray products = JSONArray.fromObject(productStr);

			String order_channel = request.getParameter("order_channel"); // 订单渠道：
																			// 1-微信，
																			// 2-支付宝，
																			// 3-
																			// 其他
			String deliver_user = request.getParameter("deliver_user"); // 收货人姓名
			String deliver_address = request.getParameter("deliver_address"); // 收货地址
			String deliver_phone = request.getParameter("deliver_phone"); // 收货人电话
			String deliver_company = request.getParameter("deliver_company"); // 快递公司
																				// ，
																				// 1-
																				// 顺丰，
																				// 2-其他

			// 1. create a order in db and return order id
			JsPayResult result = new JsPayResult();
			logger.info("****正在支付的openId****" + openId);
			
			// 统一下单
			List<HudOrderDetails> orderDetails = new ArrayList<HudOrderDetails>();
			for (int i = 0; i < products.size(); i++) {
				JSONObject pro = products.getJSONObject(i);

				HudOrderDetails detail = new HudOrderDetails();
				detail.setProductId(pro.getInt("product_id"));
				detail.setQuantity(pro.getInt("quantity"));

				orderDetails.add(detail);

			}

			// String out_trade_no = PayUtil.cereateOutTradeNo();
			logger.info("start creating order:" + openId);

			HudOrders order = wechatPayService.createOrder(openId,
					orderDetails, 0,
					deliver_user, deliver_address, deliver_phone,
					Integer.parseInt(deliver_company));

			logger.info("create order:" + order.getOrderId());
			
			if (order == null) {
				logger.info(" order is null");
				return;
			}

			// 2. call wechat unified order API to get prepay_id
			String out_trade_no = String.valueOf(order.getOrderId());
			int total_fee = (int) (order.getPayAmount() * 100);

			logger.info("Created order:" + out_trade_no+", amount:"+total_fee);
			
			String spbill_create_ip = HttpReqUtil.getRemortIP(request);
			logger.info("支付IP:" + spbill_create_ip);
			String nonce_str = PayUtil.createNonceStr(); // 随机数据
			// 参数组装
			UnifiedOrderParams unifiedOrderParams = new UnifiedOrderParams();
			unifiedOrderParams.setAppid(WechatConfig.APP_ID);// 必须
			unifiedOrderParams.setMch_id(WechatConfig.MCH_ID);// 必须
			unifiedOrderParams.setOut_trade_no(out_trade_no);// 必须
			unifiedOrderParams.setBody("酷驾导航HUD");// 必须
			unifiedOrderParams.setTotal_fee(total_fee); // 必须
			unifiedOrderParams.setNonce_str(nonce_str); // 必须
			unifiedOrderParams.setSpbill_create_ip(spbill_create_ip); // 必须
			unifiedOrderParams.setTrade_type("JSAPI"); // 必须
			unifiedOrderParams.setOpenid(openId);
			unifiedOrderParams.setNotify_url(WechatConfig.NOTIFY_URL);// 异步通知url

			// 统一下单 请求的Xml(正常的xml格式)
			String unifiedXmL = getOrderRequestXml(unifiedOrderParams);// //签名并入service

			// String unifiedXmL =
			// wechatPayService.abstractPayToXml(unifiedOrderParams);////签名并入service
			// 返回<![CDATA[SUCCESS]]>格式的XML
			logger.info("get unified XmL:" + unifiedXmL);
			String unifiedOrderResultXmL = HttpReqUtil.HttpsDefaultExecute(
					HttpReqUtil.POST_METHOD, WechatConfig.UNIFIED_ORDER_URL,
					null, unifiedXmL);
			// 进行签名校验
			logger.info("get unified Order Result XmL:" + unifiedOrderResultXmL);
			if (SignatureUtil.checkIsSignValidFromWeiXin(unifiedOrderResultXmL)) {
				String timeStamp = PayUtil.createTimeStamp();
				// 统一下单响应
				logger.info("get unifiedOrderResult");
				UnifiedOrderResult unifiedOrderResult = XmlUtil
						.getObjectFromXML(unifiedOrderResultXmL,
								UnifiedOrderResult.class);
				/**** 用map方式进行签名 ****/
				SortedMap<Object, Object> signMap = new TreeMap<Object, Object>();
				signMap.put("appId", WechatConfig.APP_ID); // 必须
				signMap.put("timeStamp", timeStamp);
				signMap.put("nonceStr", unifiedOrderResult.getNonce_str());
				signMap.put("signType", "MD5");
				signMap.put("package",
						"prepay_id=" + unifiedOrderResult.getPrepay_id());

				String paySign = SignatureUtil.createSign(signMap,
						WechatConfig.API_KEY, SystemConfig.CHARACTER_ENCODING);

				result.setAppId(WechatConfig.APP_ID);
				result.setTimeStamp(timeStamp);
				result.setNonceStr(unifiedOrderResult.getNonce_str());// 直接用返回的
				/**** prepay_id 2小时内都有效，再次支付方法自己重写 ****/
				result.setPackageStr("prepay_id="
						+ unifiedOrderResult.getPrepay_id());
				/**** 用对象进行签名 ****/
				result.setPaySign(paySign);
				result.setResultCode(unifiedOrderResult.getResult_code());
				
			} else {
				logger.info("签名验证错误");
				result.setResultCode("ERROR");
			}

			jsresult = ConvertJsPayResult2JSON(result);
			jsresult.put("out_trade_no", out_trade_no); //return order no

			responseJsonResult(response, jsresult);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return;
		}

		return;

	}

	//扫码支付，下单
	public void qrcodePay() throws Exception {

		try {
			

			String openId = request.getParameter("openId");  	//用户Id
			String seatId = request.getParameter("seatId");  	//座椅编号
			String quantity = request.getParameter("quantity");  //选项： 1- 5分钟， 2-10分钟，3-15分钟
			
			logger.info("call qrcodePay(), seatId:"+seatId);
			
			int quantityI = 0;
			try{
				quantityI = Integer.parseInt(quantity);
			}catch(Exception ex){
				logger.info(ex.getMessage());
			}
		
			// 1. create a order in db and return order id
			JsPayResult result = new JsPayResult();
			logger.info("****正在支付的openId****" + openId);
			
			// 统一下单
			List<HudOrderDetails> orderDetails = new ArrayList<HudOrderDetails>();

			HudOrderDetails detail = new HudOrderDetails();
			detail.setProductId(2); //产品id 2: 按摩座椅
			detail.setQuantity(quantityI);

			orderDetails.add(detail);

			// String out_trade_no = PayUtil.cereateOutTradeNo();
			logger.info("start creating order:" + openId);

			HudOrders order = wechatPayService.createOrder(openId,
				orderDetails, 0,
				"", seatId, "",
				0);

			logger.info("create order:" + order.getOrderId());
			
			// 2. call wechat unified order API to get prepay_id
			String out_trade_no = String.valueOf(order.getOrderId());
			int total_fee = (int) (order.getPayAmount() * 100);

			logger.info("Created order:" + out_trade_no+", amount:"+total_fee);
			
			String spbill_create_ip = HttpReqUtil.getRemortIP(request);
			logger.info("支付IP:" + spbill_create_ip);
			String nonce_str = PayUtil.createNonceStr(); // 随机数据
			// 参数组装
			UnifiedOrderParams unifiedOrderParams = new UnifiedOrderParams();
			unifiedOrderParams.setAppid(WechatConfig.APP_ID);// 必须
			unifiedOrderParams.setMch_id(WechatConfig.MCH_ID);// 必须
			unifiedOrderParams.setOut_trade_no(out_trade_no);// 必须
			unifiedOrderParams.setBody("酷驾导航HUD");// 必须
			unifiedOrderParams.setTotal_fee(total_fee); // 必须
			unifiedOrderParams.setNonce_str(nonce_str); // 必须
			unifiedOrderParams.setSpbill_create_ip(spbill_create_ip); // 必须
			unifiedOrderParams.setTrade_type("JSAPI"); // 必须
			unifiedOrderParams.setOpenid(openId);
			unifiedOrderParams.setNotify_url(WechatConfig.NOTIFY_URL);// 异步通知url

			// 统一下单 请求的Xml(正常的xml格式)
			String unifiedXmL = getOrderRequestXml(unifiedOrderParams);// //签名并入service

			// String unifiedXmL =
			// wechatPayService.abstractPayToXml(unifiedOrderParams);////签名并入service
			// 返回<![CDATA[SUCCESS]]>格式的XML
			logger.info("get unified XmL:" + unifiedXmL);
			String unifiedOrderResultXmL = HttpReqUtil.HttpsDefaultExecute(
					HttpReqUtil.POST_METHOD, WechatConfig.UNIFIED_ORDER_URL,
					null, unifiedXmL);
			// 进行签名校验
			logger.info("get unified Order Result XmL:" + unifiedOrderResultXmL);
			if (SignatureUtil.checkIsSignValidFromWeiXin(unifiedOrderResultXmL)) {
				String timeStamp = PayUtil.createTimeStamp();
				// 统一下单响应
				logger.info("get unifiedOrderResult");
				UnifiedOrderResult unifiedOrderResult = XmlUtil
						.getObjectFromXML(unifiedOrderResultXmL,
								UnifiedOrderResult.class);
				/**** 用map方式进行签名 ****/
				SortedMap<Object, Object> signMap = new TreeMap<Object, Object>();
				signMap.put("appId", WechatConfig.APP_ID); // 必须
				signMap.put("timeStamp", timeStamp);
				signMap.put("nonceStr", unifiedOrderResult.getNonce_str());
				signMap.put("signType", "MD5");
				signMap.put("package",
						"prepay_id=" + unifiedOrderResult.getPrepay_id());

				String paySign = SignatureUtil.createSign(signMap,
						WechatConfig.API_KEY, SystemConfig.CHARACTER_ENCODING);

				result.setAppId(WechatConfig.APP_ID);
				result.setTimeStamp(timeStamp);
				result.setNonceStr(unifiedOrderResult.getNonce_str());// 直接用返回的
				/**** prepay_id 2小时内都有效，再次支付方法自己重写 ****/
				result.setPackageStr("prepay_id="
						+ unifiedOrderResult.getPrepay_id());
				/**** 用对象进行签名 ****/
				result.setPaySign(paySign);
				result.setResultCode(unifiedOrderResult.getResult_code());
				
				
			} else {
				logger.info("签名验证错误");
				result.setResultCode("ERROR");
			}

			jsresult = ConvertJsPayResult2JSON(result);
			jsresult.put("out_trade_no", out_trade_no); //return order no

			responseJsonResult(response, jsresult);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return;
		}

		return;

	}
	
	// 支付未付款订单
	public void payOrder() throws Exception {

		try {
			logger.info("call payorder()");
			String orderNo = request.getParameter("orderNo");
			String openId =  request.getParameter("openId");
			
			long order_id = 0;
			try {
				order_id = Long.parseLong(orderNo);
			} catch (Exception ex) {
				logger.info(ex.getMessage());
				return;
			}

			HudOrderInfo order = wechatPayService.getOrderByOrderId(order_id);

			logger.info("get order:" + order.getOrderId());

			// 2. call wechat unified order API to get prepay_id
			String out_trade_no = String.valueOf(order.getOrderId());
			int total_fee = (int) (order.getPayAmount() * 100);

			String spbill_create_ip = HttpReqUtil.getRemortIP(request);
			logger.info("支付IP:" + spbill_create_ip);
			String nonce_str = PayUtil.createNonceStr(); // 随机数据
			// 参数组装
			UnifiedOrderParams unifiedOrderParams = new UnifiedOrderParams();
			unifiedOrderParams.setAppid(WechatConfig.APP_ID);// 必须
			unifiedOrderParams.setMch_id(WechatConfig.MCH_ID);// 必须
			unifiedOrderParams.setOut_trade_no(out_trade_no);// 必须
			unifiedOrderParams.setBody("酷驾导航HUD");// 必须
			unifiedOrderParams.setTotal_fee(total_fee); // 必须
			unifiedOrderParams.setNonce_str(nonce_str); // 必须
			unifiedOrderParams.setSpbill_create_ip(spbill_create_ip); // 必须
			unifiedOrderParams.setTrade_type("JSAPI"); // 必须
			unifiedOrderParams.setOpenid(openId);
			unifiedOrderParams.setNotify_url(WechatConfig.NOTIFY_URL);// 异步通知url

			// 统一下单 请求的Xml(正常的xml格式)
			String unifiedXmL = getOrderRequestXml(unifiedOrderParams);// //签名并入service

			// String unifiedXmL =
			// wechatPayService.abstractPayToXml(unifiedOrderParams);////签名并入service
			// 返回<![CDATA[SUCCESS]]>格式的XML
			logger.info("get unified XmL:" + unifiedXmL);
			String unifiedOrderResultXmL = HttpReqUtil.HttpsDefaultExecute(
					HttpReqUtil.POST_METHOD, WechatConfig.UNIFIED_ORDER_URL,
					null, unifiedXmL);
			// 进行签名校验
			logger.info("get unified Order Result XmL:" + unifiedOrderResultXmL);
			if (SignatureUtil.checkIsSignValidFromWeiXin(unifiedOrderResultXmL)) {

				String timeStamp = PayUtil.createTimeStamp();
				// 统一下单响应
				logger.info("get unifiedOrderResult");
				UnifiedOrderResult unifiedOrderResult = XmlUtil
						.getObjectFromXML(unifiedOrderResultXmL,
								UnifiedOrderResult.class);
				
				// /**** 用map方式进行签名 ****/
				SortedMap<Object, Object> signMap = new TreeMap<Object, Object>();
				signMap.put("appId", WechatConfig.APP_ID); // 必须
				signMap.put("timeStamp", timeStamp);
				signMap.put("nonceStr", nonce_str);
				signMap.put("signType", "MD5");
				signMap.put("package", "prepay_id=" + unifiedOrderResult.getPrepay_id());

				String paySign = SignatureUtil.createSign(signMap,
						WechatConfig.API_KEY, SystemConfig.CHARACTER_ENCODING);

				jsresult = new JSONObject();

				jsresult.put("appId", WechatConfig.APP_ID);
				jsresult.put("timeStamp", timeStamp);
				jsresult.put("nonceStr", nonce_str);
				jsresult.put("signType", "MD5");
				jsresult.put("packageStr", "prepay_id=" + unifiedOrderResult.getPrepay_id());
				jsresult.put("paySign", paySign);

				jsresult.put("errMsg", "");
				jsresult.put("resultCode", "SUCCESS");
				jsresult.put("out_trade_no", out_trade_no); // return order no

				responseJsonResult(response, jsresult);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return;
		}

		return;

	}
	
	//get client orders
	public void getClientOrders(){
		try {
			String openId = request.getParameter("openId");
			
			logger.info("get client["+openId+"] orders");
			List<HudOrders> orders = wechatPayService.getOrdersByClientCode(openId);

			logger.info("client["+openId+"] order size:"+orders.size());
			
			JSONArray ordersJ = new JSONArray();

			for (HudOrders order : orders) {
				JSONObject orderJ = new JSONObject();
				orderJ.put("order_id", order.getOrderId());
				orderJ.put("order_channel",order.getOrderChannel());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				orderJ.put("order_time", sdf.format(order.getOrderTime()));
				orderJ.put("order_status", order.getOrderStatus());
				orderJ.put("pay_amount", order.getPayAmount());
				orderJ.put("deliver_fee", order.getDeliverFee());
				
				//get order details
				List orderDetails = wechatPayService.getOrderDetails(order.getOrderId());

				JSONArray orderDetailsJ = new JSONArray();
				int total_quantity = 0;
				 for(int i=0; i< orderDetails.size(); i++)
		    	 {
		    		 JSONObject orderdetail = new JSONObject();  
		    		 
		    		 Object [] detail = (Object [])orderDetails.get(i);

		    		 //p.product_name, p.discripton, d.unit_price, d.quantity
		    		 orderdetail.put("product_name", detail[0]);
		    		 orderdetail.put("discripton",detail[1]);
					
		    		 orderdetail.put("unit_price",detail[2]); 
		    		 int quantity = (Integer) detail[3];
		    		 total_quantity += quantity;
		    		 orderdetail.put("quantity", quantity);
									
					orderDetailsJ.add(orderdetail);
				}

				orderJ.put("total_quantity", total_quantity);
				orderJ.put("order_details", orderDetailsJ.toString());
				
				ordersJ.add(orderJ);
			}

			JSONObject ordersRet = new JSONObject();
			ordersRet.put("orders", ordersJ.toString());
			ordersRet.put("resultCode","SUCCESS");
			
			responseJsonResult(response, ordersRet);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
	}
	
	//cancel order
	public void cancelOrder(){
		try {
			String orderNo = request.getParameter("orderNo");
			long order_id = 0;
			try{
				order_id = Long.parseLong(orderNo);
			}catch(Exception ex){
				logger.info(ex.getMessage());
				
				JSONObject orderdetailRet = new JSONObject();
				orderdetailRet.put("resultMessage","order no is wrong");
				orderdetailRet.put("resultCode","FAILED");
				
				responseJsonResult(response, orderdetailRet);
				return;
				
			}
			logger.info("get order detail:"+order_id);
			
			//cancel order
			boolean res = wechatPayService.cancelOrder(order_id);
			
			JSONObject orderdetailRet = new JSONObject();
			if(res){

				orderdetailRet.put("order_details", "取消订单成功");
				orderdetailRet.put("resultCode","SUCCESS");
				
				
				responseJsonResult(response, orderdetailRet);
			}else{
				
				orderdetailRet.put("resultMessage","取消订单失败");
				orderdetailRet.put("resultCode","FAILED");
				responseJsonResult(response, orderdetailRet);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("取消订单失败:"+ex.getMessage());
			return;
		}
	}
	
	//get client orders
	public void getClientOrderDetails(){
		try {
			String orderNo = request.getParameter("orderNo");
			long order_id = 0;
			try{
				order_id = Long.parseLong(orderNo);
			}catch(Exception ex){
				logger.info(ex.getMessage());
				
				JSONObject orderdetailRet = new JSONObject();
				orderdetailRet.put("resultMessage","order no is wrong");
				orderdetailRet.put("resultCode","FAILED");
				
				responseJsonResult(response, orderdetailRet);
				return;
				
			}
			logger.info("get order detail:"+order_id);
			
			//get order info
			JSONObject orderdetailRet = new JSONObject();
			HudOrderInfo order = wechatPayService.getOrderByOrderId(order_id);
			if(order == null){

				orderdetailRet.put("resultMessage","order "+order_id+" is not existed");
				orderdetailRet.put("resultCode","FAILED");
				
				responseJsonResult(response, orderdetailRet);
				return;
			}
			
			
			orderdetailRet.put("order_id", order.getOrderId());
			orderdetailRet.put("order_channel",order.getOrderChannel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			orderdetailRet.put("order_time", sdf.format(order.getOrderTime()));
			orderdetailRet.put("order_status", order.getOrderStatus());
			orderdetailRet.put("total_amount", order.getTotalAmount());
			orderdetailRet.put("discount_amount",order.getDiscountAmount());
			orderdetailRet.put("pay_amount", order.getPayAmount());
			orderdetailRet.put("deliver_company",order.getDeliverCompanyId());
			orderdetailRet.put("deliver_company_name",order.getDeliverCompanyName());
			orderdetailRet.put("deliver_company_code",order.getDeliverCompanyCode());
			orderdetailRet.put("deliver_code",order.getDeliverCode());
			orderdetailRet.put("deliver_fee", order.getDeliverFee());
			orderdetailRet.put("deliver_user", order.getDeliverUserName());
			orderdetailRet.put("deliver_phone", order.getDeliverPhone());
			orderdetailRet.put("deliver_address", order.getDeliverAddress());
			orderdetailRet.put("payment_time", order.getPaymentTime() == null?"":sdf.format(order.getPaymentTime()));
			
			//get order details
			List orderDetails = wechatPayService.getOrderDetails(order_id);

			JSONArray orderDetailsJ = new JSONArray();

			 for(int i=0; i< orderDetails.size(); i++)
	    	 {
	    		 JSONObject o = new JSONObject();  
	    		 
	    		 Object [] detail = (Object [])orderDetails.get(i);

	    		 //p.product_name, p.discripton, d.unit_price, d.quantity
				o.put("product_name", detail[0]);
				o.put("discripton",detail[1]);
				
				o.put("unit_price",detail[2]);
				o.put("quantity", detail[3]);
								
				orderDetailsJ.add(o);
			}

			orderdetailRet.put("order_details", orderDetailsJ.toString());
			orderdetailRet.put("resultCode","SUCCESS");
			
			responseJsonResult(response, orderdetailRet);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("get order detail failed"+ex.getMessage());
			return;
		}
	}
	
	
	//get order deliver company list
	public void getDeliveryCompanyList(){
		try {
			logger.info("get deliver company list");
			List<HudOrderDeliverCompany> companies = wechatPayService.getOrderDeliverCompanies();

			JSONArray companiesJ = new JSONArray();

			for (HudOrderDeliverCompany comp : companies) {
				JSONObject p = new JSONObject();
				p.put("company_id", comp.getCompanyId());
				p.put("company_name", comp.getCompanyName());
				p.put("deliver_fee", comp.getDeliverFee());

				companiesJ.add(p);
			}

			JSONObject companiesRet = new JSONObject();
			companiesRet.put("deliverCompanies", companiesJ.toString());
			companiesRet.put("resultCode", "SUCCESS");

			responseJsonResult(response, companiesRet);

		} catch (Exception ex) {
			logger.info(ex.getMessage());
			ex.printStackTrace();
			return;
		}
		
	}
	
	// get order product list
	public void getSaleProductList() {

		try {
			List<HudOrderProducts> products = wechatPayService
					.getOrderProducts();

			JSONArray productsJ = new JSONArray();

			for (HudOrderProducts prod : products) {
				JSONObject p = new JSONObject();
				p.put("product_id", prod.getProductId());
				p.put("product_name", prod.getProductName());
				p.put("description", prod.getDiscription());
				p.put("unit_price", prod.getUnitPrice());
				p.put("product_code", prod.getProductCode());

				productsJ.add(p);
			}

			JSONObject productsRet = new JSONObject();
			productsRet.put("products", productsJ.toString());
			productsRet.put("resultCode", "SUCCESS");

			responseJsonResult(response, productsRet);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
	}

	private static JSONObject ConvertJsPayResult2JSON(JsPayResult result) {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("appId", result.getAppId());
		jsonObject.put("timeStamp", result.getTimeStamp());
		jsonObject.put("nonceStr", result.getNonceStr());
		jsonObject.put("signType", result.getSignType());
		jsonObject.put("packageStr", result.getPackageStr());
		jsonObject.put("paySign", result.getPaySign());

		jsonObject.put("errMsg", result.getErrMsg());
		jsonObject.put("resultCode", result.getResultCode());

		return jsonObject;
	}

	/**
	 * 扫码支付模式一生成二维码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("qrcode")
	public String createPayImage(HttpServletRequest request,
			HttpServletResponse response) {
		String nonce_str = PayUtil.createNonceStr();
		String product_id = "product_001"; // 推荐根据商品ID生成
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", WechatConfig.APP_ID);
		packageParams.put("mch_id", WechatConfig.MCH_ID);
		packageParams.put("product_id", product_id);
		packageParams.put("time_stamp", PayUtil.createTimeStamp());
		packageParams.put("nonce_str", nonce_str);
		String str = PayUtil.createPayImageUrl(packageParams);
		String sign = SignatureUtil.createSign(packageParams,
				WechatConfig.API_KEY, SystemConfig.CHARACTER_ENCODING);
		packageParams.put("sign", sign);
		String payurl = "weixin://wxpay/bizpayurl?sign=" + sign + str;
		logger.info("payurl is " + payurl);
		/**** 转成短链接 ****/
		PayShortUrlParams payShortUrlParams = new PayShortUrlParams();
		payShortUrlParams.setAppid(WechatConfig.APP_ID);
		payShortUrlParams.setMch_id(WechatConfig.MCH_ID);
		payShortUrlParams.setLong_url(payurl);
		payShortUrlParams.setNonce_str(nonce_str);
		String urlSign = SignatureUtil.createSign(payShortUrlParams,
				WechatConfig.API_KEY, SystemConfig.CHARACTER_ENCODING);
		payShortUrlParams.setSign(urlSign);
		String longXml = XmlUtil.toSplitXml(payShortUrlParams);
		String shortResult = HttpReqUtil.HttpsDefaultExecute(
				HttpReqUtil.POST_METHOD, WechatConfig.PAY_SHORT_URL, null,
				longXml);
		PayShortUrlResult payShortUrlResult = XmlUtil.getObjectFromXML(
				shortResult, PayShortUrlResult.class);
		if ("SUCCESS".equals(payShortUrlResult.getReturn_code())) {
			payurl = payShortUrlResult.getShort_url();
		} else {
			logger.debug("错误信息" + payShortUrlResult.getReturn_msg());
		}
		/**** 生成 二维码图片自己实现 ****/
		return null;
	}

	/**
	 * 扫码模式二
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("paytwo")
	public String paytwo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 商户后台系统根据用户选购的商品生成订单。
		String out_trade_no = PayUtil.createNonceStr();
		String product_id = DateTimeUtil.getCurrTime();// 根据自己的逻辑生成
		int total_fee = 1; // 1分作测试
		String attach = "支付测试";
		String body = "支付测试";
		String nonce_str = PayUtil.createNonceStr();
		String spbill_create_ip = HttpReqUtil.getRemortIP(request); // 获取IP
		UnifiedOrderParams unifiedOrderParams = new UnifiedOrderParams();
		unifiedOrderParams.setAppid(WechatConfig.APP_ID);// 必须
		unifiedOrderParams.setMch_id(WechatConfig.MCH_ID);// 必须
		unifiedOrderParams.setOut_trade_no(out_trade_no);
		unifiedOrderParams.setBody(body);
		unifiedOrderParams.setAttach(attach);
		unifiedOrderParams.setProduct_id(product_id);// 必须
		unifiedOrderParams.setTotal_fee(total_fee);// 必须
		unifiedOrderParams.setNonce_str(nonce_str); // 必须
		unifiedOrderParams.setSpbill_create_ip(spbill_create_ip); // 必须
		unifiedOrderParams.setTrade_type("NATIVE");// 必须
		unifiedOrderParams.setNotify_url(WechatConfig.NOTIFY_URL); // 异步通知URL
		// 统一下单 请求的Xml(除detail外不需要<![CDATA[product_001]]>格式)
		String unifiedXmL = wechatPayService
				.abstractPayToXml(unifiedOrderParams); // 签名并入service
		// logger.info("统一下单 请求的Xml"+unifiedXmL);
		// 统一下单 返回的xml(<![CDATA[product_001]]>格式)
		String unifiedResultXmL = HttpReqUtil.HttpsDefaultExecute(
				HttpReqUtil.POST_METHOD, WechatConfig.UNIFIED_ORDER_URL, null,
				unifiedXmL);
		// logger.info("统一下单 返回的xml("+unifiedResultXmL);
		// 统一下单返回 验证签名
		if (SignatureUtil.checkIsSignValidFromWeiXin(unifiedResultXmL)) {
			UnifiedOrderResult unifiedOrderResult = XmlUtil.getObjectFromXML(
					unifiedResultXmL, UnifiedOrderResult.class);
			if ("SUCCESS".equals(unifiedOrderResult.getReturn_code())
					&& "SUCCESS".equals(unifiedOrderResult.getResult_code())) {
				/**** 根据unifiedOrderResult.getCode_url()生成有效期为2小时的二维码 ****/

				/**** 根据product_id再次支付方法自己写 ****/
			}
		} else {
			logger.info("签名验证错误");
		}
		return null;
	}

	public static void main(String[] args) {
		String nonce_str = PayUtil.createNonceStr();
		String product_id = "product_001"; // 推荐根据商品ID生成
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", WechatConfig.APP_ID);
		packageParams.put("mch_id", WechatConfig.MCH_ID);
		packageParams.put("product_id", product_id);
		packageParams.put("time_stamp", PayUtil.createTimeStamp());
		packageParams.put("nonce_str", nonce_str);
		String str = PayUtil.createPayImageUrl(packageParams);
		String sign = SignatureUtil.createSign(packageParams,
				WechatConfig.API_KEY, SystemConfig.CHARACTER_ENCODING);
		packageParams.put("sign", sign);
		String payurl = "weixin://wxpay/bizpayurl?sign=" + sign + str;
		logger.info("payurl is " + payurl);
		/**** 转成短链接 ****/
		PayShortUrlParams payShortUrlParams = new PayShortUrlParams();
		payShortUrlParams.setAppid(WechatConfig.APP_ID);
		payShortUrlParams.setMch_id(WechatConfig.MCH_ID);
		payShortUrlParams.setLong_url(payurl);
		payShortUrlParams.setNonce_str(nonce_str);
		String urlSign = SignatureUtil.createSign(payShortUrlParams,
				WechatConfig.API_KEY, SystemConfig.CHARACTER_ENCODING);
		payShortUrlParams.setSign(urlSign);
		String longXml = XmlUtil.toSplitXml(payShortUrlParams);
		String shortResult = HttpReqUtil.HttpsDefaultExecute(
				HttpReqUtil.POST_METHOD, WechatConfig.PAY_SHORT_URL, null,
				longXml);
		PayShortUrlResult payShortUrlResult = XmlUtil.getObjectFromXML(
				shortResult, PayShortUrlResult.class);
		if ("SUCCESS".equals(payShortUrlResult.getReturn_code())) {
			payurl = payShortUrlResult.getShort_url();
			logger.info("payurl is " + payurl);
		}
		QRCodeGenerator handler = new QRCodeGenerator();
		handler.encoderQRCode(payurl,
				"F:/微信/" + "支付二维码" + DateTimeUtil.currentTime() + ".png", "png");

	}



	/**
	 * 该链接是通过【统一下单API】中提交的参数notify_url设置，如果链接无法访问，商户将无法接收到微信通知。
	 * 通知url必须为直接可访问的url
	 * ，不能携带参数。示例：notify_url：“https://pay.weixin.qq.com/wxpay/pay.action”
	 * 
	 * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
	 * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败
	 * ，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。
	 * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
	 * 注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
	 * 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过
	 * ，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前
	 * ，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
	 * 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 */
	public String payResult() throws Exception {

		logger.info("微信支付, callback payResult()");

		String resXml = "";
		Map<String, String> backxml = new HashMap<String, String>();

		InputStream inStream;
		try {
			inStream = request.getInputStream();

			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			logger.info("微信支付----付款成功----");
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			logger.info("微信支付----result----=" + result);
			Map<Object, Object> map = XmlUtil.parseXmlToMap(result);

			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				logger.info("微信支付----返回成功");
				 if (verifyWeixinNotify(map)) {
					// 订单处理 操作 orderconroller 的回写操作
					logger.info("微信支付----验证签名成功");
					
					// ====================================================================
					// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
					resXml = "<xml>"
							+ "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>"
							+ "</xml> ";

					// 处理业务 -修改订单支付信息
					logger.info("微信支付回调：修改的订单=" + map.get("out_trade_no"));
					
					String orderId = (String) map.get("out_trade_no");
					long order_id = Long.parseLong(orderId);
					String transaction_id = (String) map.get("transaction_id");  //wechat pay transaction id
					String time_end = (String) map.get("time_end"); //支付完成时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
					Timestamp paymtime = new Timestamp(sdf.parse(time_end).getTime());
					boolean ret = wechatPayService.updateOrderPaymentInfo(order_id,  HudOrderStatus.PAIED,transaction_id, paymtime);
	
					 if (ret) {
						 logger.info("微信支付回调：修改订单支付状态成功");
					 } else {
						 logger.info("微信支付回调：修改订单支付状态失败");
					 }

				}else{ //验证签名失败
					logger.info("微信支付----验证签名失败");
					resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							 + "<return_msg><![CDATA[验证签名失败]]></return_msg>" + "</xml> ";
				}
				// ------------------------------
				// 处理业务完毕
				// ------------------------------
				BufferedOutputStream out = new BufferedOutputStream(
						response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			}
			 else {
			 logger.info("支付失败,错误信息：" + map.get("err_code"));
			 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
			 + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("支付回调发布异常：" + e);
			e.printStackTrace();
		}
		return resXml;
	}
	
	
	/** 
     * 验证签名 
     *  
     * @param map 
     * @return 
     */  
    public boolean verifyWeixinNotify(Map<Object, Object> map) {  
        SortedMap<String, String> parameterMap = new TreeMap<String, String>();  
        String sign = (String) map.get("sign");  
        for (Object keyValue : map.keySet()) {  
            if (!keyValue.toString().equals("sign")) {  
        	logger.info("key:"+keyValue.toString()+", value:"+map.get(keyValue.toString()).toString());
                parameterMap.put(keyValue.toString(), map.get(keyValue.toString()).toString());  
            }  
        }  
        //String createSign = pay.getSign(parameterMap); 
        
		// get sign
		String createSign = SignatureUtil.createSignForSortedMap(parameterMap, WechatConfig.API_KEY,
				SystemConfig.CHARACTER_ENCODING);
		
        if (createSign.equals(sign)) {  
            return true;  
        } else {  
            logger.error("微信支付  验证签名失败, returnSign:"+sign+", verifySign:"+createSign);  
            return false;  
        }  
  
  
    }  
}