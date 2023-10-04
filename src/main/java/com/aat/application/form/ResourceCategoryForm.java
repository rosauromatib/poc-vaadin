package com.aat.application.form;

import com.aat.application.core.StandardForm;
import com.aat.application.data.entity.ZJTResourceCategory;
import com.aat.application.data.service.ResourceCategoryService;

public class ResourceCategoryForm extends StandardForm<ZJTResourceCategory, ResourceCategoryService> {
    public ResourceCategoryForm(ResourceCategoryService service) {
        super(ZJTResourceCategory.class, service);
        addClassName("demo-app-form");
    }
}