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

	private static final long serialVersionUID = -5183438338263448739L;

	Binder<ZJTPricingType> binder = new BeanValidationBinder<>(ZJTPricingType.class); 
	
	TextField name = new TextField("Name");
	TextField description = new TextField("Description");

	Button save = new Button("Save");
//	Button delete = new Button("Delete");
	Button close = new Button("Cancel");
	  
	public PricingTypeForm(PricingTypeService service)
	{
		super(ZJTPricingType.class, service);
		addClassName("demo-app-form");
		
//		add(name, description, createButtonsLayout());
//		binder.bindInstanceFields(this);
	}
	  
	private HorizontalLayout createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		save.addClickShortcut(Key.ENTER);

		close.addClickShortcut(Key.ESCAPE);

		save.addClickListener(event -> validateAndSave());
//		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));
//		return new HorizontalLayout(save, delete, close);
		return new HorizontalLayout(save, close);

	}
	
	private void validateAndSave() {
		if (binder.isValid()) {
			fireEvent(new SaveEvent(this,  binder.getBean()));
		}
	}

	public void setBean(ZJTPricingType bean)
	{
		binder.setBean(bean);
	}
	
	// Events
	public static abstract class PricingTypeFormEvent extends ComponentEvent<PricingTypeForm> {
	  private final ZJTPricingType bean;

	  protected PricingTypeFormEvent(PricingTypeForm source, ZJTPricingType bean) { 


	    super(source, false);
	    this.bean = bean;
	  }

	  public ZJTPricingType getBean() {
	    return bean;
	  }
	}

	public static class SaveEvent extends PricingTypeFormEvent {
	  SaveEvent(PricingTypeForm source, ZJTPricingType bean) {
	    super(source, bean);
	  }
	}

	public static class DeleteEvent extends PricingTypeFormEvent {
	  DeleteEvent(PricingTypeForm source, ZJTPricingType bean) {
	    super(source, bean);
	  }

	}

	public static class CloseEvent extends PricingTypeFormEvent {
	  CloseEvent(PricingTypeForm source) {
	    super(source, null);
	  }
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { 
	  return addListener(DeleteEvent.class, listener);
	}

}


