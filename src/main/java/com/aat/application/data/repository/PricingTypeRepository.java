package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aat.application.data.entity.ZJTPricingType;

public interface PricingTypeRepository extends JpaRepository<ZJTPricingType, Integer>
{
	@Query(value="select a.zjt_pricingtype_id, a.name, a.description from zjt_pricingtype a " +
			"where lower(a.name) like lower (concat('%', :searchTerm, '%'))",
			nativeQuery = true)
	List<ZJTPricingType> search(@Param("searchTerm") String searchTerm);

}
