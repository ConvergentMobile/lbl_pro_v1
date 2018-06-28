package com.business.model.dataaccess.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.business.common.dto.KeywordDTO;
import com.business.common.dto.RankDTO;
import com.business.common.dto.SweetIQAccuracyDTO;
import com.business.common.dto.SweetIQCoverageDTO;
import com.business.common.dto.SweetIQDTO;
import com.business.common.dto.SweetIQRankingDTO;
import com.business.common.dto.TrendDTO;
import com.business.model.dataaccess.SweetIQDAO;
import com.business.model.pojo.KeywordEntity;
import com.business.model.pojo.SweetIQAccuracyEntity;
import com.business.model.pojo.SweetIQCoverageEntity;
import com.business.model.pojo.SweetIQEntity;
import com.business.model.pojo.SweetIQRankingEntity;

@Repository
public class SweetIQDAOImpl implements SweetIQDAO {

	Logger logger = Logger.getLogger(SweetIQDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private HttpSession httpSession;

	public void addAccuracyDetails(List<SweetIQAccuracyDTO> accuracyDetails) {

		Session session = sessionFactory.getCurrentSession();

		for (SweetIQAccuracyDTO sweetIQAccuracyDTO : accuracyDetails) {
			SweetIQAccuracyEntity accuracyEntity = new SweetIQAccuracyEntity();
			BeanUtils.copyProperties(sweetIQAccuracyDTO, accuracyEntity);
			session.save(accuracyEntity);
		}
	}

	public void saveCoverageReport(List<SweetIQCoverageDTO> coverageReport) {
		Session session = sessionFactory.getCurrentSession();

		for (SweetIQCoverageDTO coverage : coverageReport) {
			SweetIQCoverageEntity coverageEntiry = new SweetIQCoverageEntity();
			BeanUtils.copyProperties(coverage, coverageEntiry);
			session.save(coverageEntiry);
		}

	}

	public SweetIQCoverageDTO getLocationDirectoryPerformance(Date startDate,
			Date endDate, String store) {

		SweetIQCoverageDTO coverageDTO = new SweetIQCoverageDTO();


		Session session = sessionFactory.getCurrentSession();

		String query = "SELECT avg(facebookCoverage) as facebook, avg(googleCoverage) as google, avg(bingCoverage) as bing, avg(yellowPagesCoverage) as yellowpages, avg(fourSquareCoverage) as fourSquare "
				+ "FROM SweetIQCoverageEntity where date>=? and date<=? and locId=?";

		Query createQuery = session.createQuery(query.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setString(2, store);

		Object[] coverage = (Object[]) createQuery.uniqueResult();
		if (coverage != null && coverage[0] != null) {
			coverageDTO.setFacebookCoverage(Double.valueOf(
					(coverage[0].toString())).intValue());
			coverageDTO.setGoogleCoverage(Double.valueOf(
					(coverage[1].toString())).intValue());
			coverageDTO.setBingCoverage(Double
					.valueOf((coverage[2].toString())).intValue());
			coverageDTO.setYellowPagesCoverage(Double.valueOf(
					(coverage[3].toString())).intValue());
			coverageDTO.setFourSquareCoverage(Double.valueOf(
					(coverage[4].toString())).intValue());
		} else {
			coverageDTO.setFacebookCoverage(0);
			coverageDTO.setGoogleCoverage(0);
			coverageDTO.setBingCoverage(0);
			coverageDTO.setYellowPagesCoverage(0);
			coverageDTO.setFourSquareCoverage(0);
		}
		return coverageDTO;
	}

	@Override
	public SweetIQCoverageDTO getDirectoryPerformance(Date startDate,
			Date endDate, Integer clientId, String store) {

		SweetIQCoverageDTO coverageDTO = new SweetIQCoverageDTO();


		Session session = sessionFactory.getCurrentSession();
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT avg(facebookCoverage) as facebook, avg(googleCoverage) as google, avg(bingCoverage) as bing, avg(yellowPagesCoverage) as yellowpages, avg(fourSquareCoverage) as fourSquare "
				+ " FROM SweetIQCoverageEntity where date>=? and date<=? and brandId=?");
		
		if (store != null && store.length() > 0) {
			sb.append(" and locId=?");
		}


		Query createQuery = session.createQuery(sb.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, clientId);
		if (store != null && store.length() > 0) {
			createQuery.setString(3, store);
		}

		Object[] coverage = (Object[]) createQuery.uniqueResult();
		if (coverage != null && coverage[0] != null) {
			coverageDTO.setFacebookCoverage(Double.valueOf(
					(coverage[0].toString())).intValue());
			coverageDTO.setGoogleCoverage(Double.valueOf(
					(coverage[1].toString())).intValue());
			coverageDTO.setBingCoverage(Double
					.valueOf((coverage[2].toString())).intValue());
			coverageDTO.setYellowPagesCoverage(Double.valueOf(
					(coverage[3].toString())).intValue());
			coverageDTO.setFourSquareCoverage(Double.valueOf(
					(coverage[4].toString())).intValue());
		} else {
			coverageDTO.setFacebookCoverage(0);
			coverageDTO.setGoogleCoverage(0);
			coverageDTO.setBingCoverage(0);
			coverageDTO.setYellowPagesCoverage(0);
			coverageDTO.setFourSquareCoverage(0);
		}
		return coverageDTO;
	}

	public SweetIQCoverageDTO getBrandCoverage(Date startDate, Date endDate,
			Integer clientId) {
		// TODO Auto-generated method stub
		SweetIQCoverageDTO coverageDTO = new SweetIQCoverageDTO();



		Session session = sessionFactory.getCurrentSession();

		String query = "SELECT avg((facebookCoverage+bingCoverage+googleCoverage+yellowPagesCoverage+fourSquareCoverage)/5) as coverage, sum(possibleListings) as possibleListings, sum(totalListingCount) as totalListingCount"
				+ " FROM SweetIQCoverageEntity where date>=? and date<=? and brandId=?";

		Query createQuery = session.createQuery(query.toString());
		createQuery.setDate(0, startDate);
		createQuery.setDate(1, endDate);
		createQuery.setInteger(2, clientId);

		Object[] coverage = (Object[]) createQuery.uniqueResult();
		if (coverage != null && coverage[0] != null) {
			coverageDTO.setCoverage(Double.valueOf((coverage[0].toString()))
					.intValue());
			coverageDTO.setPossibleListings(Double.valueOf(
					(coverage[1].toString())).intValue());
			coverageDTO.setTotalListingCount(Double.valueOf(
					(coverage[2].toString())).intValue());

		} else {
			coverageDTO.setCoverage(0);
			coverageDTO.setPossibleListings(0);
			coverageDTO.setTotalListingCount(0);
		}
		return coverageDTO;
	}

	public SweetIQCoverageDTO getListingCoverage(Integer clientId,
			Date startDate, Date endDate, String store) {
		// TODO Auto-generated method stub
		SweetIQCoverageDTO coverageDTO = new SweetIQCoverageDTO();

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		sb.append("SELECT avg((facebookCoverage+bingCoverage+googleCoverage+yellowPagesCoverage+fourSquareCoverage)/5) as coverage, sum(possibleListings) as possibleListings, sum(totalListingCount) as totalListingCount"
				+ " FROM SweetIQCoverageEntity where brandId=? and date>=? and date<=?");

		if (store != null && store.length() > 0) {
			sb.append(" and locId=?");
		}

		Query createQuery = session.createQuery(sb.toString());
		createQuery.setInteger(0, clientId);
		createQuery.setDate(1, startDate);
		createQuery.setDate(2, endDate);
		if (store != null && store.length() > 0) {
			createQuery.setString(3, store);
		}

		Object[] coverage = (Object[]) createQuery.uniqueResult();
		if (coverage != null && coverage[0] != null) {
			coverageDTO.setCoverage(Double.valueOf((coverage[0].toString()))
					.intValue());
			coverageDTO.setPossibleListings(Double.valueOf(
					(coverage[1].toString())).intValue());
			coverageDTO.setTotalListingCount(Double.valueOf(
					(coverage[2].toString())).intValue());

		} else {
			coverageDTO.setCoverage(0);
			coverageDTO.setPossibleListings(0);
			coverageDTO.setTotalListingCount(0);
		}
		return coverageDTO;
	}

	public List<String> getBranchListFromAccuracy() {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT DISTINCT branchId FROM SweetIQAccuracyEntity";
		Query createQuery = session.createQuery(sql);
		List<String> branchList = (List<String>) createQuery.list();
		return branchList;
	}

	public void addOrUpdateStores(List<SweetIQDTO> listOfStores) {
		Session session = sessionFactory.getCurrentSession();
		for (SweetIQDTO dto : listOfStores) {
			SweetIQDTO sweetIQLBLMap = getSweetIQLBLMap(dto.getBrandId(), dto.getStore());
			if(sweetIQLBLMap.getLblStoreId()==null) {
				SweetIQEntity entity = new SweetIQEntity();
				BeanUtils.copyProperties(dto, entity);
				session.save(entity);
			}
		}
		
	
	}

	public void addRankAndKeywords(SweetIQRankingDTO dto,
			List<KeywordDTO> keywordList) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();

		SweetIQRankingEntity entity = new SweetIQRankingEntity();
		BeanUtils.copyProperties(dto, entity);

		session.save(entity);

		for (KeywordDTO keywordDTO : keywordList) {
			KeywordEntity kentity = new KeywordEntity();
			BeanUtils.copyProperties(keywordDTO, kentity);
			kentity.setRankEntity(entity);

			session.save(kentity);
		}
	}

