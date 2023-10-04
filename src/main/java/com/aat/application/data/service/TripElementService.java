package com.aat.application.data.service;

import java.util.List;

import com.aat.application.core.ZJTService;
import org.springframework.stereotype.Service;

import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.repository.PricingTypeRepository;
import com.aat.application.data.repository.TripElementRepository;

@Service
public class TripElementService implements ZJTService<ZJTElement> {

	public TripElementService(TripElementRepository repository, PricingTypeRepository ptRepo) {
		this.repository = repository;
		this.ptRepo = ptRepo;
	}

	private final TripElementRepository repository;
	private final PricingTypeRepository ptRepo;
	
	public List<ZJTElement> findAll() {
		return repository.findAll();
	}

	@Override
	public List<ZJTElement> findAll(String filter) {
		if (filter == null || filter.isEmpty()) {
			return repository.findAll();
		} else {
			return repository.findByNameContainingIgnoreCase(filter);
		}
	}

	public void save(ZJTElement po) {
		repository.save(po);
	}
	
	public void delete(ZJTElement po) {
		repository.delete(po);
	}

	public List<ZJTPricingType> getPricingTypes()
	{
		return ptRepo.findAll();
	}
}
