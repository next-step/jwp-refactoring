package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.acceptance.menu.MenuAcceptance;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 Controller 테스트")
@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private MenuService menuService;

	private MenuGroup food;
	private Product 치킨;
	private Product 피자;
	private List<MenuProduct> products;

	@BeforeEach
	void setUp() {
		food = MenuGroup.of(1L, "세트");
		치킨 = Product.of(1L, "치킨", 16000);
		피자 = Product.of(2L, "피자", 20000);
		products = Arrays.asList(MenuProduct.of(치킨, 2), MenuProduct.of(피자, 1));
	}

	@DisplayName("메뉴를 등록한다.")
	@Test
	void createMenuTest() throws Exception {
		// given
		Menu menu = Menu.of(1L, "치피세트", 50000, food.getId(), products);
		given(menuService.create(any())).willReturn(menu);

		// when
		final ResultActions resultActions = mvc.perform(post(MenuAcceptance.MENUS_REQUEST_URL)
			.content(mapper.writeValueAsString(menu))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl(MenuAcceptance.MENUS_REQUEST_URL + "/" + menu.getId()))
			.andExpect(jsonPath("$.id").value(menu.getId()))
			.andDo(log());
	}
}