	public List<SweetIQRankingDTO> getRanks() {

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SweetIQRankingEntity.class);

		List<SweetIQRankingDTO> ranks = new ArrayList<SweetIQRankingDTO>();

		List<SweetIQRankingEntity> list = criteria.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY).list();
		for (SweetIQRankingEntity entity : list) {
			SweetIQRankingDTO dto = new SweetIQRankingDTO();
			BeanUtils.copyProperties(entity, dto);
			ranks.add(dto);
		}
		return ranks;
	}

	public RankDTO getRanking(Integer clientId, String store, String engine,
			String type, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SweetIQRankingEntity.class);
		criteria.add(Restrictions.eq("brandId", clientId));
		if (store != null && store.length() > 0) {
			criteria.add(Restrictions.eq("sIQBranchId", store));
		}

		criteria.add(Restrictions.eq("engine", engine).ignoreCase());
		criteria.add(Restrictions.eq("searchType", type).ignoreCase());
		criteria.add(Restrictions.ge("date", startDate));
		criteria.add(Restrictions.le("date", endDate));
		criteria.addOrder(Order.desc("date"));

		RankDTO rankDTO = new RankDTO();

		List<SweetIQRankingDTO> ranks = new ArrayList<SweetIQRankingDTO>();

		List<SweetIQRankingEntity> list = criteria.setResultTransformer(
				Criteria.DISTINCT_ROOT_ENTITY).list();
		for (SweetIQRankingEntity entity : list) {
			SweetIQRankingDTO dto = new SweetIQRankingDTO();
			BeanUtils.copyProperties(entity, dto);
			ranks.add(dto);
		}

		// System.out.println("ranks==" + ranks.size());
		Long totalKeywords = 0l;
		Long rankedKeywords = 0l;
		Double averageRanking = 0.0;
		Double coverage = 0.0;

		List<Long> rankIds = new ArrayList<Long>();
		for (SweetIQRankingDTO sweetIQRankingDTO : ranks) {
			rankIds.add(sweetIQRankingDTO.getId());

		}
		List<KeywordDTO> keywords = new ArrayList<KeywordDTO>();
		if (rankIds.size() > 0) {
			Map<String, Double> averageRanks = getKeywordsAverageRanks(rankIds);
			Map<String, Double> coverageMap = getKeywordsCoverage(rankIds);

			Set<String> keySet = coverageMap.keySet();

			for (String keywrod : keySet) {
				KeywordDTO dto = new KeywordDTO();
				Double averageRank = averageRanks.get(keywrod);
				Double covg = coverageMap.get(keywrod);

				dto.setKeyword(keywrod);
				if (averageRank != null) {
					dto.setAvgRank(averageRank.intValue());
				} else {
					dto.setAvgRank(0);
				}
				if (covg != null) {
					dto.setCoverage(covg.intValue());
				} else {
					dto.setCoverage(0);
				}
				keywords.add(dto);
			}

		}

		List<TrendDTO> monthlyTrends = getMonthlyTrends(clientId, store,
				engine, type, startDate, endDate);

		SweetIQRankingDTO metrcs = getMetrics(clientId, store, engine, type,
				startDate, endDate);

		rankDTO.setTotalKeywords(metrcs.getTotalKeyWords());
		rankDTO.setRankedKeywords(metrcs.getRankedKeyWords());
		rankDTO.setAverageRanking(metrcs.getAverageRank());
		rankDTO.setCoverage(metrcs.getCoverage());

		// get from map
		rankDTO.setKeywords(keywords);

		rankDTO.setTrends(monthlyTrends);

		return rankDTO;

	}

	public List<SweetIQAccuracyDTO> getAccuarcy(Integer clientId, Date startDate,
			Date endDate, String store) {
		List<SweetIQAccuracyDTO> accuracyData = new ArrayList<SweetIQAccuracyDTO>();
		

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		sb.append("SELECT directory, avg(average_Match) FROM SweetIQAccuracyEntity where brandId=? and date>=? and date<=?");

		if (store != null && store.length() > 0) {
			sb.append(" and branchId=?");
		}
		sb.append(" group by directory");

		Query createQuery = session.createQuery(sb.toString());
		createQuery.setInteger(0, clientId);
		createQuery.setDate(1, startDate);
		createQuery.setDate(2, endDate);
		if (store != null && store.length() > 0) {
			createQuery.setString(3, store);
		}

		List<Object[]> accuracy = createQuery.list();
		for (Object[] objects : accuracy) {
			SweetIQAccuracyDTO accuracyDto = new SweetIQAccuracyDTO();
			if (objects[0] != null) {
				accuracyDto.setDirectory(objects[0].toString());
				accuracyDto.setAverage_Match(objects[1].toString());
				accuracyData.add(accuracyDto);
			}
			
		}
		return accuracyData;

	}
	

	public List<SweetIQAccuracyDTO> getAccuarcyForStore(Integer clientId,
			Date startDate, Date endDate, String store) {

		 List<SweetIQAccuracyDTO> accuracyDetails = new ArrayList<SweetIQAccuracyDTO>();

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(SweetIQAccuracyEntity.class);
		criteria.add(Restrictions.eq("brandId", clientId));
		criteria.add(Restrictions.eq("branchId", store));
		criteria.add(Restrictions.ge("date", startDate));
		criteria.add(Restrictions.le("date", endDate));

		 List<SweetIQAccuracyEntity> list = criteria.list();
		 for (SweetIQAccuracyEntity sweetIQAccuracyEntity : list) {
			 SweetIQAccuracyDTO dto = new SweetIQAccuracyDTO();
			 BeanUtils.copyProperties(sweetIQAccuracyEntity, dto);
			 accuracyDetails.add(dto);
		}
		return accuracyDetails;
	}

	public SweetIQRankingDTO getMetrics(Integer clientId, String store,
			String engine, String type, Date startDate, Date endDate) {


		Calendar calStart = Calendar.getInstance();
		calStart.setTime(endDate);
		calStart.set(Calendar.DATE, -30);
		startDate = calStart.getTime();

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		sb.append("SELECT avg(averageRank), sum(totalKeyWords), sum(rankedKeyWords) FROM SweetIQRankingEntity");
		sb.append(" where brandId=? and searchType=? and engine=?");
		sb.append(" and date>=? and date<=?");
		if (store != null && store.length() > 0) {
			sb.append(" and sIQBranchId=?");
		}

		String query = sb.toString();

		// System.out.println(query);

		Query createQuery = session.createQuery(query.toString());
		createQuery.setInteger(0, clientId);
		createQuery.setString(1, type);
		createQuery.setString(2, engine);
		createQuery.setDate(3, startDate);
		createQuery.setDate(4, endDate);
		if (store != null && store.length() > 0) {
			createQuery.setString(5, store);
		}

		List<Object[]> data = createQuery.list();
		SweetIQRankingDTO dto = new SweetIQRankingDTO();
		for (Object[] objects : data) {

			if (objects[0] != null) {

				dto.setAverageRank(Double.valueOf(objects[0].toString()));
				dto.setTotalKeyWords(Long.valueOf(objects[1].toString()));
				dto.setRankedKeyWords(Long.valueOf(objects[2].toString()));

				Double coverage = Math.ceil(dto.getRankedKeyWords() * 100
						/ dto.getTotalKeyWords());

				dto.setCoverage(coverage);

			}
		}
		return dto;

	}

	public Map<String, Double> getKeywordsCoverage(List<Long> rankIds) {
		Map<String, Double> coverage = new HashMap<String, Double>();

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		sb.append("SELECT keyword, avg(coverage) FROM KeywordEntity");
		sb.append(" where rank_id in (:ranks) ");
		sb.append(" group by keyword");

		String query = sb.toString();

		Query createQuery = session.createQuery(query.toString());

		createQuery.setParameterList("ranks", rankIds);

		List<Object[]> data = createQuery.list();

		for (Object[] objects : data) {
			if (objects[0] != null) {
				if (objects[0] != null) {
					coverage.put(objects[0].toString(),
							Double.valueOf(objects[1].toString()));
				}
			}
		}
		return coverage;

	}

	public Map<String, Double> getKeywordsAverageRanks(List<Long> rankIds) {
		Map<String, Double> avgRanks = new HashMap<String, Double>();

		// SELECT keyword, avg(avgRank) FROM lbl.sweetiq_keywords where rank_id
		// in (21,45) and coverage <> 0 group by keyword

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		sb.append("SELECT keyword, avg(avgRank) FROM KeywordEntity");
		sb.append(" where rank_id in (:ranks) ");
		if (rankIds.size() > 1) {
			sb.append(" and coverage <> 0");
		}
		sb.append(" group by keyword");

		String query = sb.toString();

		Query createQuery = session.createQuery(query.toString());

		createQuery.setParameterList("ranks", rankIds);

		List<Object[]> data = createQuery.list();

		for (Object[] objects : data) {
			if (objects[0] != null) {
				if (objects[0] != null) {
					avgRanks.put(objects[0].toString(),
							Double.valueOf(objects[1].toString()));
				}
			}
		}
		return avgRanks;

	}

	public List<TrendDTO> getMonthlyTrends(Integer clientId, String store,
			String engine, String type, Date startDate, Date endDate) {

		List<TrendDTO> dtoList = new ArrayList<TrendDTO>();


		Session session = sessionFactory.getCurrentSession();

		StringBuffer sb = new StringBuffer();

		sb.append("SELECT date, avg(averageRank), avg(coverage) FROM SweetIQRankingEntity");
		sb.append(" where brandId=? and searchType=? and engine=?");
		sb.append(" and date>=? and date<=?");
		if (store != null && store.length() > 0) {
			sb.append(" and sIQBranchId=?");
		}
		sb.append(" group by month(date), year(date)");

		String query = sb.toString();

		Query createQuery = session.createQuery(query.toString());
		createQuery.setInteger(0, clientId);
		createQuery.setString(1, type);
		createQuery.setString(2, engine);
		createQuery.setDate(3, startDate);
		createQuery.setDate(4, endDate);
		if (store != null && store.length() > 0) {
			createQuery.setString(5, store);
		}

		List<Object[]> data = createQuery.list();

		for (Object[] objects : data) {

			TrendDTO dto = new TrendDTO();
			if (objects[0] != null) {
				if (objects[0] != null) {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.S");
					Date date = null;
					try {
						date = format.parse(objects[0].toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					dto.setDate(date);
					dto.setRank(Double.valueOf(objects[1].toString()));
					dto.setCoverage(Double.valueOf(objects[2].toString()));

					dtoList.add(dto);
				}
			}
		}
		return dtoList;

	}

	public List<SweetIQDTO> getSweetIQLBLMap(Integer brandId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SweetIQEntity.class);
		criteria.add(Restrictions.eq("brandId", brandId));

		List<SweetIQDTO> storesList = new ArrayList<SweetIQDTO>();

		List<SweetIQEntity> list = criteria.list();
		for (SweetIQEntity entity : list) {
			SweetIQDTO dto = new SweetIQDTO();
			BeanUtils.copyProperties(entity, dto);
			storesList.add(dto);
		}
		return storesList;
	}
	
	
	public SweetIQDTO getSweetIQLBLMap(Integer brandId, String store) {
		// TODO Auto-generated method stub
		SweetIQDTO dto = new SweetIQDTO();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SweetIQEntity.class);
		criteria.add(Restrictions.eq("brandId", brandId));
		criteria.add(Restrictions.eq("store", store));

		List<SweetIQDTO> storesList = new ArrayList<SweetIQDTO>();

		SweetIQEntity entity = (SweetIQEntity)criteria.uniqueResult();
		if(entity!=null)
			BeanUtils.copyProperties(entity, dto);

		return dto;
	}

	

	public static void main(String[] args) {
		String s = "(2)";
		s = s.replaceAll("(", "");
		System.out.println(s);
	}

}
