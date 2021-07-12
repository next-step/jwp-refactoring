package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest extends WebMvcTestConfiguration {
	@MockBean
	private ProductService productService;

	@Test
	void createTest() throws Exception {
		given(productService.create(any())).willReturn(new Product());

		mockMvc.perform(post("/api/products")
			.content(objectMapper.writeValueAsString(new Product())).contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void listTest() throws Exception {
		given(productService.list()).willReturn(Arrays.asList(new Product("양념치킨", new BigDecimal(10000)), new Product("알싸한 마늘 치킨", new BigDecimal(20000))));

		mockMvc.perform(get("/api/products"))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
