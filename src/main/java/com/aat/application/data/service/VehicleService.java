package com.aat.application.data.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.repository.VehicleRepository;

@Service
public class VehicleService {

	public VehicleService(VehicleRepository repository) {
		this.repository = repository;
	}

	private final VehicleRepository repository;
	
	public List<ZJTVehicle> findAll() {
		return repository.findAll();
	}

	public void save(ZJTVehicle po) {
		repository.save(po);
	}
	
	public void delete(ZJTVehicle po) {
		repository.delete(po);
	}
	
	public List<ZJTVehicle> findAllVehicles(String stringFilter)
	{
		if (stringFilter == null || stringFilter.isEmpty()) {
			return repository.findAll();
		} else {
			return repository.findByVehicleidContainingIgnoreCase(stringFilter);
		}
	}
	
}
