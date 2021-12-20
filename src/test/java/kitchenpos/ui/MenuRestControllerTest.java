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

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

class MenuRestControllerTest extends IntegrationTest {

	private static final String BASE_PATH = "/api/menus";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MenuService menuService;

	@DisplayName("메뉴 등록")
	@Test
	void create() throws Exception {
		//given
		List<MenuProduct> menuProducts = Arrays.asList(
			new MenuProduct(1L, 2),
			new MenuProduct(2L, 1)
		);
		String menuName = "후라이드+후라이드";
		BigDecimal price = BigDecimal.valueOf(19000);
		Long menuGroupId = 1L;
		Map<String, Object> menu = 메뉴_정보(menuName, price, menuGroupId, menuProducts);
		Menu expectedMenu = new Menu(1L, menuName, price, menuGroupId, menuProducts);
		given(menuService.create(any()))
			.willReturn(expectedMenu);

		//when
		MockHttpServletResponse response = mockMvc.perform(post(BASE_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menu))
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

	@DisplayName("메뉴 조회")
	@Test
	void list() throws Exception {
		//given
		List<MenuProduct> menuProducts1 = Arrays.asList(
			new MenuProduct(1L, 1L, 1L, 2),
			new MenuProduct(2L, 1L, 2L, 3)
		);

		List<MenuProduct> menuProducts2 = Arrays.asList(
			new MenuProduct(3L, 2L, 1L, 1),
			new MenuProduct(4L, 2L, 2L, 2)
		);

		List<Menu> expectedMenus = Arrays.asList(
			new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), 1L, menuProducts1),
			new Menu(2L, "오븐구이+순살강정", BigDecimal.valueOf(23000), 2L, menuProducts2)
		);
		given(menuService.list())
			.willReturn(expectedMenus);

		//when
		MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH)
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		List<Menu> findMenus = objectMapper.readValue(response.getContentAsString(),
			new TypeReference<List<Menu>>() {
			});
		assertThat(findMenus).containsAll(expectedMenus);
	}

	private Map<String, Object> 메뉴_정보(String name, BigDecimal price, Long menuGroupId,
		List<MenuProduct> menuProducts) {

		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("price", price.toString());
		params.put("menuGroupId", String.valueOf(menuGroupId));
		params.put("menuProducts", menuProducts);
		return params;
	}
}
