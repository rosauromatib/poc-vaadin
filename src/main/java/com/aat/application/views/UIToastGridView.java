package com.aat.application.views;

import java.util.List;

import com.vaadin.componentfactory.tuigrid.model.ComplexColumn;
import com.vaadin.componentfactory.tuigrid.model.DateOption;
import com.vaadin.componentfactory.tuigrid.model.Item;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.componentfactory.tuigrid.model.Summary;
import com.vaadin.componentfactory.tuigrid.model.ColumnBaseOption;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.Column;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Demo ToastUI Grid")
@Route(value = "tui-grid", layout = MainLayout.class)

public class UIToastGridView extends VerticalLayout {

    TextArea description = new TextArea("Description");

    public UIToastGridView() {
        description.setWidthFull();
        description.setHeight("15em");

        Button testButton = new Button("Test JS");
        testButton.addClickListener(click -> addGrid());
        testButton.setWidth("10em");
//        ToastUIGrid grid = new ToastUIGrid();
        TuiGrid grid = new TuiGrid(this.getCustomHeader(), this.getTableData(),
                this.getColumns(), this.getSummaries());
        grid.setHeaderHeight(100);
        grid.setSummaryHeight(40);
        add(testButton, description, grid);
    }


    private void addGrid() {

    }

    private List<Item> getTableData() {
        List<String> headers = List.of("name", "artist", "type", "genre", "release", "price", "download", "listen");

        List<Item> TableData = List.of(
                new GuiItem(List.of("Beautiful Lies", "Birdy", "Deluxe;", "Pop", "2016-03-26", "10000", "1000", "10050"), headers),
                new GuiItem(List.of(
                        "X",
                        "Ed Sheeran",
                        "Deluxe;",
                        "",
                        "",
                        "20000",
                        "1900",
                        "2005"), headers),
                new GuiItem(List.of(
                        "Moves Like Jagger",
                        "Maroon5",
                        "Single;",
                        "Pop,Rock",
                        "2011-08-08",
                        "7000",
                        "11000",
                        "3100"
                ), headers),
                new GuiItem(List.of(
                        "A Head Full Of Dreams",
                        "Coldplay",
                        "Deluxe;",
                        "Rock",
                        "2015-12-04",
                        "25000",
                        "2230",
                        "4030"
                ), headers),
                new GuiItem(List.of(
                        "21",
                        "Adele",
                        "Deluxe;",
                        "Pop,R&B",
                        "2011-01-21",
                        "15000",
                        "1007",
                        "12000"
                ), headers),
                new GuiItem(List.of(
                        "Warm On A Cold Night",
                        "HONNE",
                        "EP;",
                        "R&B,Electronic",
                        "2016-07-22",
                        "11000",
                        "1502",
                        "5000"
                ), headers),
                new GuiItem(List.of(
                        "Take Me To The Alley",
                        "Gregory Porter",
                        "Deluxe;",
                        "Jazz",
                        "2016-09-02",
                        "30000",
                        "1200",
                        "5003"
                ), headers),
                new GuiItem(List.of(
                        "Make Out",
                        "LANY",
                        "EP;",
                        "Electronic",
                        "2015-12-11",
                        "12000",
                        "8005",
                        "9000"
                ), headers),
                new GuiItem(List.of(
                        "Get Lucky",
                        "Daft Punk",
                        "Single",
                        "Pop,Funk",
                        "2013-04-23",
                        "9000",
                        "11000",
                        "1500"
                ), headers),
                new GuiItem(List.of(
                        "Valtari",
                        "Sigur Rós",
                        "EP;",
                        "Rock",
                        "2012-05-31",
                        "10000",
                        "9000",
                        "8010"
                ), headers),
                new GuiItem(List.of(
                        "Bush",
                        "Snoop Dogg",
                        "EP",
                        "Hiphop",
                        "2015-05-12",
                        "18000",
                        "3000",
                        "2005"
                ), headers),
                new GuiItem(List.of(
                        "Chaos And The Calm",
                        "James Bay",
                        "EP",
                        "Pop,Rock",
                        "2015-03-23",
                        "12000",
                        "8007",
                        "9000"
                ), headers),
                new GuiItem(List.of(
                        "4",
                        "Beyoncé",
                        "Deluxe",
                        "Pop",
                        "2011-07-26",
                        "12000",
                        "7000",
                        "11002"
                ), headers),
                new GuiItem(List.of(
                        "I Won't Give Up",
                        "Jason Mraz",
                        "Single",
                        "Pop",
                        "2012-01-03",
                        "7000",
                        "8000",
                        "2000"
                ), headers),
                new GuiItem(List.of(
                        "Following My Intuition",
                        "Craig David",
                        "Deluxe",
                        "R&B,Electronic",
                        "2016-10-01",
                        "15000",
                        "9001",
                        "8100"
                ), headers),
                new GuiItem(List.of(
                        "Blue Skies",
                        "Lenka",
                        "Single",
                        "Pop,Rock",
                        "2015-03-18",
                        "6000",
                        "11000",
                        "9000"
                ), headers),
                new GuiItem(List.of(
                        "This Is Acting",
                        "Sia",
                        "EP",
                        "Pop",
                        "2016-10-22",
                        "20000",
                        "11400",
                        "5800"
                ), headers),
                new GuiItem(List.of(
                        "Blurryface",
                        "Twenty One Pilots",
                        "EP",
                        "Rock",
                        "2015-05-19",
                        "13000",
                        "6010",
                        "3020"
                ), headers),
                new GuiItem(List.of(
                        "I am Not The Only One",
                        "Sam Smith",
                        "Single",
                        "Pop,R&B",
                        "",
                        "",
                        "",
                        ""
                ), headers));

        return TableData;
    }

    private List<Column> getColumns() {
        List<Column> columns = List.of(
                new Column(new ColumnBaseOption(0, "Name", "name", 250, "center", "")),
                new Column(new ColumnBaseOption(1, "Artist", "artist", 250, "center", ""), true, "input"),
                new Column(new ColumnBaseOption(2, "Type", "type", 150, "center", ""), true, "input"),
                new Column(new ColumnBaseOption(3, "Genre", "genre", 150, "center", "tui-grid-cell-required"), true, "input"),
                new Column(new ColumnBaseOption(4, "Release", "release", 150, "center", "tui-grid-cell-required"), true, "datePicker", new DateOption("yyyy-MM-dd", false)),
                new Column(new ColumnBaseOption(5, "Price", "price", 150, "center", ""), "asc", true),
                new Column(new ColumnBaseOption(6, "Download", "download", 150, "center", "")),
                new Column(new ColumnBaseOption(7, "Listen", "listen", 150, "center", "")));
        return columns;
    }

    private List<Summary> getSummaries() {
        List<Summary> summaries = List.of(
                new Summary("price", Summary.OperationType.sum),
                new Summary("download", Summary.OperationType.avg),
                new Summary("listen", Summary.OperationType.max));
        return summaries;
    }

    private List<ComplexColumn> getCustomHeader() {
        List<ComplexColumn> customHeaders = List.of(
                new ComplexColumn("Details Info", "Details Info", List.of("type", "genre", "release")),
                new ComplexColumn("Count", "Count", List.of("download", "listen")),
                new ComplexColumn("Extra Info", "Extra Info", List.of("price", "Count")));
        return customHeaders;
    }
}
