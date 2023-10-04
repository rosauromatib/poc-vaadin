package com.aat.application.views;

import com.aat.application.data.service.ResourceTypeService;
import com.aat.application.form.ResourceTypeForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Resource Type")
@Route(value = "resourcetype", layout = MainLayout.class)

public class ResourceTypeView extends VerticalLayout {
    private ResourceTypeForm form;
    private final ResourceTypeService service;

    public ResourceTypeView(ResourceTypeService service) {
        this.service = service;

        configureForm();
        add(form);
    }

    private void configureForm() {
        form = new ResourceTypeForm(service);
        form.setWidth("25em");

    }
}