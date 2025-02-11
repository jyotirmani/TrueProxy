Goal
Create a company search application using Spring Boot 3.1.3 or higher.

Expose an endpoint that uses the TruProxyAPI to do a company and officer lookup via name or registration number.

Criteria
The result of the search is returned as JSON
A request parameter has to be added to decide whether only active companies should be returned
The officers of each company have to be included in the company details (new field officers)
Only include officers that are active (resigned_on is not present in that case)
Paging can be ignored
Please add unit tests and integrations tests, e.g. using WireMock to mock TruProxyAPI calls
Expected Request

The name and registration/company number are passed in via body
The API key is passed in via header x-api-key
If both fields are provided companyNumber is used
{
    "companyName" : "BBC LIMITED",
    "companyNumber" : "06500244"
}
Expected Response

Not all fields that are returned from TruProxyAPI are required. The final JSON should look like this :
{
    "total_results": 1,
    "items": [
        {
            "company_number": "06500244",
            "company_type": "ltd",
            "title": "BBC LIMITED",
            "company_status": "active",
            "date_of_creation": "2008-02-11",
            "address": {
                "locality": "Retford",
                "postal_code": "DN22 0AD",
                "premises": "Boswell Cottage Main Street",
                "address_line_1": "North Leverton",
                "country": "England"
            },
            "officers": [
                {
                    "name": "BOXALL, Sarah Victoria",
                    "officer_role": "secretary",
                    "appointed_on": "2008-02-11",
                    "address": {
                        "premises": "5",
                        "locality": "London",
                        "address_line_1": "Cranford Close",
                        "country": "England",
                        "postal_code": "SW20 0DP"
                    }
                }
            ]
        }
    ]
}
Bonus
Save the companies (by company_number) and its officers and addresses in a database and return the result from there if the endpoint is called with companyNumber.
Example API Requests
Search for Company:
https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Search?Query={search_term}

Response Example
Get Company Officers:
https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber={number}

Response Example
API documentation
Authentication:
Use the API key provided in your request header when calling the endpoints.
Example: curl -s -H 'x-api-key: xxxxxxxxxxxxx' "https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber=10241297"

API credentials will be provided seperately

