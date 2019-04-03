/**
 * 
 */
package com.cooldrive.wechatpay.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.cooldrive.HibernateSessionFactory;
import com.cooldrive.common.config.SystemConfig;
import com.cooldrive.common.config.WechatConfig;
import com.cooldrive.common.util.SignatureUtil;
import com.cooldrive.common.util.XmlUtil;
import com.cooldrive.db.HudOrderClients;
import com.cooldrive.db.HudOrderClientsDAO;
import com.cooldrive.db.HudOrderDeliverCompany;
import com.cooldrive.db.HudOrderDeliverCompanyDAO;
import com.cooldrive.db.HudOrderDetails;
import com.cooldrive.db.HudOrderDetailsDAO;
import com.cooldrive.db.HudOrderProducts;
import com.cooldrive.db.HudOrderProductsDAO;
import com.cooldrive.db.HudOrders;
import com.cooldrive.db.HudOrdersDAO;
import com.cooldrive.entity.HudOrderInfo;
import com.cooldrive.struts.WechatPayAction;
import com.cooldrive.wechatpay.model.rep.AbstractPayParams;
import com.cooldrive.wechatpay.service.WechatPayService;

/**
 * @author phil
 * @date  2017年7月25日
 *
 */
@Service
public class WechatPayServiceImpl implements WechatPayService {
	
	private static final Logger logger = Logger
			.getLogger(WechatPayServiceImpl.class);
	
	private HudOrdersDAO hudOrdersDao;
	private HudOrderDetailsDAO hudOrderDetailsDao;
	private HudOrderClientsDAO hudOrderClientDao;
	private HudOrderProductsDAO hudOrderProductsDao;
	private HudOrderDeliverCompanyDAO hudOrderDeliverCompanyDao;
	
	
	public HudOrderDeliverCompanyDAO getHudOrderDeliverCompanyDao() {
		return hudOrderDeliverCompanyDao;
	}

	public void setHudOrderDeliverCompanyDao(
			HudOrderDeliverCompanyDAO hudOrderDeliverCompanyDao) {
		this.hudOrderDeliverCompanyDao = hudOrderDeliverCompanyDao;
	}

	public HudOrderClientsDAO getHudOrderClientDao() {
		return hudOrderClientDao;
	}

	public void setHudOrderClientDao(HudOrderClientsDAO hudOrderClientDao) {
		this.hudOrderClientDao = hudOrderClientDao;
	}

	
	
	public HudOrderProductsDAO getHudOrderProductsDao() {
		return hudOrderProductsDao;
	}

	public void setHudOrderProductsDao(HudOrderProductsDAO hudOrderProductsDao) {
		this.hudOrderProductsDao = hudOrderProductsDao;
	}

	public HudOrderClientsDAO getHudClientDao() {
		return hudOrderClientDao;
	}

	public void setHudClientDao(HudOrderClientsDAO hudClientDao) {
		this.hudOrderClientDao = hudClientDao;
	}

	public HudOrdersDAO getHudOrdersDao() {
		return hudOrdersDao;
	}

	public void setHudOrdersDao(HudOrdersDAO hudOrdersDao) {
		this.hudOrdersDao = hudOrdersDao;
	}

	public HudOrderDetailsDAO getHudOrderDetailsDao() {
		return hudOrderDetailsDao;
	}

	public void setHudOrderDetailsDao(HudOrderDetailsDAO hudOrderDetailsDao) {
		this.hudOrderDetailsDao = hudOrderDetailsDao;
	}

	
	
