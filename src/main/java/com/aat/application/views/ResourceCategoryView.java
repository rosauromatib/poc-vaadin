package com.aat.application.views;

import com.aat.application.data.entity.ZJTResourceCategory;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.form.CommonForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Resource Category")
@Route(value = "resourcecategory", layout = MainLayout.class)
public class ResourceCategoryView extends VerticalLayout {

    private CommonForm<ZJTResourceCategory>  form;
    private final BaseEntityRepository<ZJTResourceCategory> repository;

    public ResourceCategoryView(BaseEntityRepository<ZJTResourceCategory> repository) {
        this.repository = repository;
        this.repository.setEntityClass(ZJTResourceCategory.class);
        configureForm();
        add(form);
    }

    private void configureForm() {
        form = new CommonForm<>(ZJTResourceCategory.class,new BaseEntityService<>(repository));
        form.setWidth("25em");
    }
}