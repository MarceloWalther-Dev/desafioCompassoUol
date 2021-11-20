package com.uol.compasso.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.uol.compasso.domain.model.Product;

@Component
public interface ProductService {

	Product findById(Long id);
	
	Product postProduct(Product product);
	
	Product putProduct(Long id, Product product);
	
	Product patchProduct(Long id, Map<String, Object> data);
	
	List<Product> listProduct();
	
	void deleteProduct(Long id);
}
