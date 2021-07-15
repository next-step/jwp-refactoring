package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@DisplayName("상품 기능")
@SpringBootTest
class ProductRestControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRestController productRestController;

	private Product product;

	@BeforeEach
	void setup() {
		// MockMvc
		mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
			.addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
			.alwaysDo(print())
			.build();

		product = new Product();
		product.setId(1L);
		product.setName("양념치킨");
		product.setPrice(new BigDecimal(16000));
	}

	@Test
	@DisplayName("상품을 생성할 수 있다.")
	public void create() throws Exception {
		// given
		given(productService.create(any())).willReturn(product);

		// when
		final ResultActions actions = 상품_생성_요청();

		// then
		상품_생성에_성공함(actions, product);
	}

	@Test
	@DisplayName("상품 목록을 조회할 수 있다.")
	public void list() throws Exception {
		// given
		given(productService.list()).willReturn(Arrays.asList(product));

		// when
		final ResultActions actions = 상품_조회_요청();

		// then
		상품_조회에_성공함(actions, product);
	}

	private ResultActions 상품_생성_요청() throws Exception {
		return mockMvc.perform(post("/api/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(product)));
	}

	private void 상품_생성에_성공함(ResultActions actions, Product product) throws Exception {
		actions.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/products" + "/1"))
			.andExpect(content().string(containsString(product.getName())))
			.andExpect(content().string(containsString(String.valueOf(product.getPrice())))
			);
	}

	private ResultActions 상품_조회_요청() throws Exception {
		return mockMvc.perform(get("/api/products")
			.contentType(MediaType.APPLICATION_JSON));
	}

	private void 상품_조회에_성공함(ResultActions actions, Product product) throws Exception {
		actions.andExpect(status().isOk())
			.andExpect(content().string(containsString(product.getName())))
			.andExpect(content().string(containsString(String.valueOf(product.getPrice()))));
	}
}