	public String abstractPayToXml(AbstractPayParams params){
		String sign = SignatureUtil.createSign(params, WechatConfig.API_KEY, SystemConfig.CHARACTER_ENCODING);
		params.setSign(sign);
		return XmlUtil.toSplitXml(params);
	}
	
	
	@SuppressWarnings("unchecked")
	public List <HudOrderProducts> getOrderProducts(){
		
		// return (List <HudOrderProducts> ) hudOrderProductsDao.findAll();
		
		Session session = HibernateSessionFactory.getSession();
		try {
			String queryString = "select * from hud_order_products as p where p.status = 1 " ;
					
			SQLQuery queryObject = session.createSQLQuery(queryString);
			List <HudOrderProducts> products = queryObject.addEntity(HudOrderProducts.class).list();
			
			return products;
			
		}catch(Exception ex)
		{
			logger.info(ex.getMessage());
			return null;		
		}finally{
			session.close();
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List <HudOrderDeliverCompany> getOrderDeliverCompanies(){
		
		//return (List <HudOrderDeliverCompany> ) hudOrderDeliverCompanyDao.findAll();
		
		Session session = HibernateSessionFactory.getSession();
		try {
			String queryString = "select * from hud_order_deliver_company as c where c.status = 1 " ;
					
			SQLQuery queryObject = session.createSQLQuery(queryString);
			List <HudOrderDeliverCompany> companies = queryObject.addEntity(HudOrderDeliverCompany.class).list();
			
			return companies;
			
		}catch(Exception ex)
		{
			logger.info(ex.getMessage());
			return null;		
		}finally{
			session.close();
		}
		
	}
	
	//get order details 
	public List getOrderDetails(long orderId){
		
		//return (List <HudOrderDeliverCompany> ) hudOrderDeliverCompanyDao.findAll();
		
		Session session = HibernateSessionFactory.getSession();
		try {
			String queryString = "select p.product_name, p.discription, d.unit_price, d.quantity" +
					" from hud_order_details d" +
					" join hud_order_products p on d.product_id = p.product_id" +
					"  where d.status = 1 and d.order_id = ?" ;
					
			SQLQuery queryObject = session.createSQLQuery(queryString);
			queryObject.setParameter(0, orderId);
			
			List orderdetails = queryObject.list();
			
			return orderdetails;
			
		}catch(Exception ex)
		{
			logger.info(ex.getMessage());
			return null;		
		}finally{
			session.close();
		}
		
	}

	
	
	//get order by order id code
	public HudOrderInfo getOrderByOrderId(long order_id){
			
		Session session = HibernateSessionFactory.getSession();
		try {
			
			String queryString = "select o.order_id, o.prepay_id, o.transaction_id, o.client_id," +
					" o.order_channel, o.total_amount, o.discount_amount, o.pay_amount, " +
					" o.deliver_user_name, o.deliver_address, o.deliver_phone, o.deliver_company_id, " +
					" c.company_name, c.company_code, o.deliver_code, o.deliver_fee, o.invoice_id , " +
					" o.order_time, o. payment_time, o.order_status " +
					" from hud_orders o  " +
					" join hud_order_deliver_company c " +
					" on o.deliver_company_id = c.company_id" +
					" where o.order_id = ? " ;
					
			SQLQuery queryObject = session.createSQLQuery(queryString);
			queryObject.setParameter(0, order_id);
			
			List  orders = queryObject.list();
			
			if(orders.size() > 0){
				Object [] order = (Object []) orders.get(0);
				HudOrderInfo orderInfo = new HudOrderInfo();
				orderInfo.setOrderId(((BigInteger)order[0]).longValue());
				orderInfo.setPrepayId((String)order[1]);
				orderInfo.setTransactionId((String)order[2]);
				orderInfo.setClientId((Integer)order[3]);
				orderInfo.setOrderChannel((Short)order[4]);
				
				orderInfo.setTotalAmount(((BigDecimal)order[5]).doubleValue());
				orderInfo.setDiscountAmount(((BigDecimal)order[6]).doubleValue());
				orderInfo.setPayAmount(((BigDecimal)order[7]).doubleValue());
				orderInfo.setDeliverUserName((String)order[8]);
				orderInfo.setDeliverAddress((String)order[9]);
				orderInfo.setDeliverPhone((String)order[10]);
				orderInfo.setDeliverCompanyId((Short)order[11]);
				orderInfo.setDeliverCompanyName((String)order[12]);
				orderInfo.setDeliverCompanyCode((String)order[13]);
				orderInfo.setDeliverCode((String)order[14]);
				orderInfo.setDeliverFee(((BigDecimal)order[15]).doubleValue());
				orderInfo.setInvoiceId((Integer)order[16]);
				orderInfo.setOrderTime((Timestamp)order[17]);
				orderInfo.setPaymentTime((Timestamp)order[18]);
				orderInfo.setOrderStatus(((Byte)order[19]).shortValue());
				
				return orderInfo;
			}else{
				return null;
			}
			
		}catch(Exception ex)
		{
			logger.info(ex.getMessage());
			return null;		
		}finally{
			session.close();
		}
	}
		
		
	// get orders by client code
	public List<HudOrders> getOrdersByClientCode(String clientCode) {

		Session session = HibernateSessionFactory.getSession();
		try {
			String queryString = "select * from hud_order_clients c  " +
					" where c.client_code = ?";

			SQLQuery queryObject = session.createSQLQuery(queryString);
			queryObject.setParameter(0, clientCode);
			
			List<HudOrderClients> clients = queryObject.addEntity(
					HudOrderClients.class).list();

			if (clients==null || clients.size() == 0){
				logger.info("Client ["+clientCode+"] cannot be found");
				return null;
			}

			HudOrderClients client = (HudOrderClients) clients.get(0);

			queryString = "select * from hud_orders o  where client_id= ?";

			queryObject = session.createSQLQuery(queryString);
			queryObject.setParameter(0, client.getClientId());

			List<HudOrders> orders = queryObject.addEntity(HudOrders.class)
					.list();

			return orders;

		} catch (Exception ex) {
			logger.info(ex.getMessage());
			return null;
		} finally {
			session.close();
		}
	}
	
	
	//create a new order
	public HudOrders createOrder(String openId,
			List<HudOrderDetails> orderDetails, int order_channel,
			String deliver_user, String deliver_address, String deliver_phone,
			int deliver_company) {

		logger.info("get session");
		
		Session session = HibernateSessionFactory.getSession();

		try {

			logger.info("new order");
			HudOrders order = new HudOrders();

			logger.info("get client by openid:"+openId);
			
			@SuppressWarnings("unchecked")
			List<HudOrderClients> clients = (List<HudOrderClients>) hudOrderClientDao.findByClientCode(openId);

			session.beginTransaction();

			HudOrderClients client = null;

			if (clients==null || clients.size() == 0) { // create new client
				client = new HudOrderClients();
				client.setClientCode(openId);
				client.setCreateTime(new Timestamp(System.currentTimeMillis()));
				
				logger.info("save new client");
				hudOrderClientDao.save(client);
			} else { //existed client
				logger.info("get client");
				client = (HudOrderClients) clients.get(0);
			}

			order.setClientId(client.getClientId());
			order.setDeliverAddress(deliver_address);
			order.setDeliverCompanyId((short) deliver_company);
			order.setDeliverPhone(deliver_phone);
			order.setDeliverUserName(deliver_user);
			order.setOrderChannel((short) order_channel);
			order.setTotalAmount(0.0);  //set dummy value, need to update later
			 
			//get delivery fee
			double deliverFee = 0.0f;
			HudOrderDeliverCompany company = hudOrderDeliverCompanyDao.findById(deliver_company);
			 if(company != null){
				 deliverFee = company.getDeliverFee();
			 }
			order.setDeliverFee(deliverFee);
			
			order.setOrderTime(new Timestamp(System.currentTimeMillis()));
			order.setOrderStatus((short)1);
			
			logger.info("save order");
			hudOrdersDao.save(order);

			// List<HudOrderDetails> orderDetails
			float total_amount = 0.0f;
			for (HudOrderDetails detail : orderDetails) {
				int productId = detail.getProductId();
				// fetch product unit price
				HudOrderProducts prod = hudOrderProductsDao.findById(productId);

				total_amount += prod.getUnitPrice()* detail.getQuantity();

				HudOrderDetails orderDetail = new HudOrderDetails();

				orderDetail.setOrderId(order.getOrderId());
				orderDetail.setProductId(detail.getProductId());
				orderDetail.setQuantity(detail.getQuantity());
				orderDetail.setUnitPrice(prod.getUnitPrice());
				orderDetail.setStatus((short) 1);

				logger.info("save order detail");
				hudOrderDetailsDao.save(orderDetail);
			}

			BigDecimal b = new BigDecimal(String.valueOf(total_amount));  
			double totalAmount = b.doubleValue(); 
			order.setTotalAmount( totalAmount);
			double discountAmount = 0.0;
			order.setDiscountAmount(discountAmount);
			
			double payAmount = totalAmount + order.getDeliverFee() - discountAmount;
			order.setPayAmount(payAmount);
			
			logger.info("save final order");
			
			hudOrdersDao.merge(order);

			session.getTransaction().commit();

			return order;

		} catch (RuntimeException re) {

			logger.info(re.getMessage());
			session.getTransaction().rollback();
			return null;
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			session.getTransaction().rollback();
			return null;
		} finally {

			session.close();
		}

	}
	
	
	// update Order prepay_id
		public boolean updateOrderPrepayId(long orderId, String prepay_id) {

			logger.info("get session");

			Session session = HibernateSessionFactory.getSession();

			try {

				HudOrders order = hudOrdersDao.findById(orderId);

				order.setPrepayId(prepay_id);

				logger.info("update order status to 2");

				session.getTransaction().begin();
				hudOrdersDao.merge(order);

				session.getTransaction().commit();

				return true;

			} catch (RuntimeException re) {

				logger.info(re.getMessage());
				session.getTransaction().rollback();
				return false;
			} catch (Exception ex) {
				logger.info(ex.getMessage());
				session.getTransaction().rollback();
				return false;
			} finally {

				session.close();
			}

		}
		
	//update order status
	public boolean updateOrderPaymentInfo(long orderId, int status, String transaction_id, Timestamp paytime) {

		logger.info("get session");

		Session session = HibernateSessionFactory.getSession();

		try {

			HudOrders order = hudOrdersDao.findById(orderId);

			order.setOrderStatus((short) status); 
			order.setTransactionId(transaction_id); //微信支付订单号
			order.setPaymentTime(paytime); //付款时间
			order.setOrderChannel((short)1); //微信支付

			session.getTransaction().begin();
			hudOrdersDao.merge(order);

			session.getTransaction().commit();

			return true;

		} catch (RuntimeException re) {

			logger.info(re.getMessage());
			session.getTransaction().rollback();
			return false;
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			session.getTransaction().rollback();
			return false;
		} finally {

			session.close();
		}

	}

	//cancel order
		public boolean cancelOrder(long orderId) {

			logger.info("get session");

			Session session = HibernateSessionFactory.getSession();

			try {

				HudOrders order = hudOrdersDao.findById(orderId);
				
				if(order.getOrderStatus() == 1){  //未付款订单才可以取消
					order.setOrderStatus((short) 0); 
	
					session.getTransaction().begin();
					hudOrdersDao.merge(order);
	
					session.getTransaction().commit();
					
				}else{
					return false;
				}

				return true;

			} catch (RuntimeException re) {

				logger.info(re.getMessage());
				session.getTransaction().rollback();
				return false;
			} catch (Exception ex) {
				logger.info(ex.getMessage());
				session.getTransaction().rollback();
				return false;
			} finally {

				session.close();
			}  

		}
}
