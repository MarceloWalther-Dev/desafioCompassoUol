package com.uol.compasso.infra;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.uol.compasso.domain.model.Product;

@Repository
public class ProductRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;

	public List<Product> findProductNamePrice(String q, BigDecimal min_price, BigDecimal max_price) {

		var jpql = new StringBuilder();
		jpql.append("from Product where 0 = 0 ");
		
		var parameter = new HashMap<String, Object>();

		if (StringUtils.hasLength(q)) {
			jpql.append("and name like :q OR description like :q ");
			parameter.put("q", "%" + q + "%");
		}

		if (min_price != null) {
			jpql.append("and price >= :min_price ");
			parameter.put("min_price", min_price);
		}

		if (max_price != null) {
			jpql.append("and price <= :max_price ");
			parameter.put("max_price", max_price);
		}

		TypedQuery<Product> query = manager
				.createQuery(jpql.toString(), Product.class);
		
		parameter.forEach( (key, value) -> query.setParameter(key, value));
		
		return query.getResultList();
	}
}