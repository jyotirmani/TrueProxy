package com.test.truproxyapi.service;

import com.test.truproxyapi.dto.CompanyResponse;
import com.test.truproxyapi.dto.OfficerResponse;
import com.test.truproxyapi.model.*;
import com.test.truproxyapi.repository.AddressRepository;
import com.test.truproxyapi.repository.CompanyRepository;
import com.test.truproxyapi.repository.OfficerRepository;
import com.test.truproxyapi.util.DataLoader;
import com.test.truproxyapi.util.UtilityHelpler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.test.truproxyapi.util.RequestUtil.appendUriQueryParam;
import static com.test.truproxyapi.util.RequestUtil.httpEntity;

@Service
public class CompanySearchService {
    private final RestTemplate resetTemplate;
    private final OfficeSearchService officeSearchService;

    private final CompanyRepository companyRepository;

    @Autowired
    private OfficerRepository officerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Value("${companies.search.url}")
    public String companiesSearchUrl;

    @Value("${truproxy.api.url}")
    private String truProxyApiUrl;

    @Value("${truproxy.api.key}")
    private String apiKey;

    @Autowired
    public CompanySearchService(RestTemplate resetTemplate, OfficeSearchService officeSearchService, CompanyRepository companyRepository) {
        this.resetTemplate = resetTemplate;
        this.officeSearchService = officeSearchService;
        this.companyRepository = companyRepository;
    }

    public Companies searchCompanies(String apiKey, boolean active) {
        String searchUrl = appendUriQueryParam(companiesSearchUrl, "Query", "companies");
        ResponseEntity<String> response = resetTemplate.exchange(searchUrl, HttpMethod.GET,  httpEntity(apiKey), String.class);
        Companies companies = DataLoader.buildCompanies(response.getBody(), active);
        officeSearchService.searchOfficers1(apiKey, companies);
        //save to DB
        List<CompanyInfo> companyInfos = companyRepository.findAll();
        if (companyInfos.isEmpty()) {
            save(companies);
        }

        return companies;
    }

    private void save(Companies companies) {
        List<CompanyInfo> companyInfos = UtilityHelpler.buildCompanyInfo(companies);
        companyRepository.saveAll(companyInfos);
    }

    public Companies findByCompaniesById(boolean active, String companyNumber) {
        List<Company> items = new ArrayList<>();
        CompanyInfo companyInfo = companyRepository.findByCompanyNumber(companyNumber);
        AddressInfo address = companyInfo.getAddress();

        OfficerInfo officerInfo = officerRepository.findByCompanyNumber(companyNumber);
        List<AddressInfo> all = addressRepository.findAll();
        List<OfficerInfo> officerInfos = companyInfo.getOfficerInfos();
        companyInfo.setOfficerInfos(officerInfos);
        Company company =  Company.builder().company_number(companyNumber).company_status(companyInfo.getCompanyStatus()
        ).address(Address.builder().country(address.getCountry()).build()).build();

        List<Company> company1 = List.of(company);
        return Companies.builder().items(company1).total_results(company1.size()).build();
    }

    public Company getCompanyByNumber(String companyNumber, boolean onlyActive) {
        String url = String.format("%s/Companies/v1/Search?Query=%s", truProxyApiUrl, companyNumber);
        CompanyResponse response = resetTemplate.getForObject(url, CompanyResponse.class);

        if (response != null && response.getTotalResults() > 0) {
            Company company = response.getItems().get(0);

            // Add officer details
            String officersUrl = String.format("%s/Companies/v1/Officers?CompanyNumber=%s", truProxyApiUrl, companyNumber);
            OfficerResponse officerResponse = resetTemplate.getForObject(officersUrl, OfficerResponse.class);

            if (officerResponse != null) {
                List<Officer> officers = officerResponse.getOfficers().stream()
                        .filter(officer -> officer.getAppointed_on() == null)  // filter out resigned officers
                        .collect(Collectors.toList());
                company.setOfficers(officers);
            }

            return company;
        }

        return null;
    }
}
