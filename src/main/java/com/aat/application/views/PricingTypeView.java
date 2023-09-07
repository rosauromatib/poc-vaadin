package com.aat.application.views;

import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.service.PricingTypeService;
import com.aat.application.form.PricingTypeForm;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.Column;
import com.vaadin.componentfactory.tuigrid.model.ColumnBaseOption;
import com.vaadin.componentfactory.tuigrid.model.Item;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@PageTitle("Pricing Type")
@Route(value = "pricingtype", layout = MainLayout.class)

public class PricingTypeView extends VerticalLayout {

    TuiGrid grid;
    List<ZJTPricingType> listPricingType;

    TextField filterText = new TextField();

    private PricingTypeForm form;

    private final PricingTypeService service;

    public PricingTypeView(PricingTypeService service) {
        this.service = service;

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
        if (filterText != null)
            listPricingType = service.findAll(filterText.getValue());
        else
            listPricingType = service.findAll(null);

        // Define a custom comparator to sort by the "No" column
        Comparator<ZJTPricingType> comparator = Comparator.comparing(item -> item.getName());

        // Sort the TableData list using the custom comparator
        Collections.sort(listPricingType, comparator);

        List<String> headers = List.of("Name", "Description");
        for (ZJTPricingType pricingType :
                listPricingType) {
            TableData.add(new GuiItem(
                    List.of(
                            pricingType.getName(),
                            pricingType.getDescription()),
                    headers));

        }
        return TableData;
    }

    private List<Column> getColumns() {
        Column nameCol = new Column(new ColumnBaseOption(0, "Name", "Name", 250, "center", ""));
        nameCol.setEditable(true);
        nameCol.setMaxLength(10);
        nameCol.setType("input");
        nameCol.setSortable(true);
        nameCol.setSortingType("asc");

        Column desCol = new Column(new ColumnBaseOption(1, "Description", "Description", 250, "center", ""));
        desCol.setEditable(true);
        desCol.setMaxLength(10);
        desCol.setType("input");
        desCol.setSortable(true);
        desCol.setSortingType("asc");

        List<Column> columns = List.of( nameCol, desCol);
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
        Button removeContactButton = new Button("Delete");
        removeContactButton.addClickListener(click -> add());
        var toolbar = new HorizontalLayout(filterText, addContactButton, removeContactButton);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
//        grid.addClassName("scheduler-grid");
        grid = new TuiGrid();

        List<Item> items = this.getTableData();
        grid.setItems(items);
        grid.setColumns(this.getColumns());
        grid.setRowHeaders(List.of("rowNum", "checkbox"));

        grid.addItemChangeListener(event -> {
            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();
            int index = item.getHeaders().indexOf(colName);
            ZJTPricingType row = listPricingType.get(event.getRow());
            switch (index) {
                case 0:
                    row.setZjt_pricingtype_id(Integer.parseInt(event.getColValue()));
                    break;
                case 1:
                    row.setName(event.getColValue());
                    break;
                case 2:
                    row.setDescription(event.getColValue());
                    break;
            }
            service.save(row);
        });

        grid.setSizeFull();
//		grid.setColumns("name", "description");
//		grid.addColumn(product -> product.getName()).setHeader("Product Name");
//		grid.getColumnByKey("zjt_pricingtype_id").setVisible(false);
//		grid.getColumns().forEach(col -> col.setAutoWidth(true));
//		grid.asSingleSelect().addValueChangeListener(event -> edit(event.getValue()));
        grid.setHeaderHeight(50);
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
        grid.setItems(this.getTableData());
        add(getContent());
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
