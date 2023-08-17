package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aat.application.data.entity.ZJTComponentLine;
import com.aat.application.data.entity.ZJTPriceListItem;
import com.aat.application.data.entity.ZJTPricelist;
import com.aat.application.data.entity.ZJTProduct;

public interface PricelistItemRepository extends JpaRepository<ZJTPriceListItem, Integer>
{

	List<ZJTPriceListItem> findByPricelistIs(ZJTPricelist pricelist);
}
