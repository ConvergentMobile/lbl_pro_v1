package com.business.model.dataaccess.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.model.dataaccess.CategoryDao;
import com.business.model.pojo.CategorySyphcode;
import com.business.model.pojo.ChannelEntity;
import com.business.web.bean.CategoryBean;

@Repository
public class CategoryDaoImpl implements CategoryDao {

	@Autowired
	private SessionFactory sessionFactory;

	public boolean saveCategoryInfo(List<CategoryBean> listDataFromXLS) {

		Session session = sessionFactory.getCurrentSession();

		for (CategoryBean categoryBean : listDataFromXLS) {

			CategorySyphcode categorySyphcode = new CategorySyphcode();

			BeanUtils.copyProperties(categoryBean, categorySyphcode);

			session.save(categorySyphcode);

		}
		return true;
	}

	public void updatecategoryInfo(CategoryBean categoryBean) {
		// TODO Auto-generated method stub

	}

	public boolean updatecategoryInfo(List<CategoryBean> listDataFromXLS) {
		Session session = sessionFactory.getCurrentSession();

		for (CategoryBean categoryBean : listDataFromXLS) {

			CategorySyphcode categorySyphcode = new CategorySyphcode();

			BeanUtils.copyProperties(categoryBean, categorySyphcode);

			session.update(categorySyphcode);

		}
		return true;
	}

	public void deleteCategoryInfo(Integer clientId) {
		Session session = sessionFactory.getCurrentSession();
		session.createCriteria(CategorySyphcode.class);
		Query createQuery = session
				.createQuery("delete from CategorySyphcode where clientId="
						+ clientId + "");
		createQuery.executeUpdate();

	}
}
