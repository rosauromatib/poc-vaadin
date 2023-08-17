package com.aat.application.views;

import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.service.TripElementService;
import com.aat.application.form.TripElementForm;
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

@PageTitle("Trip Element")
@Route(value = "tripelement", layout = MainLayout.class)

public class TripElementView extends VerticalLayout {

    //	private Grid<ZJTElement> grid = new Grid<>(ZJTElement.class);
    TuiGrid grid;
    TextField filterText = new TextField();

    private TripElementForm form;

    private TripElementService service;

    public TripElementView(TripElementService service) {
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
        List<ZJTElement> listPricingType;
        if (filterText != null)
            listPricingType = service.findAllElements(filterText.getValue());
        else
            listPricingType = service.findAllElements(null);
        List<String> headers = List.of("Name", "Uom", "Elementlist", "Pricing Type");
        for (ZJTElement zjtElement :
                listPricingType) {
            TableData.add(new RelationItem(
                    List.of(zjtElement.getName(),
                            zjtElement.getUom().toString(),
                            zjtElement.getElementlist().toString(),
                            zjtElement.getPricingType().getName()),
                    headers));

        }

        return TableData;
    }

    private List<Column> getColumns() {
        List<Column> columns = List.of(
                new Column(new ColumnBaseOption(0, "Name", "Name", 250, "center", "")),
                new Column(new ColumnBaseOption(1, "Uom", "Uom", 250, "center", "")),
                new Column(new ColumnBaseOption(2, "Elementlist", "Elementlist", 250, "center", "")),
                new Column(new ColumnBaseOption(2, "Pricing Type", "Pricing Type", 250, "center", "")));
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

        Button addButton = new Button("Add");
        addButton.addClickListener(click -> add());
        var toolbar = new HorizontalLayout(filterText, addButton);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("scheduler-grid");

        grid.setSizeFull();
//		grid.setColumns("name", "zjt_pricingtype_id");
//        grid.setColumns("name", "uom", "elementlist");
//        grid.addColumn(pricingtype -> pricingtype.getPricingType().getName()).setHeader("Pricing Type");
//		grid.addColumn(product -> product.getName()).setHeader("Product Name");
//		grid.getColumnByKey("zjt_pricingtype_id").setHeader("Pricing Type");
//        grid.getColumns().forEach(col -> col.setAutoWidth(true));
//        grid.asSingleSelect().addValueChangeListener(event -> edit(event.getValue()));
        grid.setHeaderHeight(100);
        grid.setTableWidth(1000);
        grid.setTableHeight(750);
    }

    private void configureForm() {
        form = new TripElementForm(service.getPricingTypes());
        form.setWidth("25em");

        form.addSaveListener(this::save);
        form.addDeleteListener(this::delete);
        form.addCloseListener(e -> closeEditor());
    }

    private void updateList() {
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
        edit(new ZJTElement());
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
}
