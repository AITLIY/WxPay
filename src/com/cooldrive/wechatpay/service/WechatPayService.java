/**
 * 
 */
package com.cooldrive.wechatpay.service;

import com.cooldrive.wechatpay.model.rep.AbstractPayParams;

/**
 * @author phil
 * @date  2017年7月25日
 *
 */
public interface WechatPayService {
	
	public String abstractPayToXml(AbstractPayParams params);
}
