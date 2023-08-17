package com.aat.application.form;

import java.util.List;

import com.aat.application.data.entity.ElementList;
import com.aat.application.data.entity.Uom;
import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTPricingType;
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

public class TripElementForm extends FormLayout {
	
	

	private static final long serialVersionUID = -5183438338263448739L;

	Binder<ZJTElement> binder = new BeanValidationBinder<>(ZJTElement.class); 
	
	TextField name = new TextField("Name");
	ComboBox<ZJTPricingType> pricingType = new ComboBox<>("Pricing Type");
	ComboBox<Uom> uom = new ComboBox<>("UOM");
	ComboBox<ElementList> elementlist = new ComboBox<>("Element List");

	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");
	  
	public TripElementForm(List<ZJTPricingType> pricingTypes) 
	{
		addClassName("demo-app-form");
		binder.bindInstanceFields(this);
		
		pricingType.setItems(pricingTypes);
		pricingType.setItemLabelGenerator(ZJTPricingType::getName);

		uom.setItems(Uom.values());
		elementlist.setItems(ElementList.values());
		add(name, pricingType, uom, elementlist, createButtonsLayout());
	}
	  
	private HorizontalLayout createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		save.addClickShortcut(Key.ENTER);

		close.addClickShortcut(Key.ESCAPE);

		save.addClickListener(event -> validateAndSave());
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(save, delete, close);

	}
	
	private void validateAndSave() {
		if (binder.isValid()) {
			fireEvent(new SaveEvent(this,  binder.getBean()));
		}
	}

	public void setBean(ZJTElement bean)
	{
		binder.setBean(bean);
	}
	
	// Events
	public static abstract class TripElementFormEvent extends ComponentEvent<TripElementForm> {
	  private ZJTElement bean;

	  protected TripElementFormEvent(TripElementForm source, ZJTElement bean) { 


	    super(source, false);
	    this.bean = bean;
	  }

	  public ZJTElement getBean() {
	    return bean;
	  }
	}

	public static class SaveEvent extends TripElementFormEvent {
	  SaveEvent(TripElementForm source, ZJTElement bean) {
	    super(source, bean);
	  }
	}

	public static class DeleteEvent extends TripElementFormEvent {
	  DeleteEvent(TripElementForm source, ZJTElement bean) {
	    super(source, bean);
	  }

	}

	public static class CloseEvent extends TripElementFormEvent {
	  CloseEvent(TripElementForm source) {
	    super(source, null);
	  }
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { 
	  return addListener(DeleteEvent.class, listener);
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
	  return addListener(SaveEvent.class, listener);
	}
	
	public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
	  return addListener(CloseEvent.class, listener);
	}
}


