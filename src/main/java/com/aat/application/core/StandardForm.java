package com.aat.application.core;

import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class StandardForm<T extends ZJTEntity, S extends ZJTService<T>> extends FormLayout {

    private static final long serialVersionUID = -5183438338263448739L;

    protected Binder<T> binder;
    protected TextField name;
    protected TextField description;
    protected TextField filterText = new TextField();
    protected Button save;
    protected Button close;
    protected TuiGrid grid;
    protected S service;
    List<String> headers = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    List<T> tableData = new ArrayList<>();

    public StandardForm(Class<T> entityClass, S service) {
        addClassName("demo-app-form");
        this.service = service;

        binder = new BeanValidationBinder<>(entityClass);
        name = new TextField("Name");
        description = new TextField("Description");
        save = new Button("Save");
        close = new Button("Cancel");

        headers = configureHeader(entityClass);
        configureGrid(entityClass);

        add(getToolbar(entityClass), grid);

//		add(name, description, createButtonsLayout());
        binder.bindInstanceFields(this);

    }

    private List<String> configureHeader(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (i != 0) {
                fieldNames.add(fields[i].getName());
            }
        }
        return fieldNames;
    }

    private void configureGrid(Class<T> entityClass) {
        grid = new TuiGrid();
        grid.addClassName("scheduler-grid");
        grid.setHeaders(headers);
        items = this.getTableData(entityClass);
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
            if (filterText != null)
                tableData = service.findAll(filterText.getValue());
            else
                tableData = service.findAll(null);

            Comparator<T> comparator = Comparator.comparing(item -> item.getName());
            Collections.sort(tableData, comparator);

            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();
            int columnIndex = item.getHeaders().indexOf(colName);
            if (event.getRow() >= tableData.size() - 1) {
                try {
                    tableData.add(entityClass.getDeclaredConstructor().newInstance());
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            T row = tableData.get(event.getRow());
            if (columnIndex >= 0) {
                String propertyName = headers.get(columnIndex);
                try {
                    Field field = entityClass.getDeclaredField(propertyName);
                    field.setAccessible(true);
                    field.set(row, event.getColValue());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
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

    private List<Item> getTableData(Class<T> entityClass) {

        List<Item> TableData = new ArrayList<>();
        if (filterText != null)
            tableData = service.findAll(filterText.getValue());
        else
            tableData = service.findAll(null);

        // Define a custom comparator to sort by the "No" column
        Comparator<T> comparator = Comparator.comparing(item -> item.getName());

        // Sort the TableData list using the custom comparator
        Collections.sort(tableData, comparator);

        for (T data :
                tableData) {
            List<String> rowData = new ArrayList<>(Arrays.asList(new String[headers.size()]));
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                try {
                    Field headerField = entityClass.getDeclaredField(header);
                    headerField.setAccessible(true);
                    rowData.set(i, String.valueOf(headerField.get(data)));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            TableData.add(new GuiItem(
                    rowData,
                    headers));

        }
        return TableData;
    }

    private List<Column> getColumns() {
        Column nameCol = new Column(new ColumnBaseOption(0, "Name", "name", 250, "center", ""));
        nameCol.setEditable(true);
        nameCol.setType("input");
        nameCol.setSortable(true);
        nameCol.setSortingType("asc");

        Column desCol = new Column(new ColumnBaseOption(1, "Description", "description", 250, "center", ""));
        desCol.setEditable(true);
        desCol.setType("input");
        desCol.setSortable(true);
        desCol.setSortingType("asc");

        List<Column> columns = List.of(nameCol, desCol);
        return columns;
    }

    private HorizontalLayout getToolbar(Class<T> entityClass) {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList(entityClass));

        var toolbar = new HorizontalLayout(filterText);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void updateList(Class<T> entityClass) {
        grid.setItems(this.getTableData(entityClass));
        add(grid);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setBean(T bean) {
        binder.setBean(bean);
    }

    private void delete() {
        if (grid.getCheckedItems().length == 0)
            return;
        for (int checkedRow :
                grid.getCheckedItems()) {
            service.delete(tableData.get(checkedRow));
        }
    }

    // Events
    public static abstract class StandardFormEvent<T extends ZJTEntity, S extends ZJTService<T>> extends ComponentEvent<StandardForm<T, S>> {
        private final T bean;

        protected StandardFormEvent(StandardForm<T, S> source, T bean) {
            super(source, false);
            this.bean = bean;
        }

        public T getBean() {
            return bean;
        }
    }

    public static class SaveEvent<T extends ZJTEntity, S extends ZJTService<T>> extends StandardFormEvent<T, S> {
        SaveEvent(StandardForm<T, S> source, T bean) {
            super(source, bean);
        }
    }

    public static class CloseEvent<T extends ZJTEntity, S extends ZJTService<T>> extends StandardFormEvent<T, S> {
        CloseEvent(StandardForm<T, S> source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent<T, S>> listener) {
        return addListener(SaveEvent.class, (ComponentEventListener) listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent<T, S>> listener) {
        return addListener(CloseEvent.class, (ComponentEventListener) listener);
    }
}