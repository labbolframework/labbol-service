package com.labbol.core.check.sign.constants;

public enum Mycat {
	/**切换*/
	YES("0"),
	/**不切换*/
	NO("1");
	
	private String code;
	
	Mycat(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
