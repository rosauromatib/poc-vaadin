package com.aat.application.views;

import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.form.CommonForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pricing Type")
@Route(value = "pricingtype", layout = MainLayout.class)

public class PricingTypeView extends VerticalLayout {
    private CommonForm<ZJTPricingType> form;
    private final BaseEntityRepository<ZJTPricingType> repository;

    public PricingTypeView(BaseEntityRepository<ZJTPricingType> repository) {
        this.repository = repository;
        this.repository.setEntityClass(ZJTPricingType.class);
        configureForm();
        add(form);
    }

    private void configureForm() {
        form = new CommonForm<>(ZJTPricingType.class,new BaseEntityService<>(repository));
        form.setWidth("25em");
    }
}