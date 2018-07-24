package cn.com.cimgroup.bean;

import java.io.Serializable;

public class BettingReq implements Serializable {
	
	private static final long serialVersionUID = -8100740909520760440L;
	
	private String codePlay;
	private String codeNumbers;
	private String codeMultiple;
	private String codeMoney;
	private String codeContent;
	public String getCodePlay() {
		return codePlay;
	}
	public void setCodePlay(String codePlay) {
		this.codePlay = codePlay;
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
