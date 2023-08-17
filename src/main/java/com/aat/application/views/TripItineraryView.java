package com.aat.application.views;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.vaadin.addons.componentfactory.PivotTable;
import org.vaadin.addons.componentfactory.PivotTable.Aggregator;
import org.vaadin.addons.componentfactory.PivotTable.JsonPivotData;
import org.vaadin.addons.componentfactory.PivotTable.PivotMode;
import org.vaadin.addons.componentfactory.PivotTable.PivotOptions;

import com.aat.application.data.entity.TripLegType;
import com.aat.application.data.entity.TripType;
import com.aat.application.data.entity.ZJTComponentLine;
import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTProduct;
import com.aat.application.data.service.DemoService;
import com.aat.application.data.service.ProductService;
import com.aat.application.form.ProductForm;
import com.aat.application.form.ProductForm.DeleteEvent;
import com.aat.application.form.ProductForm.SaveEvent;
import com.vaadin.componentfactory.timeline.Timeline;
import com.vaadin.componentfactory.timeline.event.ItemResizeEvent;
import com.vaadin.componentfactory.timeline.event.ItemsDragAndDropEvent;
import com.vaadin.componentfactory.timeline.model.GroupItem;
import com.vaadin.componentfactory.timeline.model.Item;
import com.vaadin.componentfactory.timeline.model.SnapStep;
import com.vaadin.external.apache.commons.fileupload2.MultipartStream.ItemInputStream;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

//@CssImport(value = "./styles/timeline-items-style.css")
@PageTitle("Trip Itinerary")
@Route(value = "tripitinerary", layout = MainLayout.class )

public class TripItineraryView  extends VerticalLayout{

//	private Grid<ZJTProduct> grid = new Grid<>(ZJTProduct.class);
	
	Timeline timeline = null;
	
	//Itinerary Panel
	TextField itiText = new TextField();
	ComboBox<ZJTProduct> itiCombo = new ComboBox<>("Itineraries");

	//Leg Panel
	TextField legText = new TextField();
	ComboBox<ZJTProduct> legsCombo = new ComboBox<>("Legs");
	
	//Leg Detail Panel
	ComboBox<TripLegType> legTypeCombo = new ComboBox<>("Leg Type");
	TimePicker startTimePicker = new TimePicker("Start");
	NumberField hourNum = new NumberField("Hours");
	NumberField kmNum = new NumberField("KM");
	NumberField eachNum = new NumberField("Each");
	MultiSelectComboBox<ZJTElement> elementCombo = new MultiSelectComboBox<>("Trip Element");

	TextArea description  = new TextArea("Description");
	
	
//	private ProductForm form;
	
	private ProductService service;
	private ArrayList<GroupItem> timelineGroups = new ArrayList<GroupItem>();
	private ArrayList<Item> timelineItems = new ArrayList<Item>();
	private List<ZJTProduct> m_itineraries;
	
	private int m_dayOffset = 0;
	private LocalDateTime m_lastEndDate;

	private List<ZJTProduct> m_activeLegs;

	private List<ZJTElement> m_tripElements;

	private boolean onLegLoading;

	private PivotTable lastPivotTable;
	
