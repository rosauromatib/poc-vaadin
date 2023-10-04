package com.aat.application.data.service;

import java.util.List;

import com.aat.application.core.ZJTService;
import org.springframework.stereotype.Service;

import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.entity.ZJTResourceCategory;
import com.aat.application.data.repository.PricingTypeRepository;
import com.aat.application.data.repository.ResourceCategoryRepository;

@Service
public class ResourceCategoryService implements ZJTService<ZJTResourceCategory> {

	public ResourceCategoryService(ResourceCategoryRepository repository) {
		this.repository = repository;
	}

	private final ResourceCategoryRepository repository;
	
	public void save(ZJTResourceCategory po) {
		repository.save(po);
	}
	
	public void delete(ZJTResourceCategory po) {
		repository.delete(po);
	}
	
	public List<ZJTResourceCategory> findAll(String stringFilter)
	{
		if (stringFilter == null || stringFilter.isEmpty()) {
			return repository.findAll();
		} else {
			return repository.search(stringFilter);
		}
	}
}
