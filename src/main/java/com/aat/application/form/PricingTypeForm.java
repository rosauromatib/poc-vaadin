package com.aat.application.form;

import com.aat.application.core.StandardForm;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.service.PricingTypeService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class PricingTypeForm extends StandardForm<ZJTPricingType, PricingTypeService> {

	public PricingTypeForm(PricingTypeService service)
	{
		super(ZJTPricingType.class, service);
		addClassName("demo-app-form");
	}
}


