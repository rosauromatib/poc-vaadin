package com.aat.application.form;

import java.util.List;

import com.aat.application.data.entity.TripType;
import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTProduct;
import com.aat.application.data.entity.ZJTResourceType;
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

public class ProductForm extends FormLayout {
	
	Binder<ZJTProduct> binder = new BeanValidationBinder<>(ZJTProduct.class); 
	
	TextField name = new TextField("Name");
	TextField description = new TextField("Description");
	ComboBox<ZJTResourceType> resourceType = new ComboBox<>("Resource Type");
	ComboBox<TripType> tripType = new ComboBox<>("Trip Type");
	ComboBox<ZJTElement> tripElement = new ComboBox<>("Trip Element");
	
	Button save = new Button("Save");
//	Button delete = new Button("Delete");
	Button close = new Button("Cancel");
	  
	public ProductForm(List<ZJTResourceType> resourceTypes, List<ZJTElement> tripElements) 
	{
		addClassName("demo-app-form");
		
		tripType.setItems(TripType.values());
		
		resourceType.setItems(resourceTypes);
		resourceType.setItemLabelGenerator(ZJTResourceType::getName);
		
		tripElement.setItems(tripElements);
		tripElement.setItemLabelGenerator(ZJTElement::getName);
		
		
		add(name, tripType, description, resourceType, tripElement, createButtonsLayout());
		binder.bindInstanceFields(this);
//		binder.forField(resourceType).asRequired();
		
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
		return new HorizontalLayout(save, close);

	}
	
	private void validateAndSave() {
		if (binder.isValid()) {
			fireEvent(new SaveEvent(this,  binder.getBean()));
		}
	}

	public void setProduct(ZJTProduct product)
	{

		binder.setBean(product);
	}
	
	// Events
	public static abstract class ProductFormEvent extends ComponentEvent<ProductForm> {
	  private ZJTProduct product;

	  protected ProductFormEvent(ProductForm source, ZJTProduct product) { 


	    super(source, false);
	    this.product = product;
	  }

	  public ZJTProduct getProduct() {
	    return product;
	  }
	}

	public static class SaveEvent extends ProductFormEvent {
	  SaveEvent(ProductForm source, ZJTProduct product) {
	    super(source, product);
	  }
	}

	public static class DeleteEvent extends ProductFormEvent {
	  DeleteEvent(ProductForm source, ZJTProduct product) {
	    super(source, product);
	  }

	}

	public static class CloseEvent extends ProductFormEvent {
	  CloseEvent(ProductForm source) {
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


