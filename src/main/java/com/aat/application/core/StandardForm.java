package com.aat.application.form;

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

public abstract class StandardForm<T> extends FormLayout {

	private static final long serialVersionUID = -5183438338263448739L;

	protected Binder<T> binder;
	protected TextField name;
	protected TextField description;
	protected Button save;
	protected Button close;

	public StandardForm(Class<T> entityClass) {
		addClassName("demo-app-form");

		binder = new BeanValidationBinder<>(entityClass);
		name = new TextField("Name");
		description = new TextField("Description");
		save = new Button("Save");
		close = new Button("Cancel");

		add(name, description, createButtonsLayout());
		binder.bindInstanceFields(this);
	}

	private HorizontalLayout createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);

		save.addClickListener(event -> validateAndSave());
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));

		return new HorizontalLayout(save, close);
	}

	private void validateAndSave() {
		if (binder.isValid()) {
			fireEvent(new SaveEvent(this, binder.getBean()));
		}
	}

	public void setBean(T bean) {
		binder.setBean(bean);
	}

	// Events
	public static abstract class StandardFormEvent<T> extends ComponentEvent<StandardForm<T>> {
		private final T bean;

		protected StandardFormEvent(StandardForm<T> source, T bean) {
			super(source, false);
			this.bean = bean;
		}

		public T getBean() {
			return bean;
		}
	}

	public static class SaveEvent<T> extends StandardFormEvent<T> {
		SaveEvent(StandardForm<T> source, T bean) {
			super(source, bean);
		}
	}

	public static class CloseEvent<T> extends StandardFormEvent<T> {
		CloseEvent(StandardForm<T> source) {
			super(source, null);
		}
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent<T>> listener) {
		return addListener(SaveEvent.class, (ComponentEventListener) listener);
	}

	public Registration addCloseListener(ComponentEventListener<CloseEvent<T>> listener) {
		return addListener(CloseEvent.class, (ComponentEventListener) listener);
	}
}