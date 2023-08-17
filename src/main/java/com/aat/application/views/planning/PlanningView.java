package com.aat.application.views.planning;

import com.aat.application.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Planning")
@Route(value="planning", layout=MainLayout.class)
public class PlanningView extends VerticalLayout {

	public PlanningView() {
		H1 h1 = new H1("Planning Window - TODO");
		
		add(h1);
	}

}
