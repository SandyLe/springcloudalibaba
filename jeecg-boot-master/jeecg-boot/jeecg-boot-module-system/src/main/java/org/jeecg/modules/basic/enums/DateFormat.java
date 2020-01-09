package org.jeecg.modules.basic.enums;

public enum DateFormat implements AbstractEnum {

	yyMMdd(1, "yyMMdd"),

	yyMM(2, "yyMM"),

	yyyyMMdd(3, "yyyyMMdd"),

	yyyyMM(4, "yyyyMM");

	private Integer id;

	private String name;

	DateFormat(Integer id, String name) {

		this.id = id;
		this.name = name;
	}

	public Integer getId() {

		return id;
	}

	@Override
	public String getSid() {

		return String.valueOf(id);
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public String getCode() {
		return null;
	}

}
