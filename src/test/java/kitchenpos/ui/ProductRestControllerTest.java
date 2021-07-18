package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("상품 컨트롤러 테스트")
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
	private static final String BASE_URL = "/api/products";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private ProductRestController productRestController;

	@MockBean
	private ProductService productService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(print())
				.build();
	}

	@Test
	void createTest() throws Exception {
		Product product = new Product(1L);
		given(productService.create(product)).willReturn(product);

		mockMvc.perform(
				post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(product)))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", BASE_URL + "/1"));
	}

	@Test
	void listTest() throws Exception {
		List<Product> products = Lists.list(new Product("피자", BigDecimal.valueOf(30000)), new Product());
		given(productService.list()).willReturn(products);

		mockMvc.perform(
				get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("피자")));
	}
}
