package com.uol.compasso.domain.service.implement;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uol.compasso.domain.exception.ProductNotFoundException;
import com.uol.compasso.domain.model.Product;
import com.uol.compasso.domain.repository.ProductRepository;
import com.uol.compasso.domain.service.ProductService;

@Service
public class ProductServiceImplement implements ProductService {

	@Autowired
	private ProductRepository repository;

	@Override
	public Product findById(Long id) {
		return this.isExistsBring(id);
	}

	@Override
	public Product postProduct(Product product) {
		return repository.save(product);
	}

	@Override
	public List<Product> listProduct() {
		return repository.findAll();
	}

	@Override
	public void deleteProduct(Long id) {
		this.isExistsBring(id);
		repository.deleteById(id);
	}

	@Override
	public Product putProduct(Long id, Product product) {
		Product productExists = this.isExistsBring(id);
		BeanUtils.copyProperties(product, productExists, "id");
		return repository.save(productExists);
	}

	@Override
	public Product patchProduct(Long id, Map<String, Object> data) {
		Product product = this.isExistsBring(id);
		
		product = merge(data, product);
		
		return this.postProduct(product);
	}

	private Product isExistsBring(Long productId) {
		return repository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
		
	}
	
	private Product merge(Map<String, Object> data, Product productFinal) {
		var objMapper = new ObjectMapper();
		
		
		//Conversão do map para um objeto especifico para evitar de ficar convertendo os tipos bigDecimal  
		var product = objMapper.convertValue(data, Product.class);
		
		data.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(Product.class, key);
			

			 //atributo do product é private, necessario liberar o acesso.
			field.setAccessible(true);
			
			//captura o valor do campo fiel no objeto convertido pelo object Mapper
			Object valueNew = ReflectionUtils.getField(field, product);
			ReflectionUtils.setField(field, productFinal, valueNew);
		});
		
		return productFinal;
	}
}
