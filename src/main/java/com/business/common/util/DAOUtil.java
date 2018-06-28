package com.business.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import com.business.common.dto.BrandInfoDTO;
import com.business.model.pojo.BrandEntity;

/**
 * 
 * @author lbl_dev
 *
 */

public class DAOUtil {

	/**
	 * getBrandsMap 
	 * 
	 * @param allActiveBrands
	 * @return
	 */
	public Map<String,List<BrandInfoDTO>> getBrandsMap(List<BrandEntity> allActiveBrands){
		Map<String, List<BrandInfoDTO>> allActiveBrandsMap = new HashMap<String, List<BrandInfoDTO>>();
		for (BrandEntity brandEntity : allActiveBrands) {
			BrandInfoDTO brandInfoDTO = new BrandInfoDTO();
			BeanUtils.copyProperties(brandEntity, brandInfoDTO);
			String brandName = brandInfoDTO.getBrandName();
			List<BrandInfoDTO> infoDTOs = new ArrayList<BrandInfoDTO>();
			if(allActiveBrandsMap.containsKey(brandName)){
				infoDTOs = allActiveBrandsMap.get(brandName);
			}
			infoDTOs.add(brandInfoDTO);
			allActiveBrandsMap.put(brandName, infoDTOs);
		}
		return allActiveBrandsMap;
	}
	
	/**
	 * removeCommaFromString
	 * @param str
	 * @param replacement
	 * @return
	 */
	public static String removeCommaFromString(String str,String replacement){
		if (str!= null && str.startsWith(",")) {
			str = str.replaceFirst(",", replacement);
		}
		return str;
	}
}
