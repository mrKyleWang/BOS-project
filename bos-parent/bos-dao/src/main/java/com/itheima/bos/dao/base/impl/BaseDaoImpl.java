package com.itheima.bos.dao.base.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.itheima.bos.dao.base.IBaseDao;
import com.itheima.bos.utils.PageBean;

/**
 * 持久层通用实现
 * 
 * @author wking
 *
 * @param <T>
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class BaseDaoImpl<T> extends HibernateDaoSupport implements IBaseDao<T> {

	// 代表某个实体的类型
	private Class<T> entityClass;

	@Resource // 根据类型注入spring工程中的会话工厂对象
	public void setMySessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	// 在父类(BaseDaoImpl)的构造方法中动态获得entityClass
	public BaseDaoImpl() {
		ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 获得父类上声明的泛型数组
		entityClass = (Class<T>) superclass.getActualTypeArguments()[0];
	}

	@Override
	public void save(T entity) {
		this.getHibernateTemplate().save(entity);
	}

	@Override
	public void delete(T entity) {
		this.getHibernateTemplate().delete(entity);
	}

	@Override
	public void update(T entity) {
		this.getHibernateTemplate().update(entity);
	}

	@Override
	public T findById(Serializable id) {
		return this.getHibernateTemplate().get(entityClass, id);
	}

	@Override
	public List<T> findAll() {
		String hql = "FROM " + entityClass.getSimpleName();
		return (List<T>) this.getHibernateTemplate().find(hql);
	}

	// 执行更新,queryName在元数据中配置
	@Override
	public void executeUpdate(String queryName, Object... objects) {
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery(queryName);
		int i = 0;
		// 为HQL语句中的?赋值
		for (Object object : objects) {
			query.setParameter(i++, object);
		}
		// 执行更新
		query.executeUpdate();
	}

	// 通用分页查询方法
	@Override
	public void pageQuery(PageBean pageBean) {
		int currentPage = pageBean.getCurrentPage();
		int pageSize = pageBean.getPageSize();
		DetachedCriteria detachedCriteria = pageBean.getDetachedCriteria();

		// 查询total--总数据量
		detachedCriteria.setProjection(Projections.rowCount()); // 设置查询聚合函数
		List<Long> countList = (List<Long>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
		pageBean.setTotal(countList.get(0).intValue());

		// 查询rows--当前页需要展示的数据集合
		detachedCriteria.setProjection(null); // 清空聚合函数
		
		//指定hibernate框架封装对象的方式
		detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		
		int firstResult = (currentPage - 1) * pageSize;
		int maxResults = pageSize;
		List rows = this.getHibernateTemplate().findByCriteria(detachedCriteria, firstResult, maxResults);
		pageBean.setRows(rows);
	}

	
	//添加或更新,根据主键是否重复判断
	@Override
	public void saveOrUpdate(T entity) {
		this.getHibernateTemplate().saveOrUpdate(entity);
	}

	//根据离线查询条件查询
	@Override
	public List<T> findByCriteria(DetachedCriteria detachedCriteria) {
		return (List<T>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
	}

}
