package kitchenpos.ui.product;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.ProductRestController;

@DisplayName("상품 컨트롤러 테스트")
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();
	}

	@DisplayName("create")
	@Test
	public void create() throws Exception {
		Product product = new Product(1L, "치킨", BigDecimal.valueOf(1_000));
		given(productService.create(any())).willReturn(product);

		mockMvc.perform(post("/api/products")
			.content(objectMapper.writeValueAsString(product))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/products/" + product.getId()))
		;
	}

	@DisplayName("list")
	@Test
	public void list() throws Exception {
		Product product = new Product(1L, "치킨", BigDecimal.valueOf(1_000));
		given(productService.create(any())).willReturn(product);

		mockMvc.perform(get("/api/products"))
			.andExpect(status().isOk())
		;
	}
}
