package com.aat.application.views;

import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.service.VehicleService;
import com.aat.application.form.VehicleForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Vehicle")
@Route(value="vehicle", layout = MainLayout.class )

public class VehicleView  extends VerticalLayout{

	private Grid<ZJTVehicle> grid = new Grid<>(ZJTVehicle.class);
	TextField filterText = new TextField();
	
	private VehicleForm form;
	
	private VehicleService service;
	
	public VehicleView(VehicleService service) {
		this.service = service;
		
		
		setSizeFull();
		
		configureGrid();
		configureForm();
		getContent();
		add(getToolbar(), getContent());
		updateList();
		closeEditor();
	}

	
	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(grid, form);
		content.setFlexGrow(2,  grid);
		content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
	}

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addButton = new Button("Add");
        addButton.addClickListener(click -> add());
        var toolbar = new HorizontalLayout(filterText, addButton); 

        toolbar.addClassName("toolbar"); 

        return toolbar;
    }

	private void configureGrid() {
		grid.addClassName("scheduler-grid");
		
		grid.setSizeFull();
		grid.setColumns("vehicleid", "fleetid", "makemodel", "description");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(event -> edit(event.getValue()));
	}
	
	private void configureForm()
	{
		form = new VehicleForm();
		form.setWidth("25em");

		form.addSaveListener(this::save);
		form.addDeleteListener(this::delete);
		form.addCloseListener(e -> closeEditor());
	}

	private void updateList() {
		grid.setItems(service.findAllVehicles(filterText.getValue()));
		
	}
	
	public void edit(ZJTVehicle po)
	{
		if (po == null) {
			closeEditor();
		} else {
			form.setBean(po);
			form.setVisible(true);
			addClassName("editing");
		}
	}
	
	private void add()
	{
		grid.asSingleSelect().clear();
		edit(new ZJTVehicle());
	}



	private void closeEditor() {
		form.setBean(null);
		form.setVisible(false);
		removeClassName("editing");
		
	}

	private void save(VehicleForm.SaveEvent event)
	{
		service.save(event.getBean());
		updateList();
		closeEditor();
	}
	
	private void delete(VehicleForm.DeleteEvent event)
	{
		service.delete(event.getBean());
		updateList();
		closeEditor();
	}
}
