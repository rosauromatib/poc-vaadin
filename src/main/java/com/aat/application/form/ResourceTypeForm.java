package com.aat.application.form;

import com.aat.application.core.StandardForm;
import com.aat.application.data.entity.ZJTResourceType;
import com.aat.application.data.service.ResourceTypeService;

public class ResourceTypeForm extends StandardForm<ZJTResourceType, ResourceTypeService> {
    public ResourceTypeForm(ResourceTypeService service) {
        super(ZJTResourceType.class, service);
        addClassName("demo-app-form");
    }
}