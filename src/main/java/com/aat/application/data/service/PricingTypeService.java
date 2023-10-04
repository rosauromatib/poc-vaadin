package com.aat.application.data.service;

import java.util.List;

import com.aat.application.core.ZJTService;
import org.springframework.stereotype.Service;

import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.repository.PricingTypeRepository;

@Service
public class PricingTypeService implements ZJTService<ZJTPricingType> {

	public PricingTypeService(PricingTypeRepository repository) {
		this.repository = repository;
	}

	private final PricingTypeRepository repository;
	
	public void save(ZJTPricingType po) {
		repository.save(po);
	}
	
	public void delete(ZJTPricingType po) {
		repository.delete(po);
	}
	
	public List<ZJTPricingType> findAll(String stringFilter)
	{
		if (stringFilter == null || stringFilter.isEmpty()) {
			return repository.findAll();
		} else {
			return repository.search(stringFilter);
		}
	}
}
