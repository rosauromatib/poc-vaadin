package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTPricelist;

public interface PricelistRepository extends JpaRepository<ZJTPricelist, Integer>
{

	List<ZJTPricelist> findByNameContainingIgnoreCase(String name);
}
