package com.ec.managementsystem.util;

public class KeyValue {

	protected int id;
	protected String value;
		
	public KeyValue(int id, String value) {
		super();
		this.id = id;
		this.value = value;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	} 
	
}
