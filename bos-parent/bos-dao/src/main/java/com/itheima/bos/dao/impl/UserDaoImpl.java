package com.itheima.bos.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.itheima.bos.dao.IUserDao;
import com.itheima.bos.dao.base.impl.BaseDaoImpl;
import com.itheima.bos.domain.User;

@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao {

	// 根据用户名和密码查询用户

	public User findUserByUsernameAndPassword(String username, String password) {
		String hql = "FROM User u where u.username = ? AND u.password = ?";
		List<User> list = (List<User>) this.getHibernateTemplate().find(hql, username, password);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public User findUserByUsername(String username) {
		String hql = "FROM User u where u.username = ?";
		List<User> list = (List<User>) this.getHibernateTemplate().find(hql, username);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
