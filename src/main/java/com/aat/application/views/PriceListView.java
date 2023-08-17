package com.aat.application.views;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import com.aat.application.data.entity.PriceListRow;
import com.aat.application.data.entity.ZJTPricelist;
import com.aat.application.data.entity.ZJTProduct;
import com.aat.application.data.service.PricelistService;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.Column;
import com.vaadin.componentfactory.tuigrid.model.ColumnBaseOption;
import com.vaadin.componentfactory.tuigrid.model.Item;
import com.vaadin.componentfactory.tuigrid.model.RelationItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Price List")
@Route(value="pricelist", layout = MainLayout.class)

public class PriceListView extends VerticalLayout{

	private PricelistService service;
	
	TextField priceListText = new TextField();
	ComboBox<ZJTPricelist> pricelistCombo = new ComboBox<>("Price List");

	private Grid<PriceListRow> grid = new Grid<>(PriceListRow.class, false);
//	TuiGrid grid;
	private BeanValidationBinder<PriceListRow> binder = new BeanValidationBinder<>(PriceListRow.class);
	
	private List<ZJTPricelist> m_pricelists;
	
	public PriceListView(PricelistService service)
	{
		this.service = service;
//		grid = new TuiGrid(null, this.getTableData(),
//				this.getColumns(), null);
		setSizeFull();
		
		configureGrid();
		
//		grid = configureGrid();
		add(getPricelistPanel(), grid);
		
		loadPricelist();
	}



	private void configureGrid()
	{
		grid.addClassName("scheduler-grid");
		grid.setSizeFull();
		Editor<PriceListRow> editor = grid.getEditor();
		editor.setBinder(binder);
		editor.setBuffered(false);
		
		grid.setColumns("name", "vehicleHours", "vehicleKM", "driverHours", "overHead", "profitMargin");
		grid.getColumnByKey("vehicleHours").setTextAlign(ColumnTextAlign.END);
		grid.getColumnByKey("vehicleKM").setTextAlign(ColumnTextAlign.END);
		grid.getColumnByKey("driverHours").setTextAlign(ColumnTextAlign.END);
		grid.getColumnByKey("overHead").setTextAlign(ColumnTextAlign.END);
		grid.getColumnByKey("profitMargin").setTextAlign(ColumnTextAlign.END);
		
		NumberField vhField = new NumberField();
		vhField.setWidthFull();
//		binder.forField(vhField)
//			.withConverter(null)
//			.bind(PriceListRow::getVehicleHours, PriceListRow::setVehicleHours);



		grid.addItemDoubleClickListener(e -> {
			editor.editItem(e.getItem());
		    Component editorComponent = e.getColumn().getEditorComponent();
		    if (editorComponent instanceof Focusable) {
		        ((Focusable) editorComponent).focus();
		    }
		});
//		grid.setHeaderHeight(100);
//		grid.setTableWidth(750);
//		grid.setTableHeight(750);
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

	private Component getPricelistPanel()
	{
        priceListText.setPlaceholder("New Pricelist name ...");
        priceListText.setClearButtonVisible(true);
        priceListText.setWidth("20em");

        Button addPricelistButton = new Button("Add pricelist");
        addPricelistButton.addClickListener(click -> addPricelist());
        addPricelistButton.setWidth("10em");

        Button savePricelistButton = new Button("Save pricelist");
        savePricelistButton.addClickListener(click -> savePriceList());
        savePricelistButton.setWidth("10em");

        pricelistCombo.setWidth("20em");

        pricelistCombo.addValueChangeListener(e -> updatePriceTable());

        var toolbar = new HorizontalLayout(priceListText,  addPricelistButton, pricelistCombo, savePricelistButton); 

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

	private void savePriceList()
	{
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
		
		List<PriceListRow> rows = service.getTabulatedItems(pricelistCombo.getValue());
		
		grid.setItems(rows);
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
	
	private void loadPricelist()
	{
		m_pricelists = service.getPriceLists(null);
		
		pricelistCombo.setItems(m_pricelists);
		pricelistCombo.setItemLabelGenerator(ZJTPricelist::getName);
	}

}
