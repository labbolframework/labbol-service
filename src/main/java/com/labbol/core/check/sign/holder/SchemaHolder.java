package com.labbol.core.check.sign.holder;

public class SchemaHolder {
	
	private static final ThreadLocal<String> SCHEMA = new ThreadLocal<>();

	public static String getSchema() {
		return SCHEMA.get();
	}
	
	public static void setSchema(String schema) {
		SCHEMA.set(schema);
	}
}
