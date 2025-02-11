package com.test.truproxyapi.dto;

import com.test.truproxyapi.model.Company;
import lombok.Data;

import java.util.List;

@Data
public class CompanyResponse {
    private int totalResults;
    private List<Company> items;
}
