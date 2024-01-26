package com.wenge.dto.org;

/**
 * @author CY
 * @since 2022/11/23 10:53
 */
public class AreaType {

	public final static int PROVINCE = 1;
	public final static int CITY = 2;
	public final static int AREA = 3;
	public final static int STREET = 4;
	public final static int COMMUNITY = 5;

	public static int getAreaType(String provinceCode, String cityCode, String areaCode, String streetCode) throws Exception {
		if (streetCode != null && !"".equals(streetCode)) {
			return STREET;
		} else if (areaCode != null && !"".equals(areaCode)) {
			return AREA;
		} else if (cityCode != null && !"".equals(cityCode)) {
			return CITY;
		} else if (provinceCode != null && !"".equals(provinceCode)) {
			return PROVINCE;
		}
		throw new Exception("wrong_area_type");
	}

}
