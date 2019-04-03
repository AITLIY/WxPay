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
 * HudOrderClients entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.cooldrive.db.HudOrderClients
 * @author MyEclipse Persistence Tools
 */
public class HudOrderClientsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(HudOrderClientsDAO.class);
	// property constants
	public static final String CLIENT_CODE = "clientCode";
	public static final String CLIENT_NAME = "clientName";
	public static final String STATUS = "status";

	public void save(HudOrderClients transientInstance) {
		log.debug("saving HudOrderClients instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(HudOrderClients persistentInstance) {
		log.debug("deleting HudOrderClients instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HudOrderClients findById(java.lang.Integer id) {
		log.debug("getting HudOrderClients instance with id: " + id);
		try {
			HudOrderClients instance = (HudOrderClients) getSession().get(
					"com.cooldrive.db.HudOrderClients", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(HudOrderClients instance) {
		log.debug("finding HudOrderClients instance by example");
		try {
			List results = getSession()
					.createCriteria("com.cooldrive.db.HudOrderClients")
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
		log.debug("finding HudOrderClients instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from HudOrderClients as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByClientCode(Object clientCode) {
		return findByProperty(CLIENT_CODE, clientCode);
	}

	public List findByClientName(Object clientName) {
		return findByProperty(CLIENT_NAME, clientName);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all HudOrderClients instances");
		try {
			String queryString = "from HudOrderClients";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public HudOrderClients merge(HudOrderClients detachedInstance) {
		log.debug("merging HudOrderClients instance");
		try {
			HudOrderClients result = (HudOrderClients) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(HudOrderClients instance) {
		log.debug("attaching dirty HudOrderClients instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HudOrderClients instance) {
		log.debug("attaching clean HudOrderClients instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}