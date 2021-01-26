package kitchenpos.menu.ui;

import api.config.ExceptionMessage;
import kitchenpos.MockMvcTest;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductRestControllerTest extends MockMvcTest {

	@DisplayName("상품을 생성한다.")
	@Test
	void create() throws Exception {
		ProductRequest productRequest = new ProductRequest("제품", new BigDecimal(1000L));

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/products", productRequest))
				.andExpect(status().isCreated())
				.andReturn();

		ProductResponse created = toObject(mvcResult, ProductResponse.class);

		assertThat(created.getId()).isNotNull();
		assertThat(created.getName()).isEqualTo("제품");
		assertThat(created.getPrice().longValue()).isEqualTo(1000L);
	}

	@DisplayName("잘못된 가격으로 상품을 생성한다.")
	@Test
	void create_WringPrice() throws Exception {
		ProductRequest productRequest = new ProductRequest("제품", new BigDecimal(-1));

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/products", productRequest))
				.andExpect(status().isBadRequest())
				.andReturn();

		ExceptionMessage result = toObject(mvcResult, ExceptionMessage.class);
		assertThat(result.getMessage()).isNotEmpty();
		assertThat(result.getType()).contains("Validation");
	}

	@DisplayName("상품을 조회한다.")
	@Test
	void list() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/products")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<ProductResponse> products = toList(mvcResult, ProductResponse.class);
		assertThat(products).isNotEmpty();
	}
}
