package com.aat.application.views;

import com.aat.application.data.entity.ZJTResourceType;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.form.CommonForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Resource Type")
@Route(value = "resourcetype", layout = MainLayout.class)

public class ResourceTypeView extends VerticalLayout {
    private CommonForm<ZJTResourceType> form;
    private final BaseEntityRepository<ZJTResourceType> repository;

    public ResourceTypeView(BaseEntityRepository<ZJTResourceType> repository) {
        this.repository = repository;
        this.repository.setEntityClass(ZJTResourceType.class);
        configureForm();
        add(form);
    }

    private void configureForm() {
        form = new CommonForm<>(ZJTResourceType.class,new BaseEntityService<>(repository));
        form.setWidth("25em");
    }
}