package com.aat.application.views;

import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.service.PricingTypeService;
import com.aat.application.form.PricingTypeForm;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import java.util.concurrent.CompletableFuture;

@PageTitle("Pricing Type")
@Route(value = "pricingtype", layout = MainLayout.class)

public class PricingTypeView extends VerticalLayout {

    TuiGrid grid;
    List<String> headers = List.of("Name", "Description");
    List<ZJTPricingType> listPricingType;
    List<Item> items = new ArrayList<>();
    TextField filterText = new TextField();
    private PricingTypeForm form;

    private final PricingTypeService service;
    private boolean bAdd = false;
    Span sp = new Span("Here is: ");

    public PricingTypeView(PricingTypeService service) {
        this.service = service;

//        setSizeFull();

        configureGrid();
        configureForm();
        getContent();

        add(sp, getToolbar(), getContent());
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
        nameCol.setType("input");
        nameCol.setSortable(true);
        nameCol.setSortingType("asc");

        Column desCol = new Column(new ColumnBaseOption(1, "Description", "Description", 250, "center", ""));
        desCol.setEditable(true);
        desCol.setType("input");
        desCol.setSortable(true);
        desCol.setSortingType("asc");

        List<Column> columns = List.of(nameCol, desCol);
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

//        Button addContactButton = new Button("Add");
//        addContactButton.addClickListener(click -> add());
//        Button removeContactButton = new Button("Delete");
//        removeContactButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
//        removeContactButton.addClickListener(click -> delete());
        var toolbar = new HorizontalLayout(filterText);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
        grid = new TuiGrid();
        grid.addClassName("scheduler-grid");
        grid.setHeaders(headers);
        items = this.getTableData();
        grid.setItems(items);
        grid.setColumns(this.getColumns());
        grid.setRowHeaders(List.of("checkbox"));

        Theme inputTheme = new Theme();
        inputTheme.setMaxLength(10);
        inputTheme.setBorder("1px solid #326f70");
        inputTheme.setBackgroundColor("#66878858");
        inputTheme.setOutline("none");
        inputTheme.setWidth("90%");
        inputTheme.setHeight("100%");
        inputTheme.setOpacity(1);

        grid.setInputTheme(inputTheme);

        grid.addItemChangeListener(event -> {


            items = grid.getItems();
            if (!bAdd) {
                if (filterText != null)
                    listPricingType = service.findAll(filterText.getValue());
                else
                    listPricingType = service.findAll(null);
            }
            bAdd = false;

            Comparator<ZJTPricingType> comparator = Comparator.comparing(item -> item.getName());
            Collections.sort(listPricingType, comparator);

            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();
            int index = item.getHeaders().indexOf(colName);
            if (event.getRow() >= listPricingType.size() - 1) {
                ZJTPricingType zpt = new ZJTPricingType();
                zpt.setName("");
                zpt.setDescription("");
                listPricingType.add(zpt);
            }
            ZJTPricingType row = listPricingType.get(event.getRow());

            sp.add(" row1: " + row.getName());
            sp.add(" row2: " + event.getColValue());
            switch (index) {
                case 0:
                    row.setName(event.getColValue());
                    break;
                case 1:
                    row.setDescription(event.getColValue());
                    break;
            }

            // Asynchronously save the modified row
            CompletableFuture.runAsync(() -> {
                service.save(row);
            });
        });
        grid.addItemDeleteListener(listener -> {
            delete();
        });

        grid.setAutoSave(true);
        grid.setSizeFull();
        grid.setHeaderHeight(50);
        grid.setTableWidth(500);
//        grid.setTableHeight(750);
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
//        edit(new ZJTPricingType());
        grid.addItem(List.of(new GuiItem(List.of("", ""), headers)));
        bAdd = true;
//        add(grid);
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

    private void delete() {
        if (grid.getCheckedItems().length == 0)
            return;
        for (int checkedRow :
                grid.getCheckedItems()) {
            service.delete(listPricingType.get(checkedRow));
        }

    }
}
