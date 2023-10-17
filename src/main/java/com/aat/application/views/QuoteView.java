package com.aat.application.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.aat.application.data.entity.ZJXPriceListRow;
import com.aat.application.data.entity.ZJTPricelist;
import com.aat.application.data.entity.ZJTProduct;
import com.aat.application.data.service.QuoteService;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Quoting")
@Route(value = "quote", layout = MainLayout.class)

public class QuoteView extends VerticalLayout {

    private final QuoteService service;

    TextField priceListText = new TextField();
    ComboBox<ZJTPricelist> pricelistCombo = new ComboBox<>("Price List");
    ComboBox<ZJTProduct> itiCombo = new ComboBox<>("Itineraries");

    //    private Grid<PriceListRow> grid = new Grid<>(PriceListRow.class, false);
    private final BeanValidationBinder<ZJXPriceListRow> binder = new BeanValidationBinder<>(ZJXPriceListRow.class);
    TuiGrid grid;
    List<ZJXPriceListRow> listPricingType;


    private List<ZJTPricelist> m_pricelists;
    private List<ZJTProduct> m_itineraries;

    public QuoteView(QuoteService service) {
        this.service = service;

        setSizeFull();

        configureGrid();

//		grid = configureGrid();
        add(getHeaderPanel(), grid);

        loadInit();
    }

    private List<Item> getTableData() {

        List<Item> TableData = new ArrayList<>();
        try {
            listPricingType = service.getTabulatedItems(pricelistCombo.getValue());

            Comparator<ZJXPriceListRow> comparator = Comparator.comparing(item -> item.getName());
            Collections.sort(listPricingType, comparator);

            List<String> headers = List.of("name", "vehicle_hours", "vehicle_km", "driver_hours", "over_head", "profit_margin");
            for (ZJXPriceListRow ZJXPriceListRow :
                    listPricingType) {
                TableData.add(new GuiItem(List.of(ZJXPriceListRow.getName(),
                        String.valueOf(ZJXPriceListRow.getVehicleHours()),
                        String.valueOf(ZJXPriceListRow.getVehicleKM()),
                        String.valueOf(ZJXPriceListRow.getDriverHours()),
                        String.valueOf(ZJXPriceListRow.getOverHead()),
                        String.valueOf(ZJXPriceListRow.getProfitMargin())),
                        headers));

            }
        } catch (Exception e) {
            TableData = null;
        }
        return TableData;
    }

    private List<Column> getColumns() {
        Column nameCol = new Column(new ColumnBaseOption(0, "Name", "name", 150, "center", ""));
        nameCol.setEditable(true);
        nameCol.setMaxLength(10);
        nameCol.setType("input");
        nameCol.setSortable(true);
        nameCol.setSortingType("asc");

        Column vhCol = new Column(new ColumnBaseOption(1, "Vehicle Hours", "vehicle_hours", 0, "center", ""));
        vhCol.setEditable(true);
        vhCol.setMaxLength(10);
        vhCol.setType("input");
        vhCol.setSortable(true);
        vhCol.setSortingType("asc");

        Column vkCol = new Column(new ColumnBaseOption(2, "Vehicle KM", "vehicle_km", 0, "center", ""));
        vkCol.setEditable(true);
        vkCol.setMaxLength(10);
        vkCol.setType("input");
        vkCol.setSortable(true);
        vkCol.setSortingType("asc");

        Column dhCol = new Column(new ColumnBaseOption(3, "Driver Hours", "driver_hours", 0, "center", ""));
        dhCol.setEditable(true);
        dhCol.setMaxLength(10);
        dhCol.setType("input");
        dhCol.setSortable(true);
        dhCol.setSortingType("asc");

        Column ohCol = new Column(new ColumnBaseOption(4, "Over Head", "over_head", 0, "center", ""));
        ohCol.setEditable(true);
        ohCol.setMaxLength(10);
        ohCol.setType("input");
        ohCol.setSortable(true);
        ohCol.setSortingType("asc");

        Column pmCol = new Column(new ColumnBaseOption(5, "Profit Margin", "profit_margin", 0, "center", ""));
        pmCol.setEditable(true);
        pmCol.setMaxLength(10);
        pmCol.setType("input");
        pmCol.setSortable(true);
        pmCol.setSortingType("asc");

        List<Column> columns = List.of(nameCol, vhCol, vkCol, dhCol, ohCol, pmCol);
        return columns;
    }


    private void configureGrid() {
        grid = new TuiGrid();
        grid.setItems(this.getTableData());
        grid.setColumns(this.getColumns());
        grid.addClassName("scheduler-grid");
        grid.setSizeFull();

        Theme inputTheme = new Theme();
        inputTheme.setMaxLength(10);
        inputTheme.setBorder("1px solid #326f70");
        inputTheme.setBackgroundColor("#66878858");
        inputTheme.setOutline("none");
        inputTheme.setWidth("90%");
        inputTheme.setHeight("100%");
        inputTheme.setOpacity(1);

        grid.setInputTheme(inputTheme);
//        Editor<PriceListRow> editor = grid.getEditor();
//        editor.setBinder(binder);
//        editor.setBuffered(false);

//        grid.setColumns("name", "vehicleHours", "vehicleKM", "driverHours", "overHead", "profitMargin");
//        grid.getColumnByKey("vehicleHours").setTextAlign(ColumnTextAlign.END);
//        grid.getColumnByKey("vehicleKM").setTextAlign(ColumnTextAlign.END);
//        grid.getColumnByKey("driverHours").setTextAlign(ColumnTextAlign.END);
//        grid.getColumnByKey("overHead").setTextAlign(ColumnTextAlign.END);
//        grid.getColumnByKey("profitMargin").setTextAlign(ColumnTextAlign.END);

        NumberField vhField = new NumberField();
        vhField.setWidthFull();
//		binder.forField(vhField)
//			.withConverter(null)
//			.bind(PriceListRow::getVehicleHours, PriceListRow::setVehicleHours);


//        grid.addItemDoubleClickListener(e -> {
//            editor.editItem(e.getItem());
//            Component editorComponent = e.getColumn().getEditorComponent();
//            if (editorComponent instanceof Focusable) {
//                ((Focusable) editorComponent).focus();
//            }
//        });
    }

//	private Grid<List<Object>> configureGrid() {
//		final Grid<List<Object>> grid = new Grid<>();
//
//		grid.addClassName("scheduler-grid");
//		Binder<List<Object>> binder = new Binder<>();
//		grid.getEditor().setBinder(binder);
//		grid.getEditor().setBuffered(false);
//
//		TextField textField = new TextField();
//		binder.forField(textField).wit
//			//.bind(list -> list.g
//
//		grid.addColumn("name");
//
//		grid.setSizeFull();
//		//grid.setColumns("name", "vehicleHours", "vehicleKM", "driverHours", "overHead", "profitMargin");
////		grid.addco
//
//		return grid;
//	}

