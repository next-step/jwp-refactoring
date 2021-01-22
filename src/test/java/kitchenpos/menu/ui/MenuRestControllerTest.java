package kitchenpos.menu.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
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
		MenuRequest menuRequest = new MenuRequest("후라이드천원메뉴", new BigDecimal(1000L), 1L,
				Arrays.asList(getProduct(1L)));

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/menus", menuRequest))
				.andExpect(status().isCreated())
				.andReturn();

		MenuResponse created = toObject(mvcResult, MenuResponse.class);

		assertThat(created.getId()).isNotNull();
		assertThat(created.getPrice().longValue()).isEqualTo(1000L);
		assertThat(created.getName()).isEqualTo("후라이드천원메뉴");
	}

	private MenuProductRequest getProduct(long productId) {
		return new MenuProductRequest(productId, 1L);
	}

	@DisplayName("메뉴를 조회한다.")
	@Test
	void list() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/menus")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<MenuResponse> menus = toList(mvcResult, MenuResponse.class);
		assertThat(menus).isNotEmpty();
	}
}
