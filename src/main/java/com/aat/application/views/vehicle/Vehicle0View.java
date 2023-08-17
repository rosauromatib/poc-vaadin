package com.aat.application.views.vehicle;

import com.aat.application.data.entity.Vehicle;
import com.aat.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Vehicle0")
@Route(value = "vehicle0", layout = MainLayout.class)
public class Vehicle0View extends VerticalLayout {
	
	private TextField textVIN;
	private TextField textPlateNo;
	private NumberField numSeatingCapacity;
	private Button btnSave;
	
	private Grid<Vehicle> grid = new Grid<>(Vehicle.class);
	
	
    private TextField name;
    private Button sayHello;

	public Vehicle0View() {
		textVIN = new TextField("VIN");
		textPlateNo = new TextField("Vehicle Plate");
		numSeatingCapacity = new NumberField("Max Seating Capacity");
		btnSave = new Button("Save");
		
		btnSave.addClickListener(e -> {
			Notification.show(textVIN.getValue() + " Saved");
		});
		
		btnSave.addClickShortcut(Key.ENTER);
		
		add(textVIN, textPlateNo, numSeatingCapacity, btnSave);
		
		configureGrid();
		add(grid);
		setSizeFull();
	}
	

	private void configureGrid() {
		grid.addClassName("scheduler-grid");
		
		grid.setSizeFull();
		grid.setColumns("VIN", "plateNo", "maxSeatingCapacity");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		
	}
}
