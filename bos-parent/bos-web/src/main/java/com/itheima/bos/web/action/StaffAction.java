package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.Staff;
import com.itheima.bos.service.IStaffService;
import com.itheima.bos.web.action.base.BaseAction;

/**
 * 取派员管理
 * 
 * @author wking
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class StaffAction extends BaseAction<Staff> {
	// 接收批量删除id字符串参数
	private String ids;

	@Autowired
	private IStaffService staffService;

	// 添加取派员
	public String add() {
		staffService.save(model);
		return LIST;
	}

	// 分页查询
	public String pageQuery() throws IOException {
		staffService.pageQuery(pageBean);
		// 指定不需要的属性,进行分页查询并相应
		this.java2Json(pageBean, new String[] { "currentPage", "detachedCriteria", "pageSize","decidedzones" });
		return NONE;
	}

	// 批量删除
	@RequiresPermissions("staff-delete")	//执行这个方法,需要有staff-delete权限
	public String deleteBatch() {
		staffService.deleteBatch(ids);
		return LIST;
	}

	// 修改取派员信息
	public String edit() {
		// 查询数据库，根据id查询原始数据
		Staff staff = staffService.findById(model.getId());
		// 使用页面提交的数据进行覆盖
		staff.setName(model.getName());
		staff.setTelephone(model.getTelephone());
		staff.setHaspda(model.getHaspda());
		staff.setStandard(model.getStandard());
		staff.setStation(model.getStation());
		staffService.update(staff);
		return LIST;
	}

	// 查询所有未删除的取派员,返回json
	public String listajax() {
		List<Staff> list = staffService.findListNotDelete();
		this.java2Json(list, new String[] {"telephone","haspda","deltag","station","standard","decidedzones"});
		return NONE;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
