package com.itheima.bos.service;

import com.itheima.bos.domain.User;
import com.itheima.bos.utils.PageBean;

public interface IUserService {

	// 登录
	User login(User model);

	//修改密码
	void editPassword(String id, String password);

	void save(User model, String[] roleIds);

	void pageQuery(PageBean pageBean);

}
