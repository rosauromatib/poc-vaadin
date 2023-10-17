package com.aat.application.form;

import com.aat.application.data.entity.ZJTVehicle;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class VehicleForm extends FormLayout {
	
	

	private static final long serialVersionUID = -5183438338263448739L;

	Binder<ZJTVehicle> binder = new BeanValidationBinder<>(ZJTVehicle.class); 
	
	TextField vehicleid = new TextField("Vehicle ID");
	TextField fleetid = new TextField("Fleet ID");
	TextField license = new TextField("License");
	TextField description = new TextField("Description");
	TextField makemodel = new TextField("Make / Model");
	TextField enginemodel = new TextField("Engine Model");
	TextField engineno = new TextField("Engine No");
	TextField chassisno = new TextField("Chassis No");

	TextField hubmeter = new TextField("Hubmeter");
	TextField kmrate = new TextField("Km Rate");
	TextField kmratedays = new TextField("Km Rate Days");
	TextField hubmetertele = new TextField("Hubmeter Tele");
	TextField kmratetele = new TextField("Km Rate Tele");
	TextField kmratedaystele = new TextField("Km Rate Days Tele");

	Checkbox operational = new Checkbox("Operational");
	Checkbox hirein = new Checkbox("Hire In");
	DatePicker disposaldate = new DatePicker("Disposal Date");
	DatePicker purchasedate = new DatePicker("Purchase Date");
			
	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");
	  
	public VehicleForm() 
	{
		addClassName("demo-app-form");
		binder.bindInstanceFields(this);
		
		add(createButtonsLayout(), configureTabs());
	}
	  
	
	private TabSheet configureTabs()
	{
		TabSheet tabs = new TabSheet();
		
		FormLayout fl;
		
		//main
		fl = new FormLayout(vehicleid, fleetid, license, description, makemodel, enginemodel, engineno, chassisno);
		fl.setHeightFull();
		tabs.add("Info", fl);
		
		//meter
		fl = new FormLayout(hubmeter, kmrate, kmratedays, hubmetertele, kmratetele, kmratedaystele);
		fl.setHeightFull();
		tabs.add("Meter", fl);

		//other
		fl = new FormLayout(operational, hirein, disposaldate, purchasedate);
		fl.setHeightFull();
		tabs.add("Other", fl);

		tabs.setHeightFull();
		return tabs;
		
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

	public void setBean(ZJTVehicle bean)
	{
		binder.setBean(bean);
	}
	
	// Events
	public static abstract class VehicleFormEvent extends ComponentEvent<VehicleForm> {
	  private final ZJTVehicle bean;

	  protected VehicleFormEvent(VehicleForm source, ZJTVehicle bean) { 


	    super(source, false);
	    this.bean = bean;
	  }

	  public ZJTVehicle getBean() {
	    return bean;
	  }
	}

	public static class SaveEvent extends VehicleFormEvent {
	  SaveEvent(VehicleForm source, ZJTVehicle bean) {
	    super(source, bean);
	  }
	}

	public static class DeleteEvent extends VehicleFormEvent {
	  DeleteEvent(VehicleForm source, ZJTVehicle bean) {
	    super(source, bean);
	  }

	}

	public static class CloseEvent extends VehicleFormEvent {
	  CloseEvent(VehicleForm source) {
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


