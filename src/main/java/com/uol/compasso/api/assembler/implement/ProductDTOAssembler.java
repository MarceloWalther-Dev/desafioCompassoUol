package com.uol.compasso.api.assembler.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uol.compasso.api.assembler.TransformingObjects;
import com.uol.compasso.api.model.ProductDTO;
import com.uol.compasso.domain.model.Product;

@Component
public class ProductDTOAssembler implements TransformingObjects<ProductDTO, Product>{

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public ProductDTO assemblerFromObject(Product product) {
		return mapper.map(product, ProductDTO.class);
	}
	
	@Override
	public List<ProductDTO> assemblerFromListObject(List<Product> products){
		return products.stream().map(product -> this.assemblerFromObject(product)).collect(Collectors.toList());
	}
}
