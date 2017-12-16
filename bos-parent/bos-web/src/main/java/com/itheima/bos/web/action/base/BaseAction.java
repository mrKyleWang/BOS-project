package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;

import com.itheima.bos.utils.PageBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 表现层通用实现
 * 
 * @author wking
 *
 * @param <T>
 */
@SuppressWarnings({ "unchecked", "serial" ,"rawtypes"})
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	public static final String HOME = "home";
	public static final String LIST = "list";

	// 模型对象
	protected T model;

	// 接收分页参数
	protected PageBean pageBean = new PageBean();
	DetachedCriteria detachedCriteria = null;

	public void setPage(int page) {
		pageBean.setCurrentPage(page);
	}

	public void setRows(int rows) {
		pageBean.setPageSize(rows);
	}
	
	//将指定Java对象转为Json并响应到客户端页面
	public void java2Json(Object o,String[] excludes) {
		JsonConfig jsonConfig = new JsonConfig();
		// 指定不需要的属性
		jsonConfig.setExcludes(excludes);
		String json = JSONObject.fromObject(o,jsonConfig).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
		try {
			ServletActionContext.getResponse().getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//将指定List对象转为Json并响应到客户端页面
		public void java2Json(List list,String[] excludes) {
			JsonConfig jsonConfig = new JsonConfig();
			// 指定不需要的属性 
			jsonConfig.setExcludes(excludes);
			String json = JSONArray.fromObject(list,jsonConfig).toString();
			ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
			try {
				ServletActionContext.getResponse().getWriter().print(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		

	// 在构造方法中动态获取实体类型，通过反射创建model对象
	public BaseAction() {
		ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> entityClass = (Class<T>) superclass.getActualTypeArguments()[0];
		// 分页离线查询对象
		detachedCriteria = DetachedCriteria.forClass(entityClass);
		pageBean.setDetachedCriteria(detachedCriteria);
		// 通过反射创建对象
		try {
			model = entityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public T getModel() {
		return model;
	}

}
