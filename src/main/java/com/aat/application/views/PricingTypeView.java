package com.aat.application.views;

import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.service.PricingTypeService;
import com.aat.application.form.PricingTypeForm;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.Column;
import com.vaadin.componentfactory.tuigrid.model.ColumnBaseOption;
import com.vaadin.componentfactory.tuigrid.model.Item;
import com.vaadin.componentfactory.tuigrid.model.RelationItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Pricing Type")
@Route(value = "pricingtype", layout = MainLayout.class)

public class PricingTypeView extends VerticalLayout {

    TuiGrid grid;

    TextField filterText = new TextField();

    private PricingTypeForm form;

    private PricingTypeService service;

    public PricingTypeView(PricingTypeService service) {
        this.service = service;
        grid = new TuiGrid(null, this.getTableData(),
                this.getColumns(), null);

        setSizeFull();

        configureGrid();
        configureForm();
        getContent();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private List<Item> getTableData() {

        List<Item> TableData = new ArrayList<>();
        List<ZJTPricingType> listPricingType;
        if (filterText != null)
            listPricingType = service.findAll(filterText.getValue());
        else
            listPricingType = service.findAll(null);
        List<String> headers = List.of("No", "Name", "Description");
        for (ZJTPricingType pricingType :
                listPricingType) {
            TableData.add(new RelationItem(
                    List.of(String.valueOf(pricingType.getZjt_pricingtype_id()),
                            pricingType.getName(),
                            pricingType.getDescription()),
                    headers));

        }

        return TableData;
    }

    private List<Column> getColumns() {
        List<Column> columns = List.of(
                new Column(new ColumnBaseOption(0, "No", "No", 250, "center", "")),
                new Column(new ColumnBaseOption(1, "Name", "Name", 250, "center", "")),
                new Column(new ColumnBaseOption(2, "Description", "Description", 250, "center", "")));
        return columns;
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
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

        Button addContactButton = new Button("Add");
        addContactButton.addClickListener(click -> add());
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
//        grid.addClassName("scheduler-grid");

        grid.setSizeFull();
//		grid.setColumns("name", "description");
//		grid.addColumn(product -> product.getName()).setHeader("Product Name");
//		grid.getColumnByKey("zjt_pricingtype_id").setVisible(false);
//		grid.getColumns().forEach(col -> col.setAutoWidth(true));
//		grid.asSingleSelect().addValueChangeListener(event -> edit(event.getValue()));
        grid.setHeaderHeight(100);
        grid.setTableWidth(750);
        grid.setTableHeight(750);
    }

    private void configureForm() {
        form = new PricingTypeForm();
        form.setWidth("25em");

        form.addSaveListener(this::save);
        form.addDeleteListener(this::delete);
        form.addCloseListener(e -> closeEditor());
    }

    private void updateList() {
//		grid.setItems(service.findAll(filterText.getValue()));

    }

    public void edit(ZJTPricingType po) {
        if (po == null) {
            closeEditor();
        } else {
            form.setBean(po);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void add() {
//		grid.asSingleSelect().clear();
        edit(new ZJTPricingType());
    }


    private void closeEditor() {
        form.setBean(null);
        form.setVisible(false);
        removeClassName("editing");

    }

    private void save(PricingTypeForm.SaveEvent event) {
        service.save(event.getBean());
        updateList();
        closeEditor();
    }

    private void delete(PricingTypeForm.DeleteEvent event) {
        service.delete(event.getBean());
        updateList();
        closeEditor();
    }
}
