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

import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹 등록 요청")
	@Test
	void create() throws Exception {
		MenuGroupResponse menuGroup = new MenuGroupResponse(1L, "추천메뉴");

		when(menuGroupService.create(any(MenuGroupRequest.class))).thenReturn(menuGroup);

		mockMvc.perform(
			post("/api/menu-groups")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\n"
					+ "  \"name\": \"추천메뉴\"\n"
					+ "}")
		)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/menu-groups/1"));
	}

	@DisplayName("메뉴 그룹 목록 조회 요청")
	@Test
	void list() throws Exception {
		MenuGroupResponse menuGroup = new MenuGroupResponse(1L, "추천메뉴");

		List<MenuGroupResponse> menuGroups = Collections.singletonList(menuGroup);

		when(menuGroupService.list()).thenReturn(menuGroups);

		mockMvc.perform(get("/api/menu-groups"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(menuGroups)));
	}
}
