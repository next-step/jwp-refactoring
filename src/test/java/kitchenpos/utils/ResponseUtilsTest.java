package kitchenpos.utils;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HTTP 응답 유틸리티 테스트")
public class ResponseUtilsTest {

	@DisplayName("Response CREATED 의 URL 을 만든다")
	@Test
	void createdTest() {
		String baseUrl = "/api/model";
		Long id = 1L;
		URI uri = ResponseUtils.createdUrl(baseUrl, id);
		assertThat(uri).hasToString(baseUrl + "/" + id);
	}
}
