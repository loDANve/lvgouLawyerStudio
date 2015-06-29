package com.lvgou.lawyer.studio.been;

import java.io.Serializable;

public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name;
	public String pinyi;
	public int index;

	public City(String name, String pinyi) {
		super();
		this.name = name;
		this.pinyi = pinyi;
	}

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
