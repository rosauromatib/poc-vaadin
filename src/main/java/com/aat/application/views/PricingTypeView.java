package com.aat.application.views;

import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.service.PricingTypeService;
import com.aat.application.form.PricingTypeForm;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@PageTitle("Pricing Type")
@Route(value = "pricingtype", layout = MainLayout.class)

public class PricingTypeView extends VerticalLayout {

    private PricingTypeForm form;
    private final PricingTypeService service;
    Span sp = new Span("Here is: ");

    public PricingTypeView(PricingTypeService service) {
        this.service = service;

        configureForm();
        add(sp, form);
    }

    private void configureForm() {
        form = new PricingTypeForm(service);
        form.setWidth("25em");

    }
}
