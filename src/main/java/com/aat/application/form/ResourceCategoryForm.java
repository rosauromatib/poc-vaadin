package com.aat.application.form;

import com.aat.application.data.entity.ZJTResourceCategory;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class ResourceCategoryForm extends FormLayout {


    private static final long serialVersionUID = -5183438338263448739L;

    Binder<ZJTResourceCategory> binder = new BeanValidationBinder<>(ZJTResourceCategory.class);

    TextField name = new TextField("Name");
    TextField description = new TextField("Description");

    Button save = new Button("Save");
    //	Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public ResourceCategoryForm() {
        addClassName("demo-app-form");

        add(name, description, createButtonsLayout());
        binder.bindInstanceFields(this);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);

        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
//		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(save, close);

    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setBean(ZJTResourceCategory bean) {
        binder.setBean(bean);
    }

    // Events
    public static abstract class ResourceCategoryFormEvent extends ComponentEvent<ResourceCategoryForm> {
        private final ZJTResourceCategory bean;

        protected ResourceCategoryFormEvent(ResourceCategoryForm source, ZJTResourceCategory bean) {


            super(source, false);
            this.bean = bean;
        }

        public ZJTResourceCategory getBean() {
            return bean;
        }
    }

    public static class SaveEvent extends ResourceCategoryFormEvent {
        SaveEvent(ResourceCategoryForm source, ZJTResourceCategory bean) {
            super(source, bean);
        }
    }

    public static class DeleteEvent extends ResourceCategoryFormEvent {
        DeleteEvent(ResourceCategoryForm source, ZJTResourceCategory bean) {
            super(source, bean);
        }

    }

    public static class CloseEvent extends ResourceCategoryFormEvent {
        CloseEvent(ResourceCategoryForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}


