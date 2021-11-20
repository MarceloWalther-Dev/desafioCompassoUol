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
public class ProductModelAssembler implements TransformingObjects<Product, ProductDTO>{

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Product assemblerFromObject(ProductDTO source) {
		return mapper.map(source, Product.class);
	}

	@Override
	public List<Product> assemblerFromListObject(List<ProductDTO> sources) {
		return sources.stream().map(source -> this.assemblerFromObject(source)).collect(Collectors.toList());
	}

}
