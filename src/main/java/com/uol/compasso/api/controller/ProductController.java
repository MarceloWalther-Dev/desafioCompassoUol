package com.uol.compasso.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uol.compasso.api.assembler.implement.ProductDTOAssembler;
import com.uol.compasso.api.assembler.implement.ProductModelAssembler;
import com.uol.compasso.api.model.ProductDTO;
import com.uol.compasso.domain.model.Product;
import com.uol.compasso.domain.repository.ProductRepository;
import com.uol.compasso.domain.service.ProductService;

@RestController
@RequestMapping( value = "/products")
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	ProductDTOAssembler convertDto;
	
	@Autowired
	ProductModelAssembler convertModel;
	
	private Product productModel;
	
	private List<ProductDTO> listDto;
	
	private ProductDTO dto;

	@GetMapping
	public ResponseEntity<List<ProductDTO>> getListProducts(){
		listDto = convertDto.assemblerFromListObject(service.listProduct());
		return ResponseEntity.ok(listDto);
	}
	
	@GetMapping(value = "/{productId}")
	public ResponseEntity<ProductDTO> getById(@PathVariable Long productId){
		dto = convertDto.assemblerFromObject(service.findById(productId));
		return ResponseEntity.ok(dto);
	}
	
		
	@GetMapping(value = "/search")
	public ResponseEntity<List<ProductDTO>> getFilterNameOrDescription(String q, BigDecimal min_price,
			BigDecimal max_price){
		listDto = convertDto.assemblerFromListObject(repository.findProductNamePrice(q, min_price, max_price));
		return ResponseEntity.ok(listDto);
	}
	
	@PostMapping()
	public ResponseEntity<ProductDTO> saveProduct(@RequestBody @Valid ProductDTO product){
		
		productModel = service.postProduct(convertModel.assemblerFromObject(product));
		dto = convertDto.assemblerFromObject(productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}
	
	@PutMapping(value = "/{productId}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO product){
		productModel = convertModel.assemblerFromObject(product);
		dto = convertDto.assemblerFromObject(service.putProduct(productId, productModel));
		return ResponseEntity.ok(dto);
	}
	
	
	@PatchMapping(value = "/{productId}")
	public ResponseEntity<ProductDTO> updatePartial(@PathVariable Long productId, @RequestBody Map<String, Object> campos){
		Product patchProduct = service.patchProduct(productId, campos);
		dto = convertDto.assemblerFromObject(patchProduct);
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping(value = "/{productId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deletedProduct(@PathVariable Long productId){
		service.deleteProduct(productId);
	}
	
	
}
