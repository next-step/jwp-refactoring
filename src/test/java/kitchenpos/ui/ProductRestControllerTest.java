package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.dto.ProductRequest;

@DisplayName("ProductRestController 테스트")
class ProductRestControllerTest extends BaseControllerTest {

	@DisplayName("Product 생성 요청")
	@Test
	void create() throws Exception {
		int expectedId = 7;
		String name = "피자";
		int price = 15000;
		ProductRequest product = TestDataUtil.createProduct(name, price);

		mockMvc.perform(post("/api/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(product)))
			.andDo(print())
			.andExpect(header().string("Location", "/api/products/" + expectedId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(expectedId))
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.price").value(price));
	}

	@DisplayName("Product 목록 조회")
	@Test
	void list() throws Exception {
		mockMvc.perform(get("/api/products"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(6)));
	}
}