    private Component getHeaderPanel() {
//        priceListText.setPlaceholder("New Pricelist name ...");
//        priceListText.setClearButtonVisible(true);
//        priceListText.setWidth("20em");
//
//        Button addPricelistButton = new Button("Add pricelist");
//        addPricelistButton.addClickListener(click -> addPricelist());
//        addPricelistButton.setWidth("10em");
//
//        Button savePricelistButton = new Button("Save pricelist");
//        savePricelistButton.addClickListener(click -> savePriceList());
//        savePricelistButton.setWidth("10em");

        pricelistCombo.setWidth("20em");
//        pricelistCombo.addValueChangeListener(e -> updatePriceTable());

        itiCombo.setWidth("20em");
//        itiCombo.addValueChangeListener(e -> updateList());


//        var toolbar = new HorizontalLayout(priceListText,  addPricelistButton, pricelistCombo, savePricelistButton); 
        var toolbar = new HorizontalLayout(pricelistCombo, itiCombo);

        toolbar.addClassName("toolbar");

        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    }

    private void addPricelist() {
        ZJTPricelist pl = new ZJTPricelist();
        pl.setName(priceListText.getValue());
        service.save(pl);
        priceListText.clear();

        m_pricelists.add(pl);
        pricelistCombo.setItems(m_pricelists);
        pricelistCombo.setValue(pl);
    }

    private void savePriceList() {
//		if (m_activeLegs == null) {
//			
//		}
//		HashMap<String, ZJTProduct> legsMap = new HashMap<>();
//		
//		m_activeLegs.stream().forEach(leg -> legsMap.put(leg.getZjt_product_id()+"", leg));
//		
//		List<Item> items = timeline.getItems();
//		
//		for (Item item : items) {
//			ZJTProduct leg = legsMap.get(item.getId());
//			
////			leg.setTriptime(item.getStart());
//			
//		}
//		//m_activeLegs.stream().filter(s -> (s.getZjt_product_id() +"").equals(it))
    }

    private void updatePriceTable() {

        List<ZJXPriceListRow> rows = service.getTabulatedItems(pricelistCombo.getValue());

//        grid.setItems(rows);
//		legsCombo.setItems(new ArrayList<ZJTProduct>());
//		ZJTProduct itinerary = itiCombo.getValue();
//		if (itinerary == null) {
//			return;
//		}
//		
//		m_dayOffset = 0;
//		m_lastEndDate =  LocalDateTime.now().withHour(3).withMinute(0);
//		timelineItems.clear();
//		
//		m_activeLegs = service.getItineraryLegs(itinerary);
//		
//		if (m_activeLegs == null) {
//			m_activeLegs = new ArrayList<>();
//		}
//		
//		legsCombo.setItems(m_activeLegs);
//		
//		for (ZJTProduct leg : m_activeLegs) {
//			addLegToTimeline(leg);
//			
//		}
////		m_lastEndDate = end;
////		
////		for (int i=1; i < 7; i++) {
////			Item item = new Item(start, end, "Item" + i);
////			timelineItems.add(item);
////			start = end;
////			end = start.plusMinutes(60);
////			
////		}
//		timeline.setItems(timelineItems, false);
////		timeline.setTimelineRange(m_lastEndDate.withHour(3), m_lastEndDate.withHour(22));
//		timeline.setZoomOption(1);
//		timeline.setAutoZoom(false);
//		
//		String s = service.getLegsInJSON(itinerary.getZjt_product_id());
//		
//		JsonPivotData pd = new JsonPivotData(s);
//		PivotOptions pivotOptions = new PivotOptions();
//		pivotOptions.setRows("tripleg");
//		pivotOptions.setCols("elementname");
//		pivotOptions.setAggregator(Aggregator.COUNT, "elementname");
//		pivotOptions.setCharts(false);
//		pivotOptions.setFieldsDisabled(false);
//		PivotTable table = new PivotTable(pd, pivotOptions, PivotMode.INTERACTIVE);
//		description.setValue(s);
//		
//		if (lastPivotTable != null) {
//			this.remove(lastPivotTable);
//		}
//		lastPivotTable = table;
//		this.add(table);

    }

    private void loadInit() {
        m_pricelists = service.getPriceLists(null);

        pricelistCombo.setItems(m_pricelists);
        pricelistCombo.setItemLabelGenerator(ZJTPricelist::getName);

        m_itineraries = service.findAllTripItineraries(null);

        itiCombo.setItems(m_itineraries);
        itiCombo.setItemLabelGenerator(ZJTProduct::getName);
    }

}
