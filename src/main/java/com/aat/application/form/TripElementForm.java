package com.aat.application.form;

import java.util.List;

import com.aat.application.core.StandardForm;
import com.aat.application.data.entity.*;
import com.aat.application.data.service.ResourceCategoryService;
import com.aat.application.data.service.TripElementService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class TripElementForm extends StandardForm<ZJTElement, TripElementService> {
    public TripElementForm(TripElementService service) {
        super(ZJTElement.class, service);
        addClassName("demo-app-form");
    }
}