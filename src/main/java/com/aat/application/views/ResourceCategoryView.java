package com.aat.application.views;

import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.entity.ZJTResourceCategory;
import com.aat.application.data.entity.ZJTResourceType;
import com.aat.application.data.service.ResourceCategoryService;
import com.aat.application.form.ResourceCategoryForm;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.Column;
import com.vaadin.componentfactory.tuigrid.model.ColumnBaseOption;
import com.vaadin.componentfactory.tuigrid.model.Item;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

@PageTitle("Resource Category")
@Route(value = "resourcecategory", layout = MainLayout.class)
public class ResourceCategoryView extends VerticalLayout {

    //	private Grid<ZJTResourceCategory> grid = new Grid<>(ZJTResourceCategory.class);
    TuiGrid grid;
    List<String> headers = List.of("Name", "Description");
    List<ZJTResourceCategory> listResourceCategory;
    List<Item> items = new ArrayList<>();
    TextField filterText = new TextField();
    private boolean bAdd = false;
    private ResourceCategoryForm form;

    private final ResourceCategoryService service;

    public ResourceCategoryView(ResourceCategoryService service) {
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
            listResourceCategory = service.findAll(filterText.getValue());
        else
            listResourceCategory = service.findAll(null);

        Comparator<ZJTResourceCategory> comparator = Comparator.comparing(item -> item.getName());
        Collections.sort(listResourceCategory, comparator);

        for (ZJTResourceCategory zjtResourceCategory :
                listResourceCategory) {
            TableData.add(new GuiItem(
                    List.of(zjtResourceCategory.getName(),
                            zjtResourceCategory.getDescription()),
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

        Column desCol = new Column(new ColumnBaseOption(0, "Description", "Description", 250, "center", ""));
        desCol.setEditable(true);
        desCol.setMaxLength(10);
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

        grid.addItemChangeListener(event -> {
            items = grid.getItems();
            if (!bAdd) {
                if (filterText != null)
                    listResourceCategory = service.findAll(filterText.getValue());
                else
                    listResourceCategory = service.findAll(null);
            }
            bAdd = false;
            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();

            int index = item.getHeaders().indexOf(colName);
            if (event.getRow() >= listResourceCategory.size() - 1) {
                ZJTResourceCategory zpt = new ZJTResourceCategory();
                zpt.setName("");
                zpt.setDescription("");
                listResourceCategory.add(zpt);
            }
            ZJTResourceCategory row = listResourceCategory.get(event.getRow());

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
        form = new ResourceCategoryForm();
        form.setWidth("25em");

        form.addSaveListener(this::save);
        form.addDeleteListener(this::delete);
        form.addCloseListener(e -> closeEditor());
    }

    private void updateList() {
//        grid.setItems(service.findAll(filterText.getValue()));
        grid.setItems(this.getTableData());
        add(getContent());
    }

    public void edit(ZJTResourceCategory po) {
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
//        edit(new ZJTResourceCategory());
        grid.addItem(List.of(new GuiItem(List.of("", ""), headers)));
        bAdd = true;
    }

    private void closeEditor() {
        form.setBean(null);
        form.setVisible(false);
        removeClassName("editing");

    }

    private void save(ResourceCategoryForm.SaveEvent event) {
        service.save(event.getBean());
        updateList();
        closeEditor();
    }

    private void delete(ResourceCategoryForm.DeleteEvent event) {
        service.delete(event.getBean());
        updateList();
        closeEditor();
    }

    private void delete() {
        if (grid.getCheckedItems().length == 0)
            return;
        for (int checkedRow :
                grid.getCheckedItems()) {
            service.delete(listResourceCategory.get(checkedRow));
        }
        
    }
}
