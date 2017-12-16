package com.itheima.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IFunctionDao;
import com.itheima.bos.domain.Function;
import com.itheima.bos.domain.User;
import com.itheima.bos.service.IFunctionService;
import com.itheima.bos.utils.BOSUtils;
import com.itheima.bos.utils.PageBean;

@Service
@Transactional
public class FunctionServiceImpl implements IFunctionService {

	@Autowired
	private IFunctionDao functionDao;

	@Override
	public List<Function> findAll() {
		return functionDao.findAll();
	}

	@Override
	public void save(Function model) {
		Function parentFunction = model.getParentFunction();
		if (parentFunction != null && parentFunction.getId().equals("")) {
			model.setParentFunction(null);
		}
		functionDao.save(model);
	}

	@Override
	public void pageQuery(PageBean pageBean) {
		functionDao.pageQuery(pageBean);
	}

	@Override
	public List<Function> findMenu() {
		
		List<Function> list = null;
		User user = BOSUtils.getLoginUser();
		if(user.getUsername().equals("admin")) {
			//如果是超级管理员内置用户,查询所有菜单
			list = functionDao.findAllMenu();
		}else{
			//其他用户,根据用户id查询菜单
			list = functionDao.findMenuByUserId(user.getId());
		}
		
		return list;
	}

}
