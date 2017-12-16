package com.itheima.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IUserDao;
import com.itheima.bos.domain.Role;
import com.itheima.bos.domain.User;
import com.itheima.bos.service.IUserService;
import com.itheima.bos.utils.MD5Utils;
import com.itheima.bos.utils.PageBean;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;
	
	//用户登录
	@Override
	public User login(User user) {
		//使用MD5加密密码
		String password = MD5Utils.md5(user.getPassword());
		return userDao.findUserByUsernameAndPassword(user.getUsername(),password);
	}

	//根据用户id修改密码
	@Override
	public void editPassword(String id, String password) {
		//使用MD5加密密码
		password = MD5Utils.md5(password);
		userDao.executeUpdate("user.editpassword",password,id);
	}

	//添加一个用户,同时关联角色
	@Override
	public void save(User model, String[] roleIds) {
		userDao.save(model);
		if(roleIds !=null && roleIds.length>0) {
			for (String roleId : roleIds) {
				//手动构造托管对象
				Role role = new Role(roleId);
				model.getRoles().add(role);
			}
		}
	}

	//分页查询
	@Override
	public void pageQuery(PageBean pageBean) {
		userDao.pageQuery(pageBean);
	}

}
