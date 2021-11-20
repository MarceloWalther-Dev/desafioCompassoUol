package com.uol.compasso.api.exceptionhandler;

public enum ErrorType {
	
	VALUE_INVALID("/valor-invalido","Valor passado está desacordo com a regra"),
	INVALID_PARAMETER("/parametro-de-url-invalido", "Parâmetro de url inválido"),
	BAD_FORMED_REQUISITION("/requisição-mal-formada", "Corpo da requisição mal formado"),
	INCOMPREHENSIBLE_MESSAGE("/mensagem-incompreensivel", "Mensagem incompreensivel"),
	PRODUCT_NOT_FOUND("/product-nao-encontrado", "Product não encontrado");

	private String title;
	private String uri;

	private ErrorType(String path, String title) {
		this.uri = "https://compassoUol.com.br" + path;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public String getUri() {
		return uri;
	}
}
