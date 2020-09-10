package com.labbol.core.check.sign.holder;

public class MycatHolder {
	
	private static final ThreadLocal<String> ISMYCAT = new ThreadLocal<>();

	public static String getIsmycat() {
		return ISMYCAT.get();
	}
	
	public static void setIsmycat(String isMycat) {
		ISMYCAT.set(isMycat);
	}
}
