package kitchenpos.menu.ui;

import static kitchenpos.utils.TestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

@DisplayName("메뉴 Controller 테스트")
public class MenuRestControllerTest extends BaseControllerTest {

	private MenuProductRequest menuProductRequest1;
	private MenuProductRequest menuProductRequest2;
	private List<MenuProductRequest> menuProductRequests;

	@BeforeEach
	public void setUp() {
		menuProductRequest1 = new MenuProductRequest(메뉴상품_신규_1_후라이드_ID, 메뉴상품_신규_1_후라이드_QUANTITY);
		menuProductRequest2 = new MenuProductRequest(메뉴상품_신규_2_양념_ID, 메뉴상품_신규_2_양념_QUANTITY);
		menuProductRequests = Arrays.asList(menuProductRequest1, menuProductRequest2);
	}

	@Test
	@DisplayName("메뉴를 등록할 수 있다 - 메뉴 등록 후, 등록된 메뉴의 아이디를 포함한 정보를 반환한다.")
	void create() throws Exception {
		//given
		MenuRequest menuRequest = new MenuRequest(메뉴_신규_NAME, 메뉴_신규_PRICE, 메뉴_신규_MENU_GROUP_ID, menuProductRequests);

		//when-then
		mockMvc.perform(post("/api/menus")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menuRequest)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.name").value(메뉴_신규_NAME))
			.andExpect(jsonPath("$.price").value(메뉴_신규_PRICE.longValue()));
	}

	@Test
	@DisplayName("메뉴의 목록을 조회할 수 있다.")
	void list() throws Exception {
		//when-then
		mockMvc.perform(get("/api/menus"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isNotEmpty())
			.andExpect(jsonPath("$..id").isNotEmpty())
			.andExpect(jsonPath("$..name").isNotEmpty())
			.andExpect(jsonPath("$..menuGroupId").isNotEmpty())
			.andExpect(jsonPath("$..menuProducts").isNotEmpty())
			.andExpect(jsonPath("$..price").isNotEmpty());
	}
}

