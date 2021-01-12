package kitchenpos.menu.ui;

import static kitchenpos.domain.TestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.menu.dto.MenuGroupRequest;

@DisplayName("메뉴 그룹 Controller 테스트")
public class MenuGroupRestControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("메뉴그룹을 등록할 수 있다 - 메뉴그룹 등록 후, 등록된 메뉴그룹의 아이디를 포함한 정보를 반환한다.")
	void create() throws Exception {
		//given
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest(메뉴_신규_NAME);

		//when-then
		mockMvc.perform(post("/api/menu-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menuGroupRequest)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.name").value(메뉴_신규_NAME));
	}

	@Test
	@DisplayName("메뉴그룹의 목록을 조회할 수 있다.")
	void list() throws Exception {
		//when-then
		mockMvc.perform(get("/api/menu-groups"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isNotEmpty())
			.andExpect(jsonPath("$..id").isNotEmpty())
			.andExpect(jsonPath("$..name").isNotEmpty());
	}
}

