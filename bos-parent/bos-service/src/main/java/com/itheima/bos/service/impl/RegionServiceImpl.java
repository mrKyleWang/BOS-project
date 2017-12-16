package com.itheima.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IRegionDao;
import com.itheima.bos.domain.Region;
import com.itheima.bos.service.IRegionService;
import com.itheima.bos.utils.PageBean;


@Service
@Transactional
public class RegionServiceImpl implements IRegionService {
	@Autowired
	private IRegionDao regionDao;
	
	//区域数据批量保存
	@Override
	public void saveBatch(List<Region> regionList) {
		for(Region region:regionList) {
			regionDao.saveOrUpdate(region);
		}
	}

	
	//分页查询
	@Override
	public void pageQuery(PageBean pageBean) {
		regionDao.pageQuery(pageBean);
	}

	//查询所有区域
	@Override
	public List<Region> findAll() {
		return regionDao.findAll();
	}

	//根据页面输入进行模糊查询
	@Override
	public List<Region> findListByQ(String q) {
		return regionDao.findListByQ(q);
	}

}
