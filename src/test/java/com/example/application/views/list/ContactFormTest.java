package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ContactFormTest {
    private List<Company> companies;
    private List<Status> statuses;
    private Contact billyBunter;
    private Company company1;
    private Company company2;
    private Status status1;
    private Status status2;

    // Before every test we run the setup
    @Before 
    public void setupData() {
        // List of companies
        companies = new ArrayList<>();
        company1 = new Company();
        company1.setName("Vaadin Ltd");
        company2 = new Company();
        company2.setName("IT Mill");
        companies.add(company1);
        companies.add(company2);

        // List of statuses
        statuses = new ArrayList<>();
        status1 = new Status();
        status1.setName("Status 1");
        status2 = new Status();
        status2.setName("Status 2");
        statuses.add(status1);
        statuses.add(status2);

        // Create a contact
        billyBunter = new Contact();
        billyBunter.setFirstName("Billy");
        billyBunter.setLastName("Bunter");
        billyBunter.setEmail("billy@bunter.com");
        billyBunter.setStatus(status1);
        billyBunter.setCompany(company2);
    }

    @Test
    public void formFieldsPopulated() {
        ContactForm contactForm = new ContactForm(companies, statuses);
        contactForm.setContact(billyBunter);

        Assert.assertEquals("Billy", contactForm.firstName.getValue());
        Assert.assertEquals("Bunter", contactForm.lastName.getValue());
        Assert.assertEquals("billy@bunter.com", contactForm.email.getValue());
        Assert.assertEquals(company2, contactForm.company.getValue());
        Assert.assertEquals(status1, contactForm.status.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        ContactForm contactForm = new ContactForm(companies, statuses);
        Contact contact = new Contact();
        contactForm.setContact(contact);

        contactForm.firstName.setValue("John");
        contactForm.lastName.setValue("Doe");
        contactForm.email.setValue("john@doe.com");
        contactForm.company.setValue(company1);
        contactForm.status.setValue(status2);

        // When we save something we get an event back
        // With AtomicReference this event can be captured
        AtomicReference<Contact> savedContact =  new AtomicReference<>(null);
        contactForm.addListener(ContactForm.SaveEvent.class, e -> savedContact.set(e.getContact()));

        // Click on the save button
        contactForm.saveButton.click();

        // This saved object has all the information that was saved
        Contact saved = savedContact.get();

        Assert.assertEquals("John", saved.getFirstName());
        Assert.assertEquals("Doe", saved.getLastName());
        Assert.assertEquals("john@doe.com", saved.getEmail());
        Assert.assertEquals(company1, saved.getCompany());
        Assert.assertEquals(status2, saved.getStatus());
    }
}