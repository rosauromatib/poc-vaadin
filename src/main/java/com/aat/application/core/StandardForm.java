package com.aat.application.core;

import com.aat.application.data.entity.ElementList;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.Column;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class StandardForm<T extends ZJTEntity, S extends ZJTService<T>> extends FormLayout {

    private static final long serialVersionUID = -5183438338263448739L;

    protected Binder<T> binder;
    protected TextField filterText = new TextField();
    protected Button save;
    protected Button close;
    protected TuiGrid grid;
    protected S service;
    List<String> headers;
    Dictionary<String, String> headerOptions = new Hashtable<>();
    Dictionary<String, Class<?>> headerTypeOptions = new Hashtable<>();
    List<Item> items = new ArrayList<>();
    List<T> tableData = new ArrayList<>();
    Span sp = new Span("Here is : ");

    public StandardForm(Class<T> entityClass, S service) {
        addClassName("demo-app-form");
        this.service = service;

        binder = new BeanValidationBinder<>(entityClass);
        save = new Button("Save");
        close = new Button("Cancel");

        headers = configureHeader(entityClass);
        configureGrid(entityClass);

        add(sp, getToolbar(entityClass), grid);

//        binder.bindInstanceFields(this);
    }

    private List<String> configureHeader(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();
        for (int i = 1; i < fields.length; i++) {
            if (fields[i].getAnnotation(jakarta.persistence.Column.class) != null) {

                fieldNames.add(fields[i].getName());
                headerOptions.put(fields[i].getName(), "input");
            }
            if (fields[i].getAnnotation(jakarta.persistence.Enumerated.class) != null) {
                fieldNames.add(fields[i].getName());
                headerTypeOptions.put(fields[i].getName(), fields[i].getType());
                headerOptions.put(fields[i].getName(), "select_enum");
            }
            if (fields[i].getAnnotation(jakarta.persistence.JoinColumn.class) != null) {
                fieldNames.add(fields[i].getName());
                headerOptions.put(fields[i].getName(), "select_class");
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
        grid.setSelectTheme(inputTheme);

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
            if (event.getRow() >= tableData.size()) {
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
                    switch (headerOptions.get(propertyName)) {
                        case "input":
                            field.set(row, event.getColValue());
                            break;
                        case "select_enum":
                            Class<?> enumTypes = headerTypeOptions.get(propertyName);
                            if (enumTypes.isEnum()) {
                                Enum<?>[] enumConstants = ((Class<Enum<?>>) enumTypes).getEnumConstants();
                                int ordinal = Integer.parseInt(event.getColValue().substring(0, 1)) - 1;
                                if (ordinal >= 0 && ordinal < enumConstants.length) {
                                    Enum<?> enumValue = enumConstants[ordinal];
                                    field.set(row, enumValue);
                                }
                            }
                            break;
                        default:
                            field.set(row, event.getColValue());
                            break;
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            sp.add(" it's " + row);

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

        Comparator<T> comparator = Comparator.comparing(item -> item.getName());
        Collections.sort(tableData, comparator);

        for (T data :
                tableData) {
            List<String> rowData = new ArrayList<>(Arrays.asList(new String[headers.size()]));
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                try {
                    String headerName = header.substring(0, 1).toLowerCase()
                            + header.substring(1);
                    Field headerField = entityClass.getDeclaredField(headerName);
                    headerField.setAccessible(true);
                    switch (headerOptions.get(header)) {
                        case "input":
                            rowData.set(i, String.valueOf(headerField.get(data)));
                            break;
                        case "select_enum":
                            rowData.set(i, String.valueOf(((Enum) headerField.get(data)).ordinal() + 1));
                            break;
                        default:
                            rowData.set(i, "");
                            break;
                    }

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

    private List<com.vaadin.componentfactory.tuigrid.model.Column> getColumns() {
        List<com.vaadin.componentfactory.tuigrid.model.Column> columns = new ArrayList<>();
        int nId = 0;

        Enumeration<String> e = headerOptions.keys();
        while (e.hasMoreElements()) {
            String header = e.nextElement();
            String headerName = header.substring(0, 1).toUpperCase()
                    + header.substring(1);
            ColumnBaseOption baseOption =
                    new ColumnBaseOption(nId++, headerName, header, 250, "center", "");
            com.vaadin.componentfactory.tuigrid.model.Column column =
                    new com.vaadin.componentfactory.tuigrid.model.Column(baseOption);
            column.setEditable(true);
            column.setSortable(true);
            column.setSortingType("asc");
//            if (headerOptions.get(header) == "input")
                column.setType("input");
            if (headerOptions.get(header) == "select_enum") {
                column.setType("select");
                column.setRoot(true);
                column.setTarget("");
                List<RelationOption> elementsList = new ArrayList<>();
                Class<?> fieldEnum = headerTypeOptions.get(header);
                int index = 1;
                for (Enum elementList : ((Class<Enum>) fieldEnum).getEnumConstants()) {
                    RelationOption option = new RelationOption(elementList.toString(), String.valueOf(index++));
                    elementsList.add(option);
                }
                column.setRelationOptions(elementsList);
            }
            columns.add(column);
        }

//        List<Column> columns = List.of(nameCol, desCol);
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

//        save.addClickListener(event -> validateAndSave());
//        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

//    private void validateAndSave() {
//        if (binder.isValid()) {
//            fireEvent(new SaveEvent(this, binder.getBean()));
//        }
//    }
//
//    public void setBean(T bean) {
//        binder.setBean(bean);
//    }

    private void delete() {
        if (grid.getCheckedItems().length == 0)
            return;
        for (int checkedRow :
                grid.getCheckedItems()) {
            service.delete(tableData.get(checkedRow));
        }
    }
//
//    // Events
//    public static abstract class StandardFormEvent<T extends ZJTEntity, S extends ZJTService<T>> extends ComponentEvent<StandardForm<T, S>> {
//        private final T bean;
//
//        protected StandardFormEvent(StandardForm<T, S> source, T bean) {
//            super(source, false);
//            this.bean = bean;
//        }
//
//        public T getBean() {
//            return bean;
//        }
//    }
//
//    public static class SaveEvent<T extends ZJTEntity, S extends ZJTService<T>> extends StandardFormEvent<T, S> {
//        SaveEvent(StandardForm<T, S> source, T bean) {
//            super(source, bean);
//        }
//    }
//
//    public static class CloseEvent<T extends ZJTEntity, S extends ZJTService<T>> extends StandardFormEvent<T, S> {
//        CloseEvent(StandardForm<T, S> source) {
//            super(source, null);
//        }
//    }
//
//    public Registration addSaveListener(ComponentEventListener<SaveEvent<T, S>> listener) {
//        return addListener(SaveEvent.class, (ComponentEventListener) listener);
//    }
//
//    public Registration addCloseListener(ComponentEventListener<CloseEvent<T, S>> listener) {
//        return addListener(CloseEvent.class, (ComponentEventListener) listener);
//    }
}