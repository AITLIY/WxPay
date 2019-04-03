package com.cooldrive.db;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * HudOrderProducts entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.cooldrive.db.HudOrderProducts
 * @author MyEclipse Persistence Tools
 */
public class HudOrderProductsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(HudOrderProductsDAO.class);
	// property constants
	public static final String PRODUCT_CODE = "productCode";
	public static final String PRODUCT_NAME = "productName";
	public static final String DISCRIPTION = "discription";
	public static final String UNIT_PRICE = "unitPrice";
	public static final String STATUS = "status";

	public void save(HudOrderProducts transientInstance) {
		log.debug("saving HudOrderProducts instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(HudOrderProducts persistentInstance) {
		log.debug("deleting HudOrderProducts instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HudOrderProducts findById(java.lang.Integer id) {
		log.debug("getting HudOrderProducts instance with id: " + id);
		try {
			HudOrderProducts instance = (HudOrderProducts) getSession().get(
					"com.cooldrive.db.HudOrderProducts", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(HudOrderProducts instance) {
		log.debug("finding HudOrderProducts instance by example");
		try {
			List results = getSession()
					.createCriteria("com.cooldrive.db.HudOrderProducts")
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
		log.debug("finding HudOrderProducts instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from HudOrderProducts as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByProductCode(Object productCode) {
		return findByProperty(PRODUCT_CODE, productCode);
	}

	public List findByProductName(Object productName) {
		return findByProperty(PRODUCT_NAME, productName);
	}

	public List findByDiscription(Object discription) {
		return findByProperty(DISCRIPTION, discription);
	}

	public List findByUnitPrice(Object unitPrice) {
		return findByProperty(UNIT_PRICE, unitPrice);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all HudOrderProducts instances");
		try {
			String queryString = "from HudOrderProducts";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public HudOrderProducts merge(HudOrderProducts detachedInstance) {
		log.debug("merging HudOrderProducts instance");
		try {
			HudOrderProducts result = (HudOrderProducts) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(HudOrderProducts instance) {
		log.debug("attaching dirty HudOrderProducts instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HudOrderProducts instance) {
		log.debug("attaching clean HudOrderProducts instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}