package com.cooldrive.db;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * HudOrderDeliverCompany entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.cooldrive.db.HudOrderDeliverCompany
 * @author MyEclipse Persistence Tools
 */
public class HudOrderDeliverCompanyDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(HudOrderDeliverCompanyDAO.class);
	// property constants
	public static final String COMPANY_NAME = "companyName";
	public static final String DELIVER_FEE = "deliverFee";
	public static final String STATUS = "status";
	public static final String COMPANY_CODE = "companyCode";

	public void save(HudOrderDeliverCompany transientInstance) {
		log.debug("saving HudOrderDeliverCompany instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(HudOrderDeliverCompany persistentInstance) {
		log.debug("deleting HudOrderDeliverCompany instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HudOrderDeliverCompany findById(java.lang.Integer id) {
		log.debug("getting HudOrderDeliverCompany instance with id: " + id);
		try {
			HudOrderDeliverCompany instance = (HudOrderDeliverCompany) getSession()
					.get("com.cooldrive.db.HudOrderDeliverCompany", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(HudOrderDeliverCompany instance) {
		log.debug("finding HudOrderDeliverCompany instance by example");
		try {
			List results = getSession()
					.createCriteria("com.cooldrive.db.HudOrderDeliverCompany")
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
		log.debug("finding HudOrderDeliverCompany instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from HudOrderDeliverCompany as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCompanyName(Object companyName) {
		return findByProperty(COMPANY_NAME, companyName);
	}

	public List findByDeliverFee(Object deliverFee) {
		return findByProperty(DELIVER_FEE, deliverFee);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCompanyCode(Object companyCode) {
		return findByProperty(COMPANY_CODE, companyCode);
	}

	public List findAll() {
		log.debug("finding all HudOrderDeliverCompany instances");
		try {
			String queryString = "from HudOrderDeliverCompany";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public HudOrderDeliverCompany merge(HudOrderDeliverCompany detachedInstance) {
		log.debug("merging HudOrderDeliverCompany instance");
		try {
			HudOrderDeliverCompany result = (HudOrderDeliverCompany) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(HudOrderDeliverCompany instance) {
		log.debug("attaching dirty HudOrderDeliverCompany instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HudOrderDeliverCompany instance) {
		log.debug("attaching clean HudOrderDeliverCompany instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}