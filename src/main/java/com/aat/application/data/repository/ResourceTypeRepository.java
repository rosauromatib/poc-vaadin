package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aat.application.data.entity.ZJTResourceType;

public interface ResourceTypeRepository extends JpaRepository<ZJTResourceType, Integer>
{
	/*
	@Query(value="select a.zjt_resourcetype_id, a.name, a.description, a.zjt_resourcecategory_id, c.name as cname " + 
			"from zjt_resourcetype a inner join zjt_resourcecategory c on a.zjt_resourcecategory_id = c.zjt_resourcecategory_id  " +
			"where (lower(a.name) like lower (concat('%', :searchTerm, '%'))) " +
			"or (lower(a.description) like lower (concat('%', :searchTerm, '%')))",
			nativeQuery = true)
	List<ZJTResourceType> search(@Param("searchTerm") String searchTerm);
*/
//	List<ZJTResourceType> findByNameOrDescriptionContaining(String searchTerm);
	
	List<ZJTResourceType> findByNameContainingIgnoreCase(String name);
}
