package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.dto.MenuGroupRequest;

@DisplayName("MenuGroupRestController 테스트")
class MenuGroupRestControllerTest extends BaseControllerTest {

	@DisplayName("MenuGroup 생성 요청")
	@Test
	void create() throws Exception {
		long expectedId = 5L;
		String name = "경양식";
		MenuGroupRequest menuGroup = TestDataUtil.createMenuGroup(name);

		mockMvc.perform(post("/api/menu-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menuGroup)))
			.andDo(print())
			.andExpect(header().string("Location", "/api/menu-groups/" + expectedId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(expectedId))
			.andExpect(jsonPath("$.name").value(name));
	}

	@DisplayName("MenuGroup 목록 조회")
	@Test
	void list() throws Exception {
		mockMvc.perform(get("/api/menu-groups"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(4)));
	}
}