package com.aat.application.views;

import com.aat.application.data.entity.ZJTResourceType;
import com.aat.application.data.service.ResourceTypeService;
import com.aat.application.form.ResourceTypeForm;
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

@PageTitle("Resource Type")
@Route(value = "resourcetype", layout = MainLayout.class)

public class ResourceTypeView extends VerticalLayout {

    //	private Grid<ZJTResourceType> grid = new Grid<>(ZJTResourceType.class);
    TuiGrid grid;
    TextField filterText = new TextField();

    private ResourceTypeForm form;

    private ResourceTypeService service;

    public ResourceTypeView(ResourceTypeService service) {
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
        List<ZJTResourceType> listPricingType;
        if (filterText != null)
            listPricingType = service.findAllResourceTypes(filterText.getValue());
        else
            listPricingType = service.findAllResourceTypes(null);
        List<String> headers = List.of("Name", "Description");
        try {
            for (ZJTResourceType zjtResourceType :
                    listPricingType) {

                TableData.add(new RelationItem(
                        List.of(zjtResourceType.getName(),
                                zjtResourceType.getDescription() != null ? zjtResourceType.getDescription() : ""),
                        headers));

            }
        } catch (Exception e) {
            TableData.add(new RelationItem(
                    List.of(e.toString(),
                            String.valueOf(listPricingType.size())),
                    headers));
        }


        return TableData;
    }

    private List<Column> getColumns() {
        List<Column> columns = List.of(
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

        Button addButton = new Button("Add");
        addButton.addClickListener(click -> add());
        var toolbar = new HorizontalLayout(filterText, addButton);

        toolbar.addClassName("toolbar");

        return toolbar;
    }


    private void configureGrid() {
        grid.addClassName("scheduler-grid");

        grid.setSizeFull();
//		grid.setColumns("name", "description");
//		//TODO - this is causing some error
//		grid.addColumn(resourceCategory -> resourceCategory.getResourceCategory().getName()).setHeader("Resource Category");
////		grid.addColumn(resourceCategory -> resourceCategory.getCname()).setHeader("Res Category");
//		grid.getColumns().forEach(col -> col.setAutoWidth(true));
//		grid.asSingleSelect().addValueChangeListener(event -> edit(event.getValue()));
        grid.setHeaderHeight(100);
        grid.setTableWidth(500);
        grid.setTableHeight(750);
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
        edit(new ZJTResourceType());
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
}
