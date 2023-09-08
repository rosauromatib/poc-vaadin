package com.aat.application.data.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.aat.application.data.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.aat.application.data.repository.ComponentLineRepository;
import com.aat.application.data.repository.ProductRepository;
import com.aat.application.data.repository.ResourceTypeRepository;
import com.aat.application.data.repository.TripElementRepository;

import jakarta.persistence.EntityManager;

@Service
public class ProductService {

    public ProductService(ProductRepository repository
            , ResourceTypeRepository rtRepo
            , TripElementRepository teRepo
            , ComponentLineRepository clRepo
    ) {
        this.repository = repository;
        this.rtRepo = rtRepo;
        this.teRepo = teRepo;
        this.clRepo = clRepo;
    }
    @Autowired
    private final ProductRepository repository;
    private final TripElementRepository teRepo;
    private final ResourceTypeRepository rtRepo;
    private final ComponentLineRepository clRepo;

    @Autowired
    private EntityManager em;

    private List<ZJTElement> allTripElements = null;

    public void saveParentWithChildren(ZJTProduct parent, List<ZJTPriceListItem> children) {
        parent.setChildren1(children);
        repository.save(parent);
    }
    public void save(ZJTProduct po) {
        try {
            repository.save(po);
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate key value violation
            // You can choose to update the existing record or throw an exception
            // For example, you can update the existing record with the new values
            if(po != null)
                repository.saveAndFlush(po);
        }
    }

    public void delete(ZJTProduct po) {
        repository.delete(po);
    }

    public void saveLine(ZJTComponentLine line) {
        clRepo.save(line);
    }

    public List<ZJTProduct> findAllProducts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return repository.findAll();
        } else {
            return repository.findByNameContainingIgnoreCase(stringFilter);
        }
    }

    public List<ZJTProduct> findAllTripComponent(String stringFilter) {
//		return repository.findByTripTypeIs(TripType.TC);
        return repository.findByNameContainingIgnoreCaseAndTripTypeIs(stringFilter, TripType.TC);
    }

    public List<ZJTProduct> findAllTripItineraries(String stringFilter) {
        return repository.findByTripTypeIs(TripType.TI);
//		return repository.findByNameContainingIgnoreCaseAndTripTypeIs(stringFilter, TripType.TI);
    }

    public List<ZJTElement> getTripElements() {
        if (allTripElements == null) {
            allTripElements = teRepo.findAll();
        }
        return allTripElements;
    }

    public List<ZJTResourceType> getResourceTypes() {
        return rtRepo.findAll();
    }

    public void populateComponents() {
        List<ZJTResourceType> resourceTypes = getResourceTypes();
        List<ZJTElement> tripElements = getTripElements();
        List<ZJTProduct> components = findAllTripComponent(null);

        for (ZJTResourceType resourceType : resourceTypes) {
            for (ZJTElement tripElement : tripElements) {
                ZJTProduct product = new ZJTProduct();
                product.setTripElement(tripElement);
                product.setResourceType(resourceType);
                product.setTripType(TripType.TC);
                product.setName(resourceType.getName() + " - " + tripElement.getName());
//				System.out.println(resourceType.getName() +" - " + tripElement.getName());

                save(product);
            }
        }

//		System.out.println("");
    }

    public void setLegElements(ZJTProduct parent, List<ZJTElement> elements) {
        List<ZJTElement> allElements = new ArrayList<>(allTripElements);

        List<ZJTComponentLine> lines = clRepo.findByParentIs(parent);

        for (ZJTElement element : elements) {
            allElements.remove(element);
            ZJTComponentLine line = find(lines, element);
            BigDecimal qty = null;
            if (line == null) {
                //add it to the component line
                line = new ZJTComponentLine();
                line.setParent(parent);
                line.setTripelement(element);
                line.setUom(element.getUom());
            }
            switch (element.getUom()) {
                case K:
                    qty = parent.getTriplegkm();
                    break;
                case H:
                    qty = parent.getTripleghour();
                    break;
                case E:
                    qty = parent.getTriplegeach();
                    break;
            }
            if (qty == null || qty.signum() == 0) {
                continue;
            }
            line.setQty(qty);

            clRepo.save(line);
        }
        //Delete unticked items here
        for (ZJTElement element : allElements) {
            ZJTComponentLine line = find(lines, element);
            if (line != null) {
                //TODO - should warn?
                clRepo.delete(line); // no longer neede
            }
        }

    }

    public List<ZJTProduct> getItineraryLegs(ZJTProduct parent) {
        List<ZJTComponentLine> lines = clRepo.findByParentIs(parent);
        if (lines.size() == 0) {
            return null;
        }

        List<ZJTProduct> legs = new ArrayList<ZJTProduct>();

        for (ZJTComponentLine line : lines) {
            legs.add(line.getProduct());
        }
        return legs;

    }

    public List<ZJTElement> getLegElements(ZJTProduct parent, List<ZJTElement> allElements) {
        List<ZJTComponentLine> lines = clRepo.findByParentIs(parent);

        List<ZJTElement> selected = new ArrayList<>();

        for (ZJTElement element : allElements) {
            ZJTComponentLine line = find(lines, element);

            if (line != null) {
                selected.add(element);
            }
        }
        return selected;
    }

    private ZJTComponentLine find(List<ZJTComponentLine> lines, ZJTElement te) {
        return lines.stream().filter(p -> p.getTripelement().getZjt_element_id() == te.getZjt_element_id()).findFirst().orElse(null);

    }

    public String getLegsInJSON(Integer itineraryID) {

        return repository.getJSONLegs(itineraryID);
    }
}
