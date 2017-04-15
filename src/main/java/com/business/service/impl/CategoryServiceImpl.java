package com.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.model.dataaccess.CategoryDao;
import com.business.service.CategoryService;
import com.business.web.bean.CategoryBean;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryDao categoryDao;

	@Transactional
	public boolean saveCategoryInfo(List<CategoryBean> listDataFromXLS) {
		
		return categoryDao.saveCategoryInfo(listDataFromXLS);
	}

	@Transactional
	public void updatecategoryInfo(CategoryBean categoryBean) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public boolean updatecategoryInfo(List<CategoryBean> listDataFromXLS) {
		// TODO Auto-generated method stub
		return categoryDao.updatecategoryInfo(listDataFromXLS);
	}

	@Transactional
	public void deleteCategoryInfo(Integer clientId) {
		categoryDao.deleteCategoryInfo(clientId);
		
	}

}
