package com.aat.application.data.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.aat.application.data.entity.*;
import com.aat.application.data.repository.BaseEntityRepository;
import org.springframework.stereotype.Service;

import com.aat.application.data.repository.PricelistItemRepository;
import com.aat.application.data.repository.PricelistRepository;
import com.aat.application.data.repository.ProductRepository;

@Service
public class PricelistService {
    private final PricelistRepository plRepository;
    private final PricelistItemRepository itemRepository;
    private final ProductRepository productRepository;
    private final BaseEntityRepository<ZJTResourceType> rtRepo;

    private List<ZJTResourceType> resourceTypes;

    public PricelistService(PricelistRepository plRepository
            , PricelistItemRepository itemRepository
            , ProductRepository productRepository
            , BaseEntityRepository<ZJTResourceType> rtRepository) {
        this.plRepository = plRepository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
        this.rtRepo = rtRepository;
    }

    public void save(ZJTPricelist po) {
        plRepository.save(po);
    }

    public void delete(ZJTPricelist po) {
        plRepository.delete(po);
    }

    public void save(ZJTPriceListItem po) {
        itemRepository.save(po);
    }

    public void delete(ZJTPriceListItem po) {
        itemRepository.delete(po);
    }

    public List<ZJTResourceType> getResourceTypes() {
        if (resourceTypes == null) {
            resourceTypes = rtRepo.findAll(null);
        }
        return resourceTypes;

    }

    public List<ZJTPricelist> getPriceLists(String stringFilter) {
        if (stringFilter == null || stringFilter.isBlank()) {
            return plRepository.findAll();
        } else {
            return plRepository.findByNameContainingIgnoreCase(stringFilter);
        }
    }

    public void updateTabulatedItems(List<ZJXPriceListRow> rows, ZJTPricelist pricelist) {
        for (ZJXPriceListRow row :
                rows) {
            this.updateTabulatedItem(row, pricelist);
        }
    }

    public void updateTabulatedItem(ZJXPriceListRow row, ZJTPricelist pricelist) {
        List<ZJTPriceListItem> items = itemRepository.findByPricelistIs(pricelist);

        List<ZJTProduct> tcs = productRepository.findByTripTypeIs(ZJETripType.TC);

        ZJTProduct product;
        ZJTPriceListItem item;
        product = getProduct(tcs, row.getResourceType().getName(), ZJEElementList.VK);
        item = getItem(items, product, pricelist);
        updatePrice(item, row.getVehicleKM());

        product = getProduct(tcs, row.getResourceType().getName(), ZJEElementList.VH);
        item = getItem(items, product, pricelist);
        updatePrice(item, row.getVehicleHours());

        product = getProduct(tcs, row.getResourceType().getName(), ZJEElementList.DH);
        item = getItem(items, product, pricelist);
        updatePrice(item, row.getDriverHours());

        product = getProduct(tcs, row.getResourceType().getName(), ZJEElementList.PM);
        item = getItem(items, product, pricelist);
        updatePrice(item, row.getProfitMargin());

        product = getProduct(tcs, row.getResourceType().getName(), ZJEElementList.OH);
        item = getItem(items, product, pricelist);
        updatePrice(item, row.getOverHead());

        product = getProduct(tcs, row.getName(), ZJEElementList.AE);
        item = getItem(items, product, pricelist);
        updatePrice(item, row.getOverHead());

    }

    private void updatePrice(ZJTPriceListItem item, BigDecimal price) {
        item.setPrice(price);
        itemRepository.save(item);
    }

    public List<ZJXPriceListRow> getTabulatedItems(ZJTPricelist pricelist) {
        List<ZJTProduct> tcs = productRepository.findByTripTypeIs(ZJETripType.TC);
        List<ZJTPriceListItem> items = itemRepository.findByPricelistIs(pricelist);
        //Field[] fields = PriceListRow.class.getDeclaredFields();

        List<ZJXPriceListRow> rows = new ArrayList<ZJXPriceListRow>();

        List<ZJTResourceType> rts = getResourceTypes();
        for (ZJTResourceType rt : rts) {
            ZJXPriceListRow row = new ZJXPriceListRow(rt, pricelist);
            ZJTProduct product;
            ZJTPriceListItem item;

            product = getProduct(tcs, rt.getName(), ZJEElementList.VK);
            item = getItem(items, product, pricelist);
            row.setVehicleKMItem(item);

            product = getProduct(tcs, rt.getName(), ZJEElementList.VH);
            item = getItem(items, product, pricelist);
            row.setVehicleHourItem(item);

            product = getProduct(tcs, rt.getName(), ZJEElementList.DH);
            item = getItem(items, product, pricelist);
            row.setDriverHourItem(item);

            product = getProduct(tcs, rt.getName(), ZJEElementList.PM);
            item = getItem(items, product, pricelist);
            row.setProfitMarginItem(item);

            product = getProduct(tcs, rt.getName(), ZJEElementList.OH);
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
            , String resourceTypeName, ZJEElementList ZJEElementList) {
        ZJTProduct product = tcs.stream()
                .filter(p -> p.getResourceType().getName().equals(resourceTypeName) &&
                        p.getTripElement().getElementlist() == ZJEElementList).findFirst().orElse(null);

        return product;
    }

    private ZJTPriceListItem getItem(List<ZJTPriceListItem> items, ZJTProduct product, ZJTPricelist pricelist) {
        ZJTPriceListItem item;
        if (product != null)
            item = items.stream()
                    .filter(i -> i.getPricelist().getZjt_pricelist_id() == pricelist.getZjt_pricelist_id()
                            && i.getProduct() != null && i.getProduct().getZjt_product_id() == product.getZjt_product_id()).findFirst().orElse(null);
        else
            item = null;

        if (item == null) {
            item = new ZJTPriceListItem();
            item.setProduct(product);
            item.setPricelist(pricelist);
            item.setPrice(BigDecimal.ZERO);
            if (pricelist != null)
                itemRepository.save(item);
            return null;
        }
        items.add(item);
        return item;
    }

    public void persistTabulatedItems(List<ZJXPriceListRow> items) {

    }
}
