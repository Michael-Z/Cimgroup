package cn.com.cimgroup.util.betingCompute;
import java.util.HashMap;
import java.util.Map;

/**
 * 选号方式
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-5-9
 */
public class MatchSelectType {

	private MatchSelectType(){}
	
	/**
	 * 选号方式组合
	 */
	private static Map<String,String> conf=new HashMap<String,String>();
	
	/**
	 * 选号方式中文
	 */
	private static Map<String, String> bet_code_map_convert=new HashMap<String, String>();
	
	
	/**
	 * 选号方式包含关系
	 */
	private static Map<String,String> contain=new HashMap<String,String>();
	
	/**
	 * 初始化数据
	 */
	static{
		conf.put("01", "1&1*00");
		conf.put("02", "2&1*02");
		
		conf.put("03", "3&03");
		conf.put("04", "3&3*02");
		conf.put("05", "3&3*02+1*03");
		
		conf.put("06", "4&1*06");
		conf.put("07", "4&4*03");
		conf.put("08", "4&4*03+1*06");
		conf.put("09", "4&6*02");
		conf.put("10", "4&6*02+4*03+1*06");
				
		conf.put("11", "5&1*11");
		conf.put("12", "5&5*06");
		conf.put("13", "5&5*06+1*11");
		conf.put("14", "5&10*02");
		conf.put("15", "5&10*03+5*06+1*11");
		conf.put("16", "5&10*02+10*03");
		conf.put("17", "5&10*02+10*03+5*06+1*11");
		
		conf.put("18", "6&1*18");
		conf.put("19", "6&6*11");
		conf.put("20", "6&6*11+1*18");
		conf.put("21", "6&15*02");
		conf.put("22", "6&20*03");
		conf.put("23", "6&15*06+6*11+1*18");
		conf.put("24", "6&15*02+20*03");
		conf.put("25", "6&20*03+15*06+6*11+1*18");
		conf.put("26", "6&15*02+20*03+15*06");
		conf.put("27", "6&15*02+20*03+15*06+6*11+1*18");
		
		conf.put("28", "7&1*28");
		conf.put("29", "7&7*18");
		conf.put("30", "7&7*18+1*28");
		conf.put("31", "7&21*11");
		conf.put("32", "7&35*06");
		conf.put("33", "7&21*02+35*03+35*06+21*11+7*18+1*28");
		
		conf.put("34", "8&1*34");
		conf.put("35", "8&8*28");
		conf.put("36", "8&8*28+1*34");
		conf.put("37", "8&28*18");
		conf.put("38", "8&56*11");
		conf.put("39", "8&70*06");
		conf.put("40", "8&28*02+56*03+70*06+56*11+28*18+8*28+1*34");
		
		
		
		contain.put("01", "1");
		contain.put("02", "2");
		
		contain.put("03", "3");
		contain.put("04", "2");
		contain.put("05", "2&3");
		
		contain.put("06", "4");
		contain.put("07", "3");
		contain.put("08", "3&4");
		contain.put("09", "2");
		contain.put("10", "2&3&4");
				
		contain.put("11", "5");
		contain.put("12", "4");
		contain.put("13", "4&5");
		contain.put("14", "2");
		contain.put("15", "3&4&5");
		contain.put("16", "2&3");
		contain.put("17", "2&3&4&5");
		
		contain.put("18", "6");
		contain.put("19", "5");
		contain.put("20", "5&6");
		contain.put("21", "2");
		contain.put("22", "3");
		contain.put("23", "4&5&6");
		contain.put("24", "2&3");
		contain.put("25", "3&4&5&6");
		contain.put("26", "2&3&4");
		contain.put("27", "2&3&4&5&6");
		
		contain.put("28", "7");
		contain.put("29", "6");
		contain.put("30", "6&7");
		contain.put("31", "5");
		contain.put("32", "4");
		contain.put("33", "2&3&4&5&6&7");
		
		contain.put("34", "8");
		contain.put("35", "7");
		contain.put("36", "7&8");
		contain.put("37", "6");
		contain.put("38", "5");
		contain.put("39", "4");
		contain.put("40", "2&3&4&5&6&7&8");
		
		
		
		
		
		bet_code_map_convert.put("01","单关");
		bet_code_map_convert.put("02","2串1");
		bet_code_map_convert.put("03","3串1");
		bet_code_map_convert.put("04","3串3");
		bet_code_map_convert.put("05","3串4");
		bet_code_map_convert.put("06","4串1");
		bet_code_map_convert.put("07","4串4");
		bet_code_map_convert.put("08","4串5");
		bet_code_map_convert.put("09","4串6");
		bet_code_map_convert.put("10","4串11");
		
		bet_code_map_convert.put("11","5串1");
		bet_code_map_convert.put("12","5串5");
		bet_code_map_convert.put("13","5串6");
		bet_code_map_convert.put("14","5串10");
		bet_code_map_convert.put("15","5串16");
		bet_code_map_convert.put("17","5串26");
		bet_code_map_convert.put("16","5串20");
		bet_code_map_convert.put("18","6串1");
		bet_code_map_convert.put("19","6串6");
		bet_code_map_convert.put("20","6串7");
		
		bet_code_map_convert.put("21","6串15");
		bet_code_map_convert.put("22","6串20");
		bet_code_map_convert.put("23","6串22");
		bet_code_map_convert.put("24","6串35");
		bet_code_map_convert.put("25","6串42");
		bet_code_map_convert.put("26","6串50");
		bet_code_map_convert.put("27","6串57");
		bet_code_map_convert.put("28","7串1");
		bet_code_map_convert.put("29","7串7");
		bet_code_map_convert.put("30","7串8");
		
		bet_code_map_convert.put("31","7串21");
		bet_code_map_convert.put("32","7串35");
		bet_code_map_convert.put("33","7串120");
		bet_code_map_convert.put("34","8串1");
		bet_code_map_convert.put("35","8串8");
		bet_code_map_convert.put("36","8串9");
		bet_code_map_convert.put("38","8串56");
		bet_code_map_convert.put("37","8串28");
		bet_code_map_convert.put("39","8串70");
		bet_code_map_convert.put("40","8串247");
	}
	
	/**
	 * 返回选号方式
	 */
	public static Map<String,String> getSelectType(){
		return conf;
	}
	
	
	/**
	 * 返回选号方式	中文
	 */
	public static Map<String,String> getChineseSelectType(){
		return bet_code_map_convert;
	}
	/**
	 * 当前对应玩法比赛组合数
	 * @param selectId
	 * @return
	 */
	public static int getChoiceNum(String selectId){
		String str = conf.get(selectId);
		if(null!=str){
			String num = str.substring(0, 1);
			return Integer.parseInt(num);
		}
		return 2;
	}


	public static String[] getCommSelect(String seleId) {
		String str = contain.get(seleId);
		if(null!=str){
			return str.split("&");
		}
		return new String []{"2"};
	}
}
