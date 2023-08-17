package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aat.application.data.entity.ZJTElement;

public interface TripElementRepository extends JpaRepository<ZJTElement, Integer>
{

	List<ZJTElement> findByNameContainingIgnoreCase(String name);
}
