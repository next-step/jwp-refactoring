package kitchenpos.ui;

import static kitchenpos.common.TestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.dto.MenuRequest;

@DisplayName("MenuRestController 테스트")
class MenuRestControllerTest extends BaseControllerTest {

	@DisplayName("Menu 생성 요청")
	@Test
	void create() throws Exception {
		long expectedId = 7L;
		int price = 20000;
		Long menuGroupId = 1L;
		String name = "후라이드 한마리 + 양념 한마리";

		MenuRequest menu = TestDataUtil.createMenu(name, price, menuGroupId, Arrays.asList(메뉴_후라이드_갯수, 메뉴_양념_갯수));

		mockMvc.perform(post("/api/menus")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menu)))
			.andDo(print())
			.andExpect(header().string("Location", "/api/menus/" + expectedId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(expectedId))
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.price").value(price))
			.andExpect(jsonPath("$.menuGroup.id").value(menuGroupId))
			.andExpect(jsonPath("$.menuProducts", Matchers.hasSize(2)));
	}

	@DisplayName("Menu 목록 조회")
	@Test
	void list() throws Exception {
		mockMvc.perform(get("/api/menus"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(6)));
	}
}