package com.aat.application.data.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.repository.PricingTypeRepository;
import com.aat.application.data.repository.TripElementRepository;

@Service
public class TripElementService {

	public TripElementService(TripElementRepository repository, PricingTypeRepository ptRepo) {
		this.repository = repository;
		this.ptRepo = ptRepo;
	}

	private final TripElementRepository repository;
	private final PricingTypeRepository ptRepo;
	
	public List<ZJTElement> findAll() {
		return repository.findAll();
	}

	public void save(ZJTElement po) {
		repository.save(po);
	}
	
	public void delete(ZJTElement po) {
		repository.delete(po);
	}
	
	public List<ZJTElement> findAllElements(String stringFilter)
	{
		if (stringFilter == null || stringFilter.isEmpty()) {
			return repository.findAll();
		} else {
			return repository.findByNameContainingIgnoreCase(stringFilter);
		}
	}
	
	public List<ZJTPricingType> getPricingTypes()
	{
		return ptRepo.findAll();
	}
}
