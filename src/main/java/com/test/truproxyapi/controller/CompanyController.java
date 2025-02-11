package com.test.truproxyapi.controller;

import com.test.truproxyapi.dto.CompanySearchRequest;
import com.test.truproxyapi.model.Company;
import com.test.truproxyapi.service.CompanySearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanySearchService companyService;

    public CompanyController(CompanySearchService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/search")
    public Company searchCompany(@RequestBody CompanySearchRequest request, @RequestHeader("x-api-key") String apiKey) {
        boolean onlyActive = request.isOnlyActive();
        return companyService.getCompanyByNumber(request.getCompanyNumber() , onlyActive);
    }
}