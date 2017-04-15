package com.business.model.dataaccess;

import java.util.List;

import com.business.web.bean.CategoryBean;

public interface CategoryDao {
	
	public boolean saveCategoryInfo(List<CategoryBean> listDataFromXLS);
	public void updatecategoryInfo(CategoryBean categoryBean);
	public  boolean updatecategoryInfo(List<CategoryBean> listDataFromXLS);
	public void deleteCategoryInfo(Integer clientId);

}
