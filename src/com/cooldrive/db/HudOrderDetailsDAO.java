package com.cooldrive.db;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * HudOrderDetails entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.cooldrive.db.HudOrderDetails
 * @author MyEclipse Persistence Tools
 */
public class HudOrderDetailsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(HudOrderDetailsDAO.class);
	// property constants
	public static final String ORDER_ID = "orderId";
	public static final String PRODUCT_ID = "productId";
	public static final String UNIT_PRICE = "unitPrice";
	public static final String QUANTITY = "quantity";
	public static final String STATUS = "status";

	public void save(HudOrderDetails transientInstance) {
		log.debug("saving HudOrderDetails instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(HudOrderDetails persistentInstance) {
		log.debug("deleting HudOrderDetails instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HudOrderDetails findById(java.lang.Long id) {
		log.debug("getting HudOrderDetails instance with id: " + id);
		try {
			HudOrderDetails instance = (HudOrderDetails) getSession().get(
					"com.cooldrive.db.HudOrderDetails", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(HudOrderDetails instance) {
		log.debug("finding HudOrderDetails instance by example");
		try {
			List results = getSession()
					.createCriteria("com.cooldrive.db.HudOrderDetails")
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
		log.debug("finding HudOrderDetails instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from HudOrderDetails as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOrderId(Object orderId) {
		return findByProperty(ORDER_ID, orderId);
	}

	public List findByProductId(Object productId) {
		return findByProperty(PRODUCT_ID, productId);
	}

	public List findByUnitPrice(Object unitPrice) {
		return findByProperty(UNIT_PRICE, unitPrice);
	}

	public List findByQuantity(Object quantity) {
		return findByProperty(QUANTITY, quantity);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all HudOrderDetails instances");
		try {
			String queryString = "from HudOrderDetails";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public HudOrderDetails merge(HudOrderDetails detachedInstance) {
		log.debug("merging HudOrderDetails instance");
		try {
			HudOrderDetails result = (HudOrderDetails) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(HudOrderDetails instance) {
		log.debug("attaching dirty HudOrderDetails instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HudOrderDetails instance) {
		log.debug("attaching clean HudOrderDetails instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}