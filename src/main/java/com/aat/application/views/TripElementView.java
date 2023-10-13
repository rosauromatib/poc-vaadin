package com.aat.application.views;

import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.form.CommonForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Trip Element")
@Route(value = "tripelement", layout = MainLayout.class)

public class TripElementView extends VerticalLayout {
    private CommonForm<ZJTElement> form;
    private final BaseEntityRepository<ZJTElement> repository;

    public TripElementView(BaseEntityRepository<ZJTElement> repository) {
        this.repository = repository;
        this.repository.setEntityClass(ZJTElement.class);
        configureForm();
        add(form);
    }
    private void configureForm() {
        form = new CommonForm<>(ZJTElement.class,new BaseEntityService<>(repository));
        form.setWidth("25em");
    }
}