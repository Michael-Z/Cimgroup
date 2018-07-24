package cn.com.cimgroup.bean;

import java.io.Serializable;

public class PlanContent implements Serializable{
	
	private static final long serialVersionUID = 926269538752642972L;
	
	private Long codePlay;
	private String codePlayDes;
	private String codeNumbers;
	private String codeMultiple;
	private String codeMoney;
	private String codeContent;
	public Long getCodePlay() {
		return codePlay;
	}
	public void setCodePlay(Long codePlay) {
		this.codePlay = codePlay;
	}
	public String getCodePlayDes() {
		return codePlayDes;
	}
	public void setCodePlayDes(String codePlayDes) {
		this.codePlayDes = codePlayDes;
	}
	public String getCodeNumbers() {
		return codeNumbers;
	}
	public void setCodeNumbers(String codeNumbers) {
		this.codeNumbers = codeNumbers;
	}
	public String getCodeMultiple() {
		return codeMultiple;
	}
	public void setCodeMultiple(String codeMultiple) {
		this.codeMultiple = codeMultiple;
	}
	public String getCodeMoney() {
		return codeMoney;
	}
	public void setCodeMoney(String codeMoney) {
		this.codeMoney = codeMoney;
	}
	public String getCodeContent() {
		return codeContent;
	}
	public void setCodeContent(String codeContent) {
		this.codeContent = codeContent;
	}
	
}
