package com.aat.application.data.service;

import org.springframework.stereotype.Service;

import com.aat.application.data.repository.ProductRepository;

@Service
public class DemoService {

	public DemoService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	private final ProductRepository productRepository;
	
//	public List<Product> findAllProducts() {
//		return productRepository.findAll();
//	}
//
//	public void saveProduct(Product product) {
//		productRepository.save(product);
//	}
//	
//	public void deleteProduct(Product product) {
//		productRepository.delete(product);
//	}
}
