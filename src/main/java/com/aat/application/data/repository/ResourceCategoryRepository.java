package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.aat.application.data.entity.ZJTResourceCategory;

public interface ResourceCategoryRepository extends JpaRepository<ZJTResourceCategory, Integer>
{

	@Query(value="select a.zjt_resourcecategory_id, a.name, a.description from zjt_resourcecategory a " +
			"where (lower(a.name) like lower (concat('%', :searchTerm, '%'))) " +
			"or (lower(a.description) like lower (concat('%', :searchTerm, '%')))",
			nativeQuery = true)
	List<ZJTResourceCategory> search(@Param("searchTerm") String searchTerm);
}
