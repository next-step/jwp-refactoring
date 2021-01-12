package kitchenpos.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuRestControllerTest extends MockMvcTest {

	@DisplayName("메뉴를 생성하다.")
	@Test
	void create() throws Exception {
		Menu menu = new Menu();
		menu.setMenuProducts(Arrays.asList(getProduct(1L)));
		menu.setPrice(new BigDecimal(1000L));
		menu.setName("후라이드천원메뉴");
		menu.setMenuGroupId(1L);

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/menus", menu))
				.andExpect(status().isCreated())
				.andReturn();

		Menu created = toObject(mvcResult, Menu.class);

		assertThat(created.getId()).isNotNull();
		assertThat(created.getPrice().longValue()).isEqualTo(1000L);
		assertThat(created.getName()).isEqualTo("후라이드천원메뉴");
	}

	private MenuProduct getProduct(long productId) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(productId);
		menuProduct.setQuantity(1L);
		return menuProduct;
	}

	@DisplayName("메뉴를 조회한다.")
	@Test
	void list() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/menus")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<Menu> menus = toList(mvcResult, Menu.class);
		assertThat(menus).isNotEmpty();
	}
}
