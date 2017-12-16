package com.itheima.bos.web.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.Function;
import com.itheima.bos.service.IFunctionService;
import com.itheima.bos.web.action.base.BaseAction;

/**
 * 权限管理
 * 
 * @author wking
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class FunctionAction extends BaseAction<Function> {

	@Autowired
	private IFunctionService functionService;

	// 查询所有权限,返回json数据
	public String listajax() {
		List<Function> list = functionService.findAll();
		this.java2Json(list, new String[] {"parentFunction","roles"});
		return NONE;
	}
	
	public String add() {
		functionService.save(model);
		return LIST;
	}
	
	//分页查询
	public String pageQuery() {
		//由于分页查询PageBean中的page和 Function中的page重名,struts默认优先给model赋值
		//所以手动取出赋值给PageBean
		String page = model.getPage();
		pageBean.setCurrentPage(Integer.parseInt(page));
		functionService.pageQuery(pageBean);
		this.java2Json(pageBean, new String[] {"parentFunction","roles","children"});
		return NONE;
	}
	
	//根据当前登录用户查询对应的菜单数据,返回json
	public String findMenu() {
		List<Function> list = functionService.findMenu();
		this.java2Json(list, new String[] {"parentFunction","roles","children"});
		return NONE;
	}
	
	
	
	
	
	
	

}
