package com.uol.compasso.domain.exception;

public class ProductNotFoundException extends EntityException{

	private static final long serialVersionUID = 1L;
	

	public ProductNotFoundException(String message) {
		super(message);
	}
	
	public ProductNotFoundException(Long productId) {
		this(String.format("Não existe um cadastro do produto com o código %d", productId));
	}
}
