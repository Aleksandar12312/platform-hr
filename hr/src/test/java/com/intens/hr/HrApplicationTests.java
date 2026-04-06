package com.intens.hr;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = HrApplication.class)
class HrApplicationTests {

	@Test
	void contextLoads() {
	}

}
