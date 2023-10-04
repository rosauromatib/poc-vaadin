package com.aat.application.views;

import com.aat.application.data.service.PricingTypeService;
import com.aat.application.form.PricingTypeForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pricing Type")
@Route(value = "pricingtype", layout = MainLayout.class)

public class PricingTypeView extends VerticalLayout {
    private PricingTypeForm form;
    private final PricingTypeService service;

    public PricingTypeView(PricingTypeService service) {
        this.service = service;
        configureForm();
        add(form);
    }

    private void configureForm() {
        form = new PricingTypeForm(service);
        form.setWidth("25em");
    }
}