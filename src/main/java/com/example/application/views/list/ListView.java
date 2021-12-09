package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Contacts | Vaadin CRM")
// The body of ListView gets loaded inside the MainLayout class
@Route(value = "", layout = MainLayout.class)
public class ListView extends VerticalLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm contactForm;

    private CrmService service;

    public ListView(CrmService service) {
        // Initializing the service to access the database
        this.service = service;

        // Css class
        addClassName("list-view");
        // This makes the list view full screen
        setSizeFull();
        
        configureGrid();
        configureForm();

        // Add the result and grid to the view
        add(
            getToolBar(),
            getContent()
        );

        updateList();
        closeEditor();
    }

    private void closeEditor() {
        // Remove the contact from the form and set it to invisible
        contactForm.setContact(null);
        contactForm.setVisible(false);
        removeClassName("editing");
    }

    // Method to update the list to the filter text
    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, contactForm);
        // Grid gets 2/3 of the space
        content.setFlexGrow(2, grid);
        // Contact form gets 1/3 of the space
        content.setFlexGrow(1, contactForm);
        content.setClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        contactForm = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        contactForm.setWidth("25em");

        // When a SaveEvent is called inside the form, run the saveContact method
        contactForm.addListener(ContactForm.SaveEvent.class, this::saveContact);
        contactForm.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        contactForm.addListener(ContactForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        // The event gets passed through from the ContactForm class in the createButtonLayout method
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void saveContact(ContactForm.SaveEvent event) {
        // The event gets passed through from the ContactForm class in the createButtonLayout method
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        // Clear button to clear the text in the text field
        filterText.setClearButtonVisible(true);
        // Optimiziation, so that we don't hit the database on every keystroke
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        // Update the list on keystroke change (LAZY MODE)
        filterText.addValueChangeListener(e -> updateList());

        // Add button
        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(e -> addContact());

        // Horizontal layout of the text field and button
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void addContact() {
        // Clear the selection, so it's not possible to select another contact
        grid.asSingleSelect().clear();
        // Creating a new form with an empty contact (So it can be filled)
        editContact(new Contact());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        
        // ..
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");

        // Auto column width
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        // Single select for selecting a single contact
        grid.asSingleSelect().addValueChangeListener(e -> editContact(e.getValue()));
    }

    private void editContact(Contact contact) {
        // If someone deselects a contact -> close the editor
        if (contact == null) {
            closeEditor();
        } else {
            contactForm.setContact(contact);
            contactForm.setVisible(true);
            contactForm.addClassName("editing");
        }
    }

}
