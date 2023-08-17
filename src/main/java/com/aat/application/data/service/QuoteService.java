package com.aat.application.data.service;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aat.application.data.entity.ElementList;
import com.aat.application.data.entity.PriceListRow;
import com.aat.application.data.entity.TripType;
import com.aat.application.data.entity.ZJTPriceListItem;
import com.aat.application.data.entity.ZJTPricelist;
import com.aat.application.data.entity.ZJTProduct;
import com.aat.application.data.entity.ZJTResourceType;
import com.aat.application.data.repository.PricelistItemRepository;
import com.aat.application.data.repository.PricelistRepository;
import com.aat.application.data.repository.ProductRepository;
import com.aat.application.data.repository.ResourceTypeRepository;

@Service
public class QuoteService {

	private final PricelistRepository plRepository;
	private final PricelistItemRepository itemRepository;
	private final ProductRepository productRepository;
	private final ResourceTypeRepository rtRepo;
	
	private List<ZJTResourceType> resourceTypes;
	
	public QuoteService(PricelistRepository plRepository
			, PricelistItemRepository itemRepository
			, ProductRepository productRepository
			, ResourceTypeRepository rtRepository) {
		this.plRepository = plRepository;
		this.itemRepository = itemRepository;
		this.productRepository = productRepository;
		this.rtRepo = rtRepository;
	}
	
	public List<ZJTProduct> findAllTripItineraries(String stringFilter)
	{
		return productRepository.findByTripTypeIs(TripType.TI);
	}

	
	public void save(ZJTPricelist po)
	{
		plRepository.save(po);
	}
	
	public void delete(ZJTPricelist po)
	{
		plRepository.delete(po);
	}
	
	public void save(ZJTPriceListItem po)
	{
		itemRepository.save(po);
	}
	
	public void delete(ZJTPriceListItem po)
	{
		itemRepository.delete(po);
	}
	
	public List<ZJTResourceType> getResourceTypes()
	{
		if (resourceTypes == null) {
			resourceTypes = rtRepo.findAll(); 
		}
		return resourceTypes;
		
	}
	
	public List<ZJTPricelist> getPriceLists(String stringFilter)
	{
		if (stringFilter == null || stringFilter.isBlank()) {
			return plRepository.findAll();
		} else {
			return plRepository.findByNameContainingIgnoreCase(stringFilter);
		}
	}

	public List<PriceListRow> getTabulatedItems(ZJTPricelist pricelist)
	{
		List<ZJTProduct> tcs = productRepository.findByTripTypeIs(TripType.TC);
		List<ZJTPriceListItem> items = itemRepository.findByPricelistIs(pricelist);
		
		//Field[] fields = PriceListRow.class.getDeclaredFields();
		
		List<PriceListRow> rows = new ArrayList<PriceListRow>();
		
		List<ZJTResourceType> rts = getResourceTypes();	
		for (ZJTResourceType rt : rts)
		{
			PriceListRow row = new PriceListRow(rt, pricelist);
			ZJTProduct product;
			ZJTPriceListItem item;
			
			product = getProduct(tcs, rt.getName(), ElementList.VK);
			item = getItem(items, product, pricelist);
			row.setVehicleKMItem(item);

			product = getProduct(tcs, rt.getName(), ElementList.VH);
			item = getItem(items, product, pricelist);
			row.setVehicleHourItem(item);

			product = getProduct(tcs, rt.getName(), ElementList.DH);
			item = getItem(items, product, pricelist);
			row.setDriverHourItem(item);

			product = getProduct(tcs, rt.getName(), ElementList.PM);
			item = getItem(items, product, pricelist);
			row.setProfitMarginItem(item);

			product = getProduct(tcs, rt.getName(), ElementList.OH);
			item = getItem(items, product, pricelist);
			row.setOverHeadItem(item);

//			product = getProduct(tcs, rt.getName(), ElementList.AE);
//			item = getItem(items, product, pricelist);
//			row.set(item);

			rows.add(row);
		}
			

		return rows;
	}
	
	private ZJTProduct getProduct(List<ZJTProduct> tcs
			, String resourceTypeName, ElementList elementList)
	{
		ZJTProduct product = tcs.stream()
				.filter(p -> p.getResourceType().getName().equals(resourceTypeName) &&
						p.getTripElement().getElementlist() == elementList).findFirst().orElse(null);
		
		return product;
	}
	
	private ZJTPriceListItem getItem(List<ZJTPriceListItem> items, ZJTProduct product, ZJTPricelist pricelist)
	{
		ZJTPriceListItem item = items.stream()
				.filter(i -> i.getPricelist().getZjt_pricelist_id() == pricelist.getZjt_pricelist_id() 
						&& i.getProduct().getZjt_product_id() == product.getZjt_product_id()).findFirst().orElse(null);
		
		if (item == null) {
			item = new ZJTPriceListItem();
			item.setPricelist(pricelist);
			item.setProduct(product);
			item.setPrice(BigDecimal.ZERO);
		}
		items.add(item);
		return item;
	}
	
	public void persistTabulatedItems(List<PriceListRow> items)
	{
		
	}
}
