package com.itheima.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IDecidedzoneDao;
import com.itheima.bos.dao.ISubareaDao;
import com.itheima.bos.domain.Decidedzone;
import com.itheima.bos.domain.Subarea;
import com.itheima.bos.service.IDecidedzoneService;
import com.itheima.bos.utils.PageBean;

@Service
@Transactional
public class DecidedzoneServiceImpl implements IDecidedzoneService {
	@Autowired
	private IDecidedzoneDao decidedzoneDao;
	@Autowired
	private ISubareaDao subareaDao;
		
	//添加定区,同时关联分区
	@Override
	public void save(Decidedzone model, String[] subareaid) {
		decidedzoneDao.save(model);
		for (String id : subareaid) {
			//根据id获取分区
			Subarea subarea = subareaDao.findById(id);
			//分区关联定区(多的一方维护关系)
			subarea.setDecidedzone(model);
		}
	}

	@Override
	public void pageQuery(PageBean pageBean) {
		decidedzoneDao.pageQuery(pageBean);
	}
}
