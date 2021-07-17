package kitchenpos.product.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.WebMvcTestConfiguration;

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest extends WebMvcTestConfiguration {
	@MockBean
	private ProductService productService;

	@Test
	void createTest() throws Exception {
		given(productService.create(any())).willReturn(new ProductResponse());

		mockMvc.perform(post("/api/products")
			.content(objectMapper.writeValueAsString(new ProductRequest())).contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void listTest() throws Exception {
		given(productService.list()).willReturn(Arrays.asList(new ProductResponse(), new ProductResponse()));

		mockMvc.perform(get("/api/products"))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
