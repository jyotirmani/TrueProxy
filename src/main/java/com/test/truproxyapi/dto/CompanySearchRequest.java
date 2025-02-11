package com.test.truproxyapi.dto;

import lombok.Data;

@Data
public class CompanySearchRequest {
    private String companyName;
    private String companyNumber;
    private boolean onlyActive;
}
