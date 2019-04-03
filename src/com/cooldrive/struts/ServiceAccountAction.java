package com.cooldrive.struts;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cooldrive.common.util.MD5Util;
import com.cooldrive.common.util.SignatureUtil;
import com.cooldrive.common.util.XmlUtil;
import com.cooldrive.entity.Article;
import com.cooldrive.entity.LinkMessage;
import com.cooldrive.entity.NewsMessage;
import com.cooldrive.entity.TextMessage;
import com.cooldrive.enums.WechatMessageType;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import net.sf.json.JSONObject;

public class ServiceAccountAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	// service account messae callback
	public void WXMessageCallback() {

		
//		NewsMessage message1 = new NewsMessage();
//		message1.setMsgType(WechatMessageType.MESSAGE_NEWS);
//		message1.setToUserName("");
//		message1.setFromUserName("");
//		message1.setCreateTime(System.currentTimeMillis());
//		
//		String Location_X1 = "30";
//		String Location_Y1 = "121";
//		String param1 = Location_X1+","+Location_Y1+","+"5";
//		String latlonzoom1="";
//		try {
//			latlonzoom1 = Base64.encode(param1.getBytes("UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//		
//	    String url1 = "http://www.cooldrivehud.com:18080/HUDServer/cooldrive_geo.html?latlonzoom="+
//	    		latlonzoom1+"&lat="+Location_X1+"&lon="+Location_Y1;
//	    logger.info("url:"+url1);
//	    
//	    Article article1 = new Article();
//	    article1.setPicUrl("");
//	    article1.setUrl(url1);
//	    article1.setTitle("位置信息");
//	    article1.setDescription("查看位置并导航");
//
//	    message1.setArticleCount(1);
//	    
//	    List<Article> articles1=new ArrayList<Article>();
//	    articles1.add(article1);
//	    
//		    
//	    message1.setArticles(articles1);
//		String test = XmlUtil.newsMessageToXml(message1);
//		logger.info("test:" + test);

		
		String method = request.getMethod();
		logger.info("WXMessageCallback, method:" + method);

		// 默认回复一个"success"
		String responseMessage = "success";
		PrintWriter out = null;
		try {
			out = new PrintWriter(response.getOutputStream());

			if (method.equals("GET")) {// 注意全部大写
				String signature = request.getParameter("signature");
				String timestamp = request.getParameter("timestamp");
				String nonce = request.getParameter("nonce");
				String echostr = request.getParameter("echostr");
				
				logger.info("check signature:"+signature+","+timestamp+","+nonce+","+echostr);
				

				// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
				if (SignatureUtil.checkSignature(signature, timestamp, nonce)) {
					logger.info("check signature success");
					out.print(echostr);
				} else {
					logger.info("check signature failed");
					out.print(responseMessage);
				}
				
				out.close();
				out = null;
				return;

			} else {  //POST

				 // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		        request.setCharacterEncoding("UTF-8");
		        response.setCharacterEncoding("UTF-8");
		        
				Map<String, String> map = XmlUtil.parseXmlToMap(request);
				logger.info("get message:"+map.toString());
				// 发送方帐号（一个OpenID）
				String fromUserName = map.get("FromUserName");
				// 开发者微信号
				String toUserName = map.get("ToUserName");
				// 消息类型
				String msgType = map.get("MsgType");
				// 对消息进行处理

				if (WechatMessageType.MESSAGE_TEXT.equals(msgType)) {// 文本消息
					TextMessage textMessage = new TextMessage();
					textMessage.setMsgType(WechatMessageType.MESSAGE_TEXT);
					textMessage.setToUserName(fromUserName);
					textMessage.setFromUserName(toUserName);
					textMessage.setCreateTime(System.currentTimeMillis());
					textMessage.setContent("我已经收到你发来的消息了");

					responseMessage = XmlUtil.toXml(textMessage);
				} else if (WechatMessageType.MESSAGE_LOCATION.equals(msgType)) {
					NewsMessage message = new NewsMessage();
					message.setMsgType(WechatMessageType.MESSAGE_NEWS);
					message.setToUserName(fromUserName);
					message.setFromUserName(toUserName);
					message.setCreateTime(System.currentTimeMillis());
					
					String Location_X = map.get("Location_X");
					String Location_Y = map.get("Location_Y");
					String Label = map.get("Label");
					String param = Location_X+","+Location_Y+","+"5";
					String latlonzoom= Base64.encode(param.getBytes("UTF-8"));  
					
				    String url = "http://www.cooldrivehud.com:18080/HUDServer/cooldrive_geo.html?latlonzoom="+
				    		latlonzoom+"&lat="+Location_X+"&lon="+Location_Y;
				    logger.info("url:"+url);
				    
				    Article article = new Article();
				    article.setPicUrl("http://wx.cooldrivenavi.com/WxPay/img/hudnavi.png");
					article.setUrl(url);
				    article.setTitle(Label);
				    article.setDescription("点击在酷驾地图中查看位置并导航");

				    message.setArticleCount(1);
				    
				    List<Article> articles=new ArrayList<Article>();
				    articles.add(article);
				    
				    message.setArticles(articles);
				    
					responseMessage = XmlUtil.newsMessageToXml(message);
				}

				logger.info("response message:"+responseMessage);
				out.print(responseMessage);
				out.close();
				out = null;

				return;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("接收微信消息失败:" + ex.getMessage());
			if (out != null) {
				out.print(responseMessage);
				out.close();
			}
			return;
		}
	}

}
