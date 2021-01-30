package kitchenpos.menu;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.dto.MenuRequest;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MenuService menuService;

	@DisplayName("메뉴 등록 요청")
	@Test
	void create() throws Exception {
		ProductResponse product = new ProductResponse(1L, "후라이드", 17000);
		MenuProductResponse menuProduct = new MenuProductResponse(1L, 1L, product, 2);
		MenuGroupResponse menuGroup = new MenuGroupResponse(1L, "추천메뉴");
		MenuResponse savedMenu = new MenuResponse(1L, "후라이드+후라이드", 19000, menuGroup,
			Collections.singletonList(menuProduct));

		when(menuService.create(any(MenuRequest.class))).thenReturn(savedMenu);

		mockMvc.perform(
			post("/api/menus")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\n"
					+ "  \"name\": \"후라이드+후라이드\",\n"
					+ "  \"price\": 19000,\n"
					+ "  \"menuGroupId\": 1,\n"
					+ "  \"menuProducts\": [\n"
					+ "    {\n"
					+ "      \"productId\": 1,\n"
					+ "      \"quantity\": 2\n"
					+ "    }\n"
					+ "  ]\n"
					+ "}")
		)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/menus/1"));
	}

	@DisplayName("메뉴 목록 조회 요청")
	@Test
	void list() throws Exception {
		ProductResponse product = new ProductResponse(1L, "후라이드",17000);
		MenuProductResponse menuProduct = new MenuProductResponse(1L, 1L, product, 2);
		MenuGroupResponse menuGroup = new MenuGroupResponse(1L, "추천메뉴");
		MenuResponse savedMenu = new MenuResponse(1L, "후라이드+후라이드", 19000, menuGroup, Collections.singletonList(menuProduct));

		List<MenuResponse> menus = Collections.singletonList(savedMenu);

		when(menuService.list()).thenReturn(menus);

		mockMvc.perform(get("/api/menus"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(menus)));
	}
}
