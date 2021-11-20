package com.uol.compasso.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Padrão RFC 7807
 *
 */

@JsonInclude(Include.NON_NULL)
public class ApiError {

	private Integer status;
	
	@JsonIgnore
	private String title;
	@JsonIgnore
	private String type;
	private String detail;

	public ApiError(Integer status, String type, String title, String detail) {
		this.status = status;
		this.type = type;
		this.title = title;
		this.detail = detail;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
