package com.business.service;

import java.util.List;

import com.business.web.bean.CategoryBean;

public interface CategoryService {

	public boolean saveCategoryInfo(List<CategoryBean> listDataFromXLS);

	public  boolean updatecategoryInfo(List<CategoryBean> listDataFromXLS);

	public void deleteCategoryInfo(Integer clientId);



		

}
