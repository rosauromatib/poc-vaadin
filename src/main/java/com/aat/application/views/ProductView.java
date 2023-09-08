package com.aat.application.views;

import com.aat.application.data.entity.PriceListRow;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.entity.ZJTProduct;
import com.aat.application.data.entity.ZJTResourceType;
import com.aat.application.data.service.ProductService;
import com.aat.application.form.ProductForm;
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

import javax.management.relation.Relation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@PageTitle("Product")
@Route(value = "product", layout = MainLayout.class)

public class ProductView extends VerticalLayout {

    //	private Grid<ZJTProduct> grid = new Grid<>(ZJTProduct.class);
    TuiGrid grid;
    List<ZJTProduct> zjtProductList;
    TextField filterText = new TextField();

    private ProductForm form;

    private final ProductService service;
    public ProductView(ProductService service) {
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
        zjtProductList = service.findAllProducts(filterText.getValue());

        Comparator<ZJTProduct> comparator = Comparator.comparing(item -> item.getName());
        Collections.sort(zjtProductList, comparator);

        List<String> headers = List.of("Name", "Description", "Resource Type");
        for (ZJTProduct zjtProduct :
                zjtProductList) {
            TableData.add(new GuiItem(
                    List.of(zjtProduct.getName(),
                            zjtProduct.getDescription() != null ? zjtProduct.getDescription() : "",
                            zjtProduct.getResourceType() != null ? String.valueOf(zjtProduct.getResourceType().getZjt_resourcetype_id()) : ""),
                    headers));

        }

        return TableData;
    }

    private List<Column> getColumns() {

        Column nameCol = new Column(new ColumnBaseOption(0, "Name", "Name", 250, "center", ""));
        nameCol.setEditable(false);
        nameCol.setSortable(true);
        nameCol.setSortingType("asc");

        Column desCol = new Column(new ColumnBaseOption(0, "Description", "Description", 250, "center", ""));
        desCol.setEditable(true);
        desCol.setMaxLength(10);
        desCol.setType("input");
        desCol.setSortable(true);
        desCol.setSortingType("asc");

        Column resourceTypeCol = new Column(new ColumnBaseOption(2, "Resource Type", "Resource Type", 250, "center", ""));
        resourceTypeCol.setEditable(true);
        resourceTypeCol.setMaxLength(10);
        resourceTypeCol.setType("select");
        resourceTypeCol.setRoot(true);
        resourceTypeCol.setTarget("");
        resourceTypeCol.setSortable(true);
        resourceTypeCol.setSortingType("asc");

        List<ZJTResourceType> resourceTypes = service.getResourceTypes();
        List<RelationOption> combResourceTypes = new ArrayList<>();
        for (ZJTResourceType resourceType :
                resourceTypes) {
            RelationOption option = new RelationOption(resourceType.getName(), String.valueOf(resourceType.getZjt_resourcetype_id()));
            combResourceTypes.add(option);
        }
        resourceTypeCol.setRelationOptions(combResourceTypes);

        List<Column> columns = List.of(nameCol, desCol, resourceTypeCol);
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

        Button addContactButton = new Button("Add product");
        addContactButton.addClickListener(click -> add());
        Button removeContactButton = new Button("Delete");
        removeContactButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeContactButton.addClickListener(click -> delete());
        var toolbar = new HorizontalLayout(filterText, addContactButton, removeContactButton);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
        grid = new TuiGrid();

        List<Item> items = this.getTableData();
        grid.setItems(items);
        grid.setColumns(this.getColumns());
        grid.setRowHeaders(List.of("rowNum", "checkbox"));

        grid.addItemChangeListener(event -> {
            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();
            int index = item.getHeaders().indexOf(colName);
            ZJTProduct product = zjtProductList.get(event.getRow());
            if(index == 1)
                product.setDescription(event.getColValue());
            else{
                ZJTResourceType resourceType = product.getResourceType();
                if(resourceType == null)
                    resourceType = new ZJTResourceType();
                resourceType.setZjt_resourcetype_id(Integer.parseInt(event.getColValue()));
                product.setResourceType(resourceType);
            }

            service.save(product);
        });
        grid.addClassName("scheduler-grid");
        grid.setSizeFull();
//        grid.setHeaderHeight(100);
        grid.setTableWidth(750);
        grid.setTableHeight(750);
    }

    private void configureForm() {
        form = new ProductForm(service.getResourceTypes(), service.getTripElements());
        form.setWidth("25em");

        form.addSaveListener(this::save);
        form.addDeleteListener(this::delete);
        form.addCloseListener(e -> closeEditor());

    }

    private void updateList() {
//		grid.setItems(service.findAllProducts(filterText.getValue()));
        grid.setItems(this.getTableData());
        add(getContent());
    }

    public void edit(ZJTProduct product) {
        if (product == null) {
            closeEditor();
        } else {
            form.setProduct(product);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void add() {
//		grid.asSingleSelect().clear();
        edit(new ZJTProduct());
    }


    private void closeEditor() {
        form.setProduct(null);
        form.setVisible(false);
        removeClassName("editing");

    }

    private void save(ProductForm.SaveEvent event) {
        service.save(event.getProduct());
        updateList();
        closeEditor();
    }

    private void delete(ProductForm.DeleteEvent event) {
        service.delete(event.getProduct());
        updateList();
        closeEditor();
    }

    private void delete() {
        if (grid.getCheckedItems().size() == 0)
            return;
        for (int checkedRow :
                grid.getCheckedItems()) {
            service.delete(zjtProductList.get(checkedRow));
        }
        grid.deleteItems(grid.getCheckedItems());
    }
}
