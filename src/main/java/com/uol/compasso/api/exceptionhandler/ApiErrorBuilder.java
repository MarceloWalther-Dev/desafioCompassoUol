package com.uol.compasso.api.exceptionhandler;

public class ApiErrorBuilder {
	
	//Mandatory
	private Integer status;

	//Optionals
	private String title;
	private String type;
	private String detail;
	
	public ApiErrorBuilder(Integer status) {
		this.status = status;
	}

	public ApiErrorBuilder setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public ApiErrorBuilder setType(String type) {
		this.type = type;
		return this;
	}
	
	public ApiErrorBuilder setDetail(String detail) {
		this.detail = detail;
		return this;
	}
	
	public ApiError build() {
		return new ApiError(status,title,type,detail);
	}
}
