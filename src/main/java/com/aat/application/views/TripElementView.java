package com.aat.application.views;

import com.aat.application.data.service.TripElementService;
import com.aat.application.form.TripElementForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Trip Element")
@Route(value = "tripelement", layout = MainLayout.class)

public class TripElementView extends VerticalLayout {
    private TripElementForm form;
    private final TripElementService service;

    public TripElementView(TripElementService service) {
        this.service = service;
        configureForm();
        add(form);
    }
    private void configureForm() {
        form = new TripElementForm(service);
        form.setWidth("25em");
    }
}