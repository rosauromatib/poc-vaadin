package com.aat.application.views;

import com.aat.application.data.entity.PriceListRow;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.entity.ZJTResourceCategory;
import com.aat.application.data.entity.ZJTResourceType;
import com.aat.application.data.service.ResourceTypeService;
import com.aat.application.form.ResourceTypeForm;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@PageTitle("Resource Type")
@Route(value = "resourcetype", layout = MainLayout.class)

public class ResourceTypeView extends VerticalLayout {

    //	private Grid<ZJTResourceType> grid = new Grid<>(ZJTResourceType.class);
    TuiGrid grid;
    List<String> headers = List.of("Name", "Description");
    List<ZJTResourceType> listResourceType;
    List<Item> items = new ArrayList<>();
    TextField filterText = new TextField();

    private ResourceTypeForm form;

    private final ResourceTypeService service;
    private boolean bAdd = false;

    public ResourceTypeView(ResourceTypeService service) {
        this.service = service;

//        setSizeFull();

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
            listResourceType = service.findAllResourceTypes(filterText.getValue());
        else
            listResourceType = service.findAllResourceTypes(null);

        Comparator<ZJTResourceType> comparator = Comparator.comparing(item -> item.getName());
        Collections.sort(listResourceType, comparator);

        try {
            for (int i = 0; i < 200; i++) {
                for (ZJTResourceType zjtResourceType :
                        listResourceType) {

                    TableData.add(new GuiItem(
                            List.of(zjtResourceType.getName(),
                                    zjtResourceType.getDescription() != null ? zjtResourceType.getDescription() : ""),
                            headers));

                }
            }
        } catch (Exception e) {
            TableData.add(new GuiItem(
                    List.of(e.toString(),
                            String.valueOf(listResourceType.size())),
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

        Column desCol = new Column(new ColumnBaseOption(0, "Description", "Description", 250, "center", ""));
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
        grid.sethScroll(true);
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
                    listResourceType = service.findAllResourceTypes(filterText.getValue());
                else
                    listResourceType = service.findAllResourceTypes(null);
            }
            bAdd = false;
            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();
            int index = item.getHeaders().indexOf(colName);
            if (event.getRow() >= listResourceType.size() - 1) {
                ZJTResourceType zpt = new ZJTResourceType();
                zpt.setName("");
                zpt.setDescription("");
                zpt.setResourceCategory(service.getResourceCategories().get(0));
                listResourceType.add(zpt);
            }
            ZJTResourceType row = listResourceType.get(event.getRow());
            switch (index) {
                case 0:
                    row.setName(event.getColValue());
                    break;
                case 1:
                    row.setDescription(event.getColValue());
                    break;
            }
            // Asynchronously save the modified row
//            CompletableFuture.runAsync(() -> {
            service.save(row);
//            });
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
        form = new ResourceTypeForm(service.getResourceCategories());
        form.setWidth("25em");

        form.addSaveListener(this::save);
        form.addDeleteListener(this::delete);
        form.addCloseListener(e -> closeEditor());
    }

    private void updateList() {
//		grid.setItems(service.findAllResourceTypes(filterText.getValue()));
        grid.setItems(this.getTableData());
        add(getContent());
    }

    public void edit(ZJTResourceType po) {
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
//        edit(new ZJTResourceType());
        grid.addItem(List.of(new GuiItem(List.of("", ""), headers)));
        bAdd = true;
    }


    private void closeEditor() {
        form.setBean(null);
        form.setVisible(false);
        removeClassName("editing");

    }

    private void save(ResourceTypeForm.SaveEvent event) {
        service.save(event.getBean());
        updateList();
        closeEditor();
    }

    private void delete(ResourceTypeForm.DeleteEvent event) {
        service.delete(event.getBean());
        updateList();
        closeEditor();
    }

    private void delete() {
        if (grid.getCheckedItems().length == 0)
            return;
        for (int checkedRow :
                grid.getCheckedItems()) {
            service.delete(listResourceType.get(checkedRow));
        }

    }
}
