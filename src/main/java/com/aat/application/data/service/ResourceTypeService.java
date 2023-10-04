package com.aat.application.data.service;

import java.util.List;

import com.aat.application.core.ZJTService;
import org.springframework.stereotype.Service;

import com.aat.application.data.entity.ZJTResourceCategory;
import com.aat.application.data.entity.ZJTResourceType;
import com.aat.application.data.repository.ResourceCategoryRepository;
import com.aat.application.data.repository.ResourceTypeRepository;

@Service
public class ResourceTypeService implements ZJTService<ZJTResourceType> {

	public ResourceTypeService(ResourceTypeRepository repository, ResourceCategoryRepository rcRepo) {
		this.repository = repository;
		this.rcRepo = rcRepo;
	}

	private final ResourceTypeRepository repository;
	private final ResourceCategoryRepository rcRepo;
	
	public void save(ZJTResourceType po) {
		repository.save(po);
	}
	
	public void delete(ZJTResourceType po) {
		repository.delete(po);
	}
	
	public List<ZJTResourceType> findAll(String stringFilter)
	{
		if (stringFilter == null || stringFilter.isEmpty()) {
			return repository.findAll();
		} else {
//			return repository.search(stringFilter);
			return repository.findByNameContainingIgnoreCase(stringFilter);
		}
	}
	
	public List<ZJTResourceCategory> getResourceCategories()
	{
		return rcRepo.findAll();
	}
}
