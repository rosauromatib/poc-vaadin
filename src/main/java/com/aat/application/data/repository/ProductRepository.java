package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aat.application.data.entity.ZJETripType;
import com.aat.application.data.entity.ZJTProduct;

public interface ProductRepository extends JpaRepository<ZJTProduct, Integer>
{
	
	List<ZJTProduct> findByNameContainingIgnoreCase(String name);
	
	List<ZJTProduct> findByTripTypeIs(ZJETripType triptype);
	
	List<ZJTProduct> findByNameContainingIgnoreCaseAndTripTypeIs(String name, ZJETripType triptype);
	
	@Query(value = 
			"SELECT array_to_json(array_agg(row_to_json (r))) || '' FROM ( " + 
			"select it.name as itinerary, zp.trip_type, " + 
			"to_char( zp.triptime, ' HH24:MI-') || to_char( zp.triptime + (zp.tripleghour * 60  * interval '1 minute') , ' HH24:MI ') || zp.\"name\" as tripleg " + 
			", ze.\"name\"  as elementname " + 
			", zc.qty "  + 
			"from zjt_product it\n" + 
			"left join zjt_componentline itl ON it.zjt_product_id = itl.parent_id " + 
			"left join zjt_product zp  on itl.product_id = zp.zjt_product_id " + 
			"left join zjt_componentline zc ON zp.zjt_product_id  = zc.parent_id and zp.trip_type = 'LI' " + 
			"left join zjt_element ze on zc.zjt_element_id = ze.zjt_element_id " + 
			"where it.zjt_product_id = ?1  " + 
			"order by it.name, zp.triptime, zp.name" + 
			") r"
//			"select name from zjt_product zp  where zjt_product_id = ?1"
			, nativeQuery = true)
	String getJSONLegs(Integer itineraryID);
}
