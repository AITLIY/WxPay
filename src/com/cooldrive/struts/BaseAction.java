package com.cooldrive.struts;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.cooldrive.wechatpay.model.resp.JsPayResult;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements
ServletRequestAware, ServletResponseAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected HttpServletRequest request;  
	protected HttpServletResponse response;
	
	protected static final Logger logger = Logger
			.getLogger(BaseAction.class);
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}


	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

	public static void responseStringResult(HttpServletResponse response,
			String strResult) throws IOException {
		byte[] resBytes = strResult.getBytes("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setContentLength(resBytes.length);
		response.getOutputStream().write(resBytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	public static void responseJsonResult(HttpServletResponse response,
			JSONObject json) throws IOException {
		byte[] jsonBytes = json.toString().getBytes("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setContentLength(jsonBytes.length);
		response.getOutputStream().write(jsonBytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	public static void responseJsonResult(HttpServletResponse response,
			JsPayResult json) throws IOException {
		byte[] jsonBytes = json.toString().getBytes("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setContentLength(jsonBytes.length);
		response.getOutputStream().write(jsonBytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
    
	
}
