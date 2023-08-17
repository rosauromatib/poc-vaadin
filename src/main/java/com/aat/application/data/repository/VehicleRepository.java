package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aat.application.data.entity.ZJTVehicle;

public interface VehicleRepository extends JpaRepository<ZJTVehicle, Integer>
{
	List<ZJTVehicle> findByVehicleidContainingIgnoreCase(String name);
}
