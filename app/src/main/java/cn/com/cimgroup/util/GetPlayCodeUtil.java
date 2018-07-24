package cn.com.cimgroup.util;

import cn.com.cimgroup.GlobalConstants;

/**
 * 数字彩通用获取对应的codeplay
 * @Description:
 * @param gameNo
 * @param seleId
 * @param typeId
 * @return
 * @author:www.wenchuang.com
 * @date:2016年1月20日
 */
public class GetPlayCodeUtil {
	/**
	 * 数字彩通用获取对应的codeplay
	 * @Description:
	 * @param gameNo
	 * @param seleId
	 * @param typeId
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月20日
	 */
	public static String getCodePlay(String gameNo, String seleId, String typeId) {
		String codePlay = "";
		switch (gameNo) {
		case GlobalConstants.TC_DLT:
			//seleId 01单式，02复式，03胆拖
			//typeId 是否是追加，00不追加，01追加
			codePlay = getDLTCodePlay(seleId, typeId);
			break;
		case GlobalConstants.TC_PL3:
			//typeId 01直选，02直选和值，03排三组三，04组选和值，05排三组六
			//seleId 01单式，02复式
			codePlay = getPL3CodePlay(seleId, typeId);
			break;
		case GlobalConstants.TC_PL5:
			codePlay = getPL5CodePlay(seleId, typeId);
			break;
		case GlobalConstants.TC_QXC:
			codePlay = getQXCCodePlay(seleId, typeId);
			break;
		default:
			break;
		}
		return codePlay;
	}
	
	
	/**
	 * 获取大乐透的codeplay
	 * @Description:
	 * @param seleId
	 * @param typeId
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月20日
	 */
	private static String getDLTCodePlay(String seleId, String typeId){
		String codePlay = "";
		switch (seleId) {
		case "01":
			switch (typeId) {
			case "00":
				codePlay = PlayInfo.DLT_DS;
				break;
			case "01":
				codePlay = PlayInfo.DLT_DSZJ;
				break;
			default:
				break;
			}
			break;
		case "02":
			switch (typeId) {
			case "00":
				codePlay = PlayInfo.DLT_FS;
				break;
			case "01":
				codePlay = PlayInfo.DLT_FSZJ;
				break;
			default:
				break;
			}

			break;
		case "03":
			switch (typeId) {
			case "00":
				codePlay = PlayInfo.DLT_DT;
				break;
			case "01":
				codePlay = PlayInfo.DLT_DTZJ;
				break;
			default:
				break;
			}

			break;
		default:
			break;
		}
		return codePlay;
	}
	
	/**
	 * 获取排三的codeplay
	 * @Description:
	 * @param seleId
	 * @param typeId
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月22日
	 */
	private static String getPL3CodePlay(String seleId, String typeId){
		String codePlay = "";
		switch (typeId) {
		case "01":
			switch (seleId) {
			case "01":
				codePlay = PlayInfo.PL3_ZXDS;
				break;
			case "02":
				codePlay = PlayInfo.PL3_ZXFS;
				break;
			default:
				break;
			}
			break;
		case "02":
			codePlay = PlayInfo.PL3_ZXHZ;
			break;
		case "03":
//			switch (seleId) {
//			case "01":
				codePlay = PlayInfo.PL3_ZUX3DS;
//				break;
//			case "02":
//				codePlay = PlayInfo.PL3_ZUX3FS;
//				break;
//			default:
//				break;
//			}
			break;
		case "04":
//			switch (seleId) {
//			case "01":
				codePlay = PlayInfo.PL3_ZUX6DS;
//				break;
//			case "02":
//				codePlay = PlayInfo.PL3_ZUX6FS;
//				break;
//			default:
//				break;
//			}
			break;
		case "05":
			codePlay = PlayInfo.PL3_ZUXHZ;
			break;
		default:
			break;
		}
		return codePlay;
	}
	
	/**
	 * 获取排列5的codeplay
	 * @Description:
	 * @param seleId
	 * @param typeId
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	private static String getPL5CodePlay(String seleId, String typeId) {
		// TODO Auto-generated method stub
		String codePlay = "";
		switch (seleId) {
		case "01":
			codePlay = PlayInfo.PL5_DS;
			break;
		case "02":
			codePlay = PlayInfo.PL5_FS;
			break;
		default:
			break;
		}
		return codePlay;
	}
	
	/**
	 * 获取七星彩的codeplay
	 * @Description:
	 * @param seleId
	 * @param typeId
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	private static String getQXCCodePlay(String seleId, String typeId) {
		// TODO Auto-generated method stub
		String codePlay = "";
		switch (seleId) {
		case "01":
			codePlay = PlayInfo.QXC_DS;
			break;
		case "02":
			codePlay = PlayInfo.QXC_FS;
			break;
		default:
			break;
		}
		return codePlay;
	}
}
