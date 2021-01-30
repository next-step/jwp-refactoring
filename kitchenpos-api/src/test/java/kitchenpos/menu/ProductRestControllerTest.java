package kitchenpos.menu;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductService productService;

	@DisplayName("상품 등록 요청")
	@Test
	void create() throws Exception {
		ProductResponse savedProduct = new ProductResponse(1L, "강정치킨", 17000);

		when(productService.create(any(ProductRequest.class))).thenReturn(savedProduct);

		mockMvc.perform(
			post("/api/products")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\n"
					+ "  \"name\": \"강정치킨\",\n"
					+ "  \"price\": 17000\n"
					+ "}")
		).andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/products/1"));
	}

	@DisplayName("상품 목록 조회 요청")
	@Test
	void list() throws Exception {
		ProductResponse savedProduct = new ProductResponse(1L, "강정치킨", 17000);

		List<ProductResponse> products = new ArrayList<>();
		products.add(savedProduct);

		when(productService.list()).thenReturn(products);

		mockMvc.perform(get("/api/products"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(products)));

	}
}
