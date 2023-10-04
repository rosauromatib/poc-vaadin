package com.aat.application.views;

import com.aat.application.data.service.ResourceCategoryService;
import com.aat.application.form.ResourceCategoryForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Resource Category")
@Route(value = "resourcecategory", layout = MainLayout.class)
public class ResourceCategoryView extends VerticalLayout {

    private ResourceCategoryForm form;
    private final ResourceCategoryService service;

    public ResourceCategoryView(ResourceCategoryService service) {
        this.service = service;

        configureForm();
        add(form);
    }

    private void configureForm() {
        form = new ResourceCategoryForm(service);
        form.setWidth("25em");

    }
}