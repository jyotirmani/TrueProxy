package com.test.truproxyapi;

import com.test.truproxyapi.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootTest
class TruProxyApiApplicationTests {

	@Mock
	private CompanyRepository companyRepository;

	@Mock
	private RestTemplate restTemplate;

	@Test
	void contextLoads() {
		log.info("TruProxyApiApplicationTests: Context Load..");
	}

}
