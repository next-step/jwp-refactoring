package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.ProductService;

import kitchenpos.domain.Product;

class ProductRestControllerTest extends IntegrationTest {

	private static final String BASE_PATH = "/api/products";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@DisplayName("상품 등록")
	@Test
	void create() throws Exception {
		//given
		String productName = "육개장";
		BigDecimal price = BigDecimal.valueOf(9000);
		Map<String, String> params = 상품정보(productName, price);
		Product expectedProduct = new Product(1L, productName, price);
		given(productService.create(any()))
			.willReturn(expectedProduct);

		//when
		MockHttpServletResponse response = mockMvc.perform(post(BASE_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(params))
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		Product savedProduct = objectMapper.readValue(response.getContentAsString(), Product.class);
		assertThat(savedProduct).isEqualTo(expectedProduct);
	}

	@DisplayName("상품 목록 조회")
	@Test
	void list() throws Exception {
		//given
		List<Product> products = Arrays.asList(
			new Product(1L, "육개장", BigDecimal.valueOf(9000)),
			new Product(1L, "과메기", BigDecimal.valueOf(22000)));
		given(productService.list())
			.willReturn(products);

		//when
		MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH)
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		List<Product> findProducts = objectMapper.readValue(response.getContentAsString(),
			new TypeReference<List<Product>>() {
			});
		assertThat(findProducts).containsAll(products);
	}

	private Map<String, String> 상품정보(String productName, BigDecimal price) {
		Map<String, String> params = new HashMap<>();
		params.put("productName", productName);
		params.put("price", String.valueOf(price));
		return params;
	}
}