	public TripItineraryView(ProductService service) {
		this.service = service;
		
		
		setSizeFull();
		
		configureTimeline();
//		configureForm();
//		getContent();
		
		description.setVisible(false);  //hide for debugging json only
		description.setWidthFull();
		description.setHeight("15em");
//		description.setValue("dafjhofdaoo0w4ehnkegfnl");
		legTypeCombo.setItems(TripLegType.values());
		add(getItineraryPanel(), getLegPanel(), getLegDetailPanel(), timeline, description);
		updateList();
//		closeEditor();
	}

	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(timeline);
	      content.addClassNames("content");
	      content.setSizeFull();
		return content;
//		HorizontalLayout content = new HorizontalLayout(timeline, form);
//		content.setFlexGrow(1, form);
//        content.addClassNames("content");
//        content.setSizeFull();
//        return content;
	}

    private HorizontalLayout getItineraryPanel() {
//        filterText.addValueChangeListener(e -> updateList());

        itiText.setPlaceholder("New Itinerary name ...");
        itiText.setClearButtonVisible(true);
        itiText.setWidth("20em");

        Button addItineraryButton = new Button("Add itinerary");
        addItineraryButton.addClickListener(click -> add());
        addItineraryButton.setWidth("10em");

        Button saveItineraryButton = new Button("Save itinerary");
        saveItineraryButton.addClickListener(click -> save());
        saveItineraryButton.setWidth("10em");

        itiCombo.setWidth("20em");

        itiCombo.addValueChangeListener(e -> updateList());

        var toolbar = new HorizontalLayout(itiText,  addItineraryButton, itiCombo, saveItineraryButton); 

        toolbar.addClassName("toolbar"); 

        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    }
    
    private HorizontalLayout getLegPanel()
    {
    	
        legText.setPlaceholder("New Leg ...");
        legText.setClearButtonVisible(true);
        legText.setWidth("20em");

        Button addLegButton = new Button("Add Leg");
        addLegButton.addClickListener(click -> addLeg());
        addLegButton.setWidth("10em");

        Button saveLegButton = new Button("Save Leg");
        saveLegButton.addClickListener(click -> saveLeg());
        saveLegButton.setWidth("10em");
        
        legsCombo.setWidth("20em");
        legsCombo.setItemLabelGenerator(ZJTProduct::getName);
        legsCombo.addValueChangeListener(e -> updateDetail());

        var toolbar = new HorizontalLayout(legText,  addLegButton, legsCombo, saveLegButton); 

        toolbar.addClassName("toolbar"); 

        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    }
    
    private HorizontalLayout getLegDetailPanel()
    {

    	m_tripElements = service.getTripElements();
		elementCombo.setItems(m_tripElements);
    	elementCombo.setItemLabelGenerator(ZJTElement::getName);
    	elementCombo.setWidth("40em");
    	
    	startTimePicker.setStep(Duration.ofMinutes(15));
    	startTimePicker.setWidth("10em");
    	kmNum.setWidth("4em");
    	hourNum.setWidth("4em");
    	eachNum.setWidth("4em");
    	
    	legTypeCombo.addValueChangeListener(e -> updateTimelineLeg());
    	hourNum.addValueChangeListener(e -> updateTimelineLeg());
    	startTimePicker.addValueChangeListener(e -> updateTimelineLeg());
        var toolbar = new HorizontalLayout(legTypeCombo, startTimePicker, hourNum, kmNum, eachNum, elementCombo); 

//        toolbar.addClassName("toolbar"); 

        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    } 

	private void configureTimeline() {
		
		loadItinerary();
		timeline  = new Timeline(timelineItems, timelineGroups);
		
		timeline.setHeight("10em");
		LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0);
		timeline.setTimelineRange(start
				, start.plusDays(1));
	    // add listener to get new resized item range values
	    timeline.addItemResizeListener(e -> updateLegOnItemResize(e));
	    timeline.addItemsDragAndDropListener(e -> updateLegOnDragEvent(e));
	    timeline.setSnapStep(SnapStep.QUARTER);
	    timeline.addItemSelectListener(e -> {
	    	timeline.onSelectItem(e.getTimeline(), e.getItemId(), false);
	    	ZJTProduct leg = m_activeLegs.stream().filter(s -> (s.getZjt_product_id() +"").equals(e.getItemId())).findFirst().get();
	    	legsCombo.setValue(leg);
	    });
	}
	
	private String formatDates(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	}

	private void configureForm()
	{
//		form = new ProductForm(service.getResourceTypes(), service.getTripElements());
//		form.setWidth("25em");
//
//		form.addSaveListener(this::save);
//		form.addDeleteListener(this::delete);
//		form.addCloseListener(e -> closeEditor());
	}

	private void updateList() {
		legsCombo.setItems(new ArrayList<ZJTProduct>());
		ZJTProduct itinerary = itiCombo.getValue();
		if (itinerary == null) {
			return;
		}
		
		m_dayOffset = 0;
		m_lastEndDate =  LocalDateTime.now().withHour(3).withMinute(0);
		timelineItems.clear();
		
		m_activeLegs = service.getItineraryLegs(itinerary);
		
		if (m_activeLegs == null) {
			m_activeLegs = new ArrayList<>();
		}
		
		legsCombo.setItems(m_activeLegs);
		
		for (ZJTProduct leg : m_activeLegs) {
			addLegToTimeline(leg);
			
		}
//		m_lastEndDate = end;
//		
//		for (int i=1; i < 7; i++) {
//			Item item = new Item(start, end, "Item" + i);
//			timelineItems.add(item);
//			start = end;
//			end = start.plusMinutes(60);
//			
//		}
		timeline.setItems(timelineItems, false);
//		timeline.setTimelineRange(m_lastEndDate.withHour(3), m_lastEndDate.withHour(22));
		timeline.setZoomOption(1);
		timeline.setAutoZoom(false);
		
		String s = service.getLegsInJSON(itinerary.getZjt_product_id());
		
		JsonPivotData pd = new JsonPivotData(s);
		PivotOptions pivotOptions = new PivotOptions();
		pivotOptions.setRows("tripleg");
		pivotOptions.setCols("elementname");
		pivotOptions.setAggregator(Aggregator.COUNT, "elementname");
		pivotOptions.setCharts(false);
		pivotOptions.setFieldsDisabled(false);
		PivotTable table = new PivotTable(pd, pivotOptions, PivotMode.INTERACTIVE);
		description.setValue(s);
		
		if (lastPivotTable != null) {
			this.remove(lastPivotTable);
		}
		lastPivotTable = table;
		this.add(table);

	}

	private Item addLegToTimeline(ZJTProduct leg) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(leg.getTriptime().getTime());
		
		int dayoffset = leg.getTripdayoffset() == null ? 0 : leg.getTripdayoffset();
		
		LocalDateTime start = LocalDateTime.now()
				.withHour(cal.get(Calendar.HOUR_OF_DAY))
				.withMinute(cal.get(Calendar.MINUTE))
				.plusDays(dayoffset);
		
		int duration = leg.getTripleghour() == null ? 60 : (int)(leg.getTripleghour().doubleValue() * 60); 
		LocalDateTime end = start.plusMinutes(duration);

		m_lastEndDate = m_lastEndDate.isAfter(end) ? m_lastEndDate : end;

		String className;
		switch (leg.getTriplegtype()) {
			case LR : className = "green"; break;
			case LS : className = "red"; break;
			case UR : className = "orange"; break;
			default : className = "blue"; break;
		}
	
	
		Item item = new Item(start, end, leg.getName());
		item.setTitle(item.getContent());
		item.setId(leg.getZjt_product_id() + "");
		item.setEditable(true);
		item.setUpdateTime(true);
		item.setClassName(className);

		timelineItems.add(item);
		return item;
	}
	
	private void updateDetail()
	{
		onLegLoading = true;
		ZJTProduct leg = legsCombo.getValue();
		elementCombo.deselectAll();
		if (leg == null) {
			legTypeCombo.setValue(null);
			kmNum.setValue(0.0);
			hourNum.setValue(0.0);
			eachNum.setValue(0.0);
			startTimePicker.setValue(null);
			return;
		}
		timeline.setSelectItem(leg.getZjt_product_id() + "");
		legTypeCombo.setValue(leg.getTriplegtype());
		kmNum.setValue(leg.getTriplegkm() == null ? 0 : leg.getTriplegkm().doubleValue());
		hourNum.setValue(leg.getTripleghour() == null ? 0 : leg.getTripleghour().doubleValue());
		eachNum.setValue(leg.getTriplegeach() == null ? 0 : leg.getTriplegeach().doubleValue());

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(leg.getTriptime().getTime());

		LocalTime time = LocalTime.of(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		startTimePicker.setValue(time);
		
		elementCombo.select(service.getLegElements(leg, m_tripElements));
		
		onLegLoading = false;
	}
	
	private void updateTimelineLeg()
	{
		if (onLegLoading) {
			return;
		}
		ZJTProduct leg = legsCombo.getValue();
		if (leg == null) {
			return;
		}
		Item item = timelineItems.stream().filter(i -> i.getId().equals(leg.getZjt_product_id()+"")).findFirst().get();
		
		
		LocalDateTime start = item.getStart()
				.withHour(startTimePicker.getValue().getHour())
				.withMinute(startTimePicker.getValue().getMinute());
		
		long minutes = (long) (hourNum.getValue() * 60);
		LocalDateTime end = start.plusMinutes(minutes);
		
		String className;
		switch (legTypeCombo.getValue()) {
			case LR : className = "green"; break;
			case LS : className = "red"; break;
			case UR : className = "orange"; break;
			default : className = "blue"; break;
		}
		item.setStart(start);
		item.setEnd(end);
		item.setClassName(className);
		
	
		redrawTimeline(item.getId());
	}
	
	private void redrawTimeline(String selectedID)
	{
		//TODO - must reorder / stack the trip with no overlapping / spacing
		
//		List<Item> sortedItems = timelineItems.stream()
//				.sorted((v1, v2) -> v1.getStart().compareTo(v2.getStart())).collect(Collectors.toList()); 

//		timelineItems.clear();
//
//		m_lastEndDate
//		sortedItems.stream().forEach(item -> {
//			m_lastEndDate = item.getEnd();
//			
//		});
		timeline.setItems(timelineItems, false);
		timeline.setSelectItem(selectedID);
		
	}
	
//	public void edit(ZJTProduct product)
//	{
//		if (product == null) {
//			closeEditor();
//		} else {
//			form.setProduct(product);
//			form.setVisible(true);
//			addClassName("editing");
//		}
//	}
	
	private void add()
	{
//		grid.asSingleSelect().clear();
		//edit(new ZJTProduct());
		ZJTProduct itinerary = new ZJTProduct();
		itinerary.setTripType(TripType.TI);
		itinerary.setName(itiText.getValue());
		service.save(itinerary);
		
		GroupItem group = new GroupItem();
		group.setId((timelineGroups.size() + 1) + "");
		group.setContent(itinerary.getName());
//		group.setTitle(group.getContent());
		timelineGroups.add(group);
//		timeline.setGroups(timelineGroups);
//		timeline.setg
//	    Item item5 =
//		        new Item(
//		            LocalDateTime.of(2023, 6, 13, 12, 30, 00),
//		            LocalDateTime.of(2023, 6, 13, 14, 30, 00),
//		            itinerary.getName());
//		    item5.setId("8");
//		    item5.setGroup("1");
//		    item5.setTitle(item5.getContent());
//		    item5.setEditable(true);
//		    item5.setUpdateTime(true);
//		    
//		timeline.addItem(item5);
		
		m_itineraries.add(itinerary);
		itiCombo.setItems(m_itineraries);
		itiCombo.setValue(itinerary);
		
	}
	
	/**
	 * Save Itinerary and it's legs
	 */
	private void save()
	{
		if (m_activeLegs == null) {
			
		}
		HashMap<String, ZJTProduct> legsMap = new HashMap<>();
		
		m_activeLegs.stream().forEach(leg -> legsMap.put(leg.getZjt_product_id()+"", leg));
		
		List<Item> items = timeline.getItems();
		
		for (Item item : items) {
			ZJTProduct leg = legsMap.get(item.getId());
			
//			leg.setTriptime(item.getStart());
			
		}
		//m_activeLegs.stream().filter(s -> (s.getZjt_product_id() +"").equals(it))
	}

	private void addLeg()
	{
		ZJTProduct parent = itiCombo.getValue();
		
		ZJTProduct leg = new ZJTProduct();
		leg.setTripType(TripType.LI);
		leg.setName(legText.getValue());
		leg.setTripdayoffset(m_dayOffset);
		leg.setTriptime(Timestamp.valueOf(m_lastEndDate));
		leg.setTriplegtype(TripLegType.US);
		service.save(leg);
		
		ZJTComponentLine line = new ZJTComponentLine();
		line.setParent(parent);
		line.setProduct(leg);
		service.saveLine(line);
		
		m_activeLegs.add(leg);
		legsCombo.setItems(m_activeLegs);
		legsCombo.setValue(leg);
		
		Item item = addLegToTimeline(leg);
		
		redrawTimeline(item.getId());
	}
	
	private void saveLeg()
	{
		ZJTProduct leg = legsCombo.getValue();
		
		leg.setTriplegtype(legTypeCombo.getValue());
		LocalDateTime ldt = LocalDateTime.now().withHour(startTimePicker.getValue().getHour())
				.withMinute(startTimePicker.getValue().getMinute()).withSecond(0).withNano(0);
		leg.setTriptime(Timestamp.valueOf(ldt));
		leg.setTripleghour(BigDecimal.valueOf(hourNum.getValue()));
		leg.setTriplegkm(BigDecimal.valueOf(kmNum.getValue()));
		leg.setTriplegeach(BigDecimal.valueOf(eachNum.getValue()));
		service.save(leg);
		
		List<ZJTElement>items =  new ArrayList<ZJTElement> (elementCombo.getSelectedItems());
		service.setLegElements(legsCombo.getValue(), items);
	}

	private void loadItinerary()
	{
		m_itineraries = service.findAllTripItineraries(null);
		
		itiCombo.setItems(m_itineraries);
		itiCombo.setItemLabelGenerator(ZJTProduct::getName);
		
				
	}

//	private void closeEditor() {
//		form.setProduct(null);
//		form.setVisible(false);
//		removeClassName("editing");
//		
//	}

//	private void save(ProductForm.SaveEvent event)
//	{
//		service.save(event.getProduct());
//		updateList();
//		closeEditor();
//	}
//	
//	private void delete(ProductForm.DeleteEvent event)
//	{
//		service.delete(event.getProduct());
//		updateList();
//		closeEditor();
//	}
	
	private void updateLegOnDragEvent(ItemsDragAndDropEvent e)
	{
		List<Item> items = e.getItems();
		
		if (items.size() != 1) {
			return;  //TODO - know what to do if there are more than 1 item dragged
		}
		Timestamp start = Timestamp.valueOf(items.get(0).getStart());
//		long offset = Math.round((Duration.between(items.get(0).getStart(),items.get(0).getStart()).toMinutes()+2)/5*5);
		ZJTProduct leg = m_activeLegs.stream().filter(s -> (s.getZjt_product_id() +"").equals(items.get(0).getId())).findFirst().get();
		leg.setTriptime(start);
		
		if (legsCombo.getValue() != leg) {
			legsCombo.setValue(leg); 
		} else {
			updateDetail();
		}
	}
	private void updateLegOnItemResize(ItemResizeEvent e) 
	{
		Timestamp start = Timestamp.valueOf(e.getNewStart());
		long offset = Math.round((Duration.between(e.getNewStart(),e.getNewEnd()).toMinutes()+2)/5*5);
		BigDecimal hourOffset = new BigDecimal(offset/60.0);
		//e.getItemId()
		
		ZJTProduct leg = m_activeLegs.stream().filter(s -> (s.getZjt_product_id() +"").equals(e.getItemId())).findFirst().get();
		leg.setTriptime(start);
		leg.setTripleghour(hourOffset);
		
		if (legsCombo.getValue() != leg) {
			legsCombo.setValue(leg); 
		} else {
			updateDetail();
		}
	}
}
