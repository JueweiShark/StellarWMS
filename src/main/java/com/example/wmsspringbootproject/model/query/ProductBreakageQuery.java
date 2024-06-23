package com.example.wmsspringbootproject.model.query;

import com.example.wmsspringbootproject.model.form.ProductBreakageForm;
import lombok.Data;

import java.util.List;

@Data
public class ProductBreakageQuery {
    private List<ProductBreakageForm> productBreakageForms;
    private String startTime;
    private String endTime;
}
