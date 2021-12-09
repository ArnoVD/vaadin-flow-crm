package com.example.application.views.list;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightCondition;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

// AppLayout implements the drawer
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createDrawer() {
        // Router link to navigate to the List View
        RouterLink listView =  new RouterLink("Contacts", ListView.class);
        // This is needed so that the empty path does not get triggered first
        listView.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listView
        ));
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        // Classes come from the vaadin utility classes
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        // Center the header
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        // Padding y-axis = 0, Padding x-axis = medium
        header.addClassNames("py-0", "px-m");

        // Vaadin method for adding the header to the drawer
        addToNavbar(header);
    }
}
