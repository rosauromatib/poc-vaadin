package com.aat.application.views;


import com.aat.application.data.entity.ZJTElement;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.entity.ZJTResourceCategory;
import com.aat.application.data.entity.ZJTResourceType;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.RouterLink;
import org.springframework.core.annotation.AnnotationUtils;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.aat.application.components.appnav.AppNav;
import com.aat.application.components.appnav.AppNavItem;
import com.aat.application.views.about.AboutView;
import com.aat.application.views.planning.PlanningView;
import com.aat.application.views.vehicle.Vehicle0View;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
@Route(value = "")
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("JTT Scheduler");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

//        nav.addItem(new AppNavItem("Hello World", HelloWorldView.class, LineAwesomeIcon.GLOBE_SOLID.create()));
        nav.addItem(new AppNavItem("Vehicle List", Vehicle0View.class, LineAwesomeIcon.BABY_CARRIAGE_SOLID.create()));
        AppNavItem parent = new AppNavItem("Trip Module");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Product", ProductView.class, LineAwesomeIcon.PRODUCT_HUNT.create()));
        parent.addItem(new AppNavItem("Trip Component", TripComponentView.class, LineAwesomeIcon.TRIPADVISOR.create()));
        parent.addItem(new AppNavItem("Trip Itinerary", TripItineraryView.class, LineAwesomeIcon.TRIPADVISOR.create()));
        parent.addItem(new AppNavItem("Price List", PriceListView.class, LineAwesomeIcon.TRIPADVISOR.create()));
        parent.addItem(new AppNavItem("Quoting", QuoteView.class, LineAwesomeIcon.TRIPADVISOR.create()));

        parent = new AppNavItem("Resource Scheduling");
        parent.setIcon(LineAwesomeIcon.CALENDAR_ALT.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Schedule Planner", PlanningView.class, LineAwesomeIcon.CALENDAR_ALT.create()));

        parent = new AppNavItem("Vehicle Maintenance");
        parent.setIcon(LineAwesomeIcon.BUS_ALT_SOLID.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Vehicle", VehicleView.class, LineAwesomeIcon.BUS_SOLID.create()));

        parent = new AppNavItem("Setup Data");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Pricing Type", CommonView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(CommonView.class, "entityClass", ZJTPricingType.class.getSimpleName()));
        parent.addItem(new AppNavItem("Trip Element", CommonView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(CommonView.class, "entityClass", ZJTElement.class.getSimpleName()));
        parent.addItem(new AppNavItem("Resource Category", CommonView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(CommonView.class, "entityClass", ZJTResourceCategory.class.getSimpleName()));
        parent.addItem(new AppNavItem("Resource Type", CommonView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(CommonView.class, "entityClass", ZJTResourceType.class.getSimpleName()));
//        nav.addItem(new AppNavItem("Vehicle List", VehicleView.class, LineAwesomeIcon.BABY_CARRIAGE_SOLID.create()));

        parent = new AppNavItem("UI Test");
        parent.setIcon(LineAwesomeIcon.UIKIT.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("ToastUI Grid", UIToastGridView.class, LineAwesomeIcon.COMPASS.create()));

        nav.addItem(new AppNavItem("About", AboutView.class, LineAwesomeIcon.FILE.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
