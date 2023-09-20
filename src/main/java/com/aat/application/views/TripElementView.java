package com.aat.application.views;

import com.aat.application.data.entity.*;
import com.aat.application.data.service.TripElementService;
import com.aat.application.form.TripElementForm;
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

@PageTitle("Trip Element")
@Route(value = "tripelement", layout = MainLayout.class)

public class TripElementView extends VerticalLayout {

    //	private Grid<ZJTElement> grid = new Grid<>(ZJTElement.class);
    TuiGrid grid;
    List<String> headers = List.of("Name", "Uom", "Elementlist", "Pricing Type");
    List<Item> items = new ArrayList<>();
    List<ZJTElement> elements;
    TextField filterText = new TextField();

    private TripElementForm form;
    private final TripElementService service;
    private boolean bAdd = false;

    public TripElementView(TripElementService service) {
        this.service = service;

//        setSizeFull();

        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private List<Item> getTableData() {

        List<Item> TableData = new ArrayList<>();
        if (filterText != null)
            elements = service.findAllElements(filterText.getValue());
        else
            elements = service.findAllElements(null);
//        Define a custom comparator to sort by the "No" column
        Comparator<ZJTElement> comparator = Comparator.comparing(item -> (item.getName()));
        // Sort the TableData list using the custom comparator
        Collections.sort(elements, comparator);


        for (ZJTElement zjtElement :
                elements) {
            TableData.add(new GuiItem(
                    List.of(zjtElement.getName(),
                            zjtElement.getUom().toString(),
                            zjtElement.getElementlist().toString(),
                            String.valueOf(zjtElement.getPricingType().getZjt_pricingtype_id())),
                    headers));

        }

        return TableData;
    }

    private List<Column> getColumns() {
        Column nameCol = new Column(new ColumnBaseOption(0, "Name", "Name", 150, "center", ""));
        nameCol.setEditable(true);
        nameCol.setMaxLength(10);
        nameCol.setType("input");
        nameCol.setSortable(true);
        nameCol.setSortingType("asc");

        Column uomCol = new Column(new ColumnBaseOption(1, "Uom", "Uom", 150, "center", ""));
        uomCol.setEditable(true);
        uomCol.setMaxLength(10);
        uomCol.setType("select");
        uomCol.setRoot(true);
        uomCol.setTarget("");
        uomCol.setSortable(true);
        uomCol.setSortingType("asc");
        List<RelationOption> uomList = new ArrayList<>();
        for (Uom uom : Uom.values()) {
            RelationOption option = new RelationOption(uom.toString(), uom.toString());
            uomList.add(option);
        }

        uomCol.setRelationOptions(uomList);

        Column elementlistCol = new Column(new ColumnBaseOption(2, "Elementlist", "Elementlist", 150, "center", ""));
        elementlistCol.setEditable(false);
        elementlistCol.setMaxLength(10);
        elementlistCol.setType("select");
        elementlistCol.setRoot(true);
        elementlistCol.setTarget("");
        elementlistCol.setSortable(true);
        elementlistCol.setSortingType("asc");
        List<RelationOption> elementsList = new ArrayList<>();
        for (ElementList elementList : ElementList.values()) {
            RelationOption option = new RelationOption(elementList.toString(), elementList.toString());
            elementsList.add(option);
        }

        elementlistCol.setRelationOptions(elementsList);

        Column pricingTypeCol = new Column(new ColumnBaseOption(3, "Pricing Type", "Pricing Type", 150, "center", ""));
        pricingTypeCol.setEditable(true);
        pricingTypeCol.setMaxLength(10);
        pricingTypeCol.setType("select");
        pricingTypeCol.setRoot(true);
        pricingTypeCol.setTarget("");
        pricingTypeCol.setSortable(true);
        pricingTypeCol.setSortingType("asc");
        List<ZJTPricingType> pricingTypes = service.getPricingTypes();
        List<RelationOption> combPricingTypes = new ArrayList<>();
        for (ZJTPricingType pricingType :
                pricingTypes) {
            RelationOption option = new RelationOption(pricingType.getName(), String.valueOf(pricingType.getZjt_pricingtype_id()));
            combPricingTypes.add(option);
        }

        pricingTypeCol.setRelationOptions(combPricingTypes);

        List<Column> columns = List.of(nameCol, uomCol, elementlistCol, pricingTypeCol);
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

//        Button addButton = new Button("Add");
//        addButton.addClickListener(click -> add());
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

        grid.addItemChangeListener(event -> {

            items = grid.getItems();
            if (!bAdd) {
                if (filterText != null)
                    elements = service.findAllElements(filterText.getValue());
                else
                    elements = service.findAll();
            }
            bAdd = false;
            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();

            int index = item.getHeaders().indexOf(colName);
            if (event.getRow() >= elements.size() - 1) {
                ZJTElement zpt = new ZJTElement();
                zpt.setName("");
                zpt.setUom(Uom.E);
                zpt.setElementlist(ElementList.DH);
                zpt.setPricingType(service.getPricingTypes().get(0));
                elements.add(zpt);
            }
            ZJTElement row = elements.get(event.getRow());

            switch (index) {
                case 0:
                    row.setName(event.getColValue());
                    break;
                case 1:
                    row.setUom(Uom.valueOf(event.getColValue().substring(0, 1)));
                    break;
                case 2:
                    for (ElementList enumValue : ElementList.values()) {
                        if (enumValue.toString().equals(event.getColValue())) {
                            row.setElementlist(enumValue);
                            break;
                        }
                    }
                    break;
                case 3:
                    ZJTPricingType pricingType = row.getPricingType();
                    pricingType.setZjt_pricingtype_id(Integer.parseInt(event.getColValue()));
                    row.setPricingType(pricingType);
                    break;
            }

            CompletableFuture.runAsync(() -> {
                service.save(row);
            });

        });
        grid.addItemDeleteListener(listener -> {
            delete();
        });
        grid.setSizeFull();

        grid.setAutoSave(true);
        grid.setHeaderHeight(50);
        grid.setTableWidth(600);
//        grid.setTableHeight(750);
    }

    private void configureForm() {
        form = new TripElementForm(service.getPricingTypes());
        form.setWidth("25em");

        form.addSaveListener(this::save);
        form.addDeleteListener(this::delete);
        form.addCloseListener(e -> closeEditor());
    }

    private void updateList() {
        grid.setItems(this.getTableData());
        add(getContent());
//        grid.setItems(service.findAllElements(filterText.getValue()));

    }

    public void edit(ZJTElement po) {
        if (po == null) {
            closeEditor();
        } else {
            form.setBean(po);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void add() {
//        grid.asSingleSelect().clear();
//        edit(new ZJTElement());
        grid.addItem(List.of(new GuiItem(List.of("", "", "", ""), headers)));
        bAdd = true;
    }


    private void closeEditor() {
        form.setBean(null);
        form.setVisible(false);
        removeClassName("editing");

    }

    private void save(TripElementForm.SaveEvent event) {
        service.save(event.getBean());
        updateList();
        closeEditor();
    }

    private void delete(TripElementForm.DeleteEvent event) {
        service.delete(event.getBean());
        updateList();
        closeEditor();
    }

    private void delete() {
        if (grid.getCheckedItems().length == 0)
            return;
        for (int checkedRow :
                grid.getCheckedItems()) {
            service.delete(elements.get(checkedRow));
        }

    }
}
