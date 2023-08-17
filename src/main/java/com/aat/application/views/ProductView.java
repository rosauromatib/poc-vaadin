package com.aat.application.views;

import com.aat.application.data.entity.ZJTProduct;
import com.aat.application.data.service.ProductService;
import com.aat.application.form.ProductForm;
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


@PageTitle("Product")
@Route(value = "product", layout = MainLayout.class)

public class ProductView extends VerticalLayout {

    //	private Grid<ZJTProduct> grid = new Grid<>(ZJTProduct.class);
    TuiGrid grid;
    TextField filterText = new TextField();


    private ProductForm form;

    private ProductService service;

    public ProductView(ProductService service) {
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
        List<ZJTProduct> listPricingType;
        listPricingType = service.findAllProducts(filterText.getValue());
        List<String> headers = List.of("Name", "Description", "Resource Type");
        for (ZJTProduct zjtProduct :
                listPricingType) {
            TableData.add(new RelationItem(
                    List.of(zjtProduct.getName(),
                            zjtProduct.getDescription(),
                            zjtProduct.getResourceType() != null ? zjtProduct.getResourceType().getName() : ""),
                    headers));

        }

        return TableData;
    }

    private List<Column> getColumns() {
        List<Column> columns = List.of(
                new Column(new ColumnBaseOption(0, "Name", "Name", 250, "center", "")),
                new Column(new ColumnBaseOption(1, "Description", "Description", 250, "center", "")),
                new Column(new ColumnBaseOption(2, "Resource Type", "Resource Type", 250, "center", "")));
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
        var toolbar = new HorizontalLayout(filterText, addContactButton);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("scheduler-grid");

        grid.setSizeFull();
//		grid.setColumns("name", "description");
//		grid.addColumn(product ->
//			product.getResourceType() != null ? product.getResourceType().getName() : null)
//			.setHeader("Resource Type");
//		grid.getColumns().forEach(col -> col.setAutoWidth(true));
//		grid.asSingleSelect().addValueChangeListener(event -> edit(event.getValue()));
        grid.setHeaderHeight(100);
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
}
