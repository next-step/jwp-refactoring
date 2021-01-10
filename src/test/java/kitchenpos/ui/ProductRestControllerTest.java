package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.domain.TestDomainConstructor;
import kitchenpos.domain.Product;

@DisplayName("상품 Controller 테스트")
public class ProductRestControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("상품을 등록할 수 있다 - 상품 등록 후, 등록된 상품의 아이디를 포함한 정보를 반환한다.")
	void create() throws Exception {
		//given
		String name = "상품1";
		int price = 1000;
		Product product = TestDomainConstructor.product(name, price);

		//when-then
		mockMvc.perform(post("/api/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(product)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.price").value(price));
	}

	@Test
	@DisplayName("상품의 목록을 조회할 수 있다.")
	void list() throws Exception {
		//when-then
		mockMvc.perform(get("/api/products"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isNotEmpty())
			.andExpect(jsonPath("$..id").isNotEmpty())
			.andExpect(jsonPath("$..name").isNotEmpty())
			.andExpect(jsonPath("$..price").isNotEmpty());
	}
}

