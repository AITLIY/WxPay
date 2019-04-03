package com.cooldrive.db;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * HudOrders entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.cooldrive.db.HudOrders
 * @author MyEclipse Persistence Tools
 */
public class HudOrdersDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(HudOrdersDAO.class);
	// property constants
	public static final String PREPAY_ID = "prepayId";
	public static final String TRANSACTION_ID = "transactionId";
	public static final String CLIENT_ID = "clientId";
	public static final String ORDER_CHANNEL = "orderChannel";
	public static final String TOTAL_AMOUNT = "totalAmount";
	public static final String DISCOUNT_AMOUNT = "discountAmount";
	public static final String PAY_AMOUNT = "payAmount";
	public static final String DELIVER_USER_NAME = "deliverUserName";
	public static final String DELIVER_ADDRESS = "deliverAddress";
	public static final String DELIVER_PHONE = "deliverPhone";
	public static final String DELIVER_COMPANY_ID = "deliverCompanyId";
	public static final String DELIVER_CODE = "deliverCode";
	public static final String DELIVER_FEE = "deliverFee";
	public static final String INVOICE_ID = "invoiceId";
	public static final String ORDER_STATUS = "orderStatus";

	public void save(HudOrders transientInstance) {
		log.debug("saving HudOrders instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(HudOrders persistentInstance) {
		log.debug("deleting HudOrders instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HudOrders findById(java.lang.Long id) {
		log.debug("getting HudOrders instance with id: " + id);
		try {
			HudOrders instance = (HudOrders) getSession().get(
					"com.cooldrive.db.HudOrders", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(HudOrders instance) {
		log.debug("finding HudOrders instance by example");
		try {
			List results = getSession()
					.createCriteria("com.cooldrive.db.HudOrders")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding HudOrders instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from HudOrders as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPrepayId(Object prepayId) {
		return findByProperty(PREPAY_ID, prepayId);
	}

	public List findByTransactionId(Object transactionId) {
		return findByProperty(TRANSACTION_ID, transactionId);
	}

	public List findByClientId(Object clientId) {
		return findByProperty(CLIENT_ID, clientId);
	}

	public List findByOrderChannel(Object orderChannel) {
		return findByProperty(ORDER_CHANNEL, orderChannel);
	}

	public List findByTotalAmount(Object totalAmount) {
		return findByProperty(TOTAL_AMOUNT, totalAmount);
	}

	public List findByDiscountAmount(Object discountAmount) {
		return findByProperty(DISCOUNT_AMOUNT, discountAmount);
	}

	public List findByPayAmount(Object payAmount) {
		return findByProperty(PAY_AMOUNT, payAmount);
	}

	public List findByDeliverUserName(Object deliverUserName) {
		return findByProperty(DELIVER_USER_NAME, deliverUserName);
	}

	public List findByDeliverAddress(Object deliverAddress) {
		return findByProperty(DELIVER_ADDRESS, deliverAddress);
	}

	public List findByDeliverPhone(Object deliverPhone) {
		return findByProperty(DELIVER_PHONE, deliverPhone);
	}

	public List findByDeliverCompanyId(Object deliverCompanyId) {
		return findByProperty(DELIVER_COMPANY_ID, deliverCompanyId);
	}

	public List findByDeliverCode(Object deliverCode) {
		return findByProperty(DELIVER_CODE, deliverCode);
	}

	public List findByDeliverFee(Object deliverFee) {
		return findByProperty(DELIVER_FEE, deliverFee);
	}

	public List findByInvoiceId(Object invoiceId) {
		return findByProperty(INVOICE_ID, invoiceId);
	}

	public List findByOrderStatus(Object orderStatus) {
		return findByProperty(ORDER_STATUS, orderStatus);
	}

	public List findAll() {
		log.debug("finding all HudOrders instances");
		try {
			String queryString = "from HudOrders";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public HudOrders merge(HudOrders detachedInstance) {
		log.debug("merging HudOrders instance");
		try {
			HudOrders result = (HudOrders) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(HudOrders instance) {
		log.debug("attaching dirty HudOrders instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HudOrders instance) {
		log.debug("attaching clean HudOrders instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}