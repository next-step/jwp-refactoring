package api.menu.ui;

import api.menu.MockMvcTest;
import api.menu.dto.MenuGroupRequest;
import api.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuGroupRestControllerTest extends MockMvcTest {

	@DisplayName("메뉴 그룹을 생성할 수 있다.")
	@Test
	void create() throws Exception {
		MenuGroupRequest request = new MenuGroupRequest("aa");

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/menu-groups", request))
				.andExpect(status().isCreated())
				.andReturn();

		MenuGroupResponse created = toObject(mvcResult.getResponse().getContentAsString(), MenuGroupResponse.class);
		assertThat(created.getId()).isNotNull();
		assertThat(created.getName()).isEqualTo("aa");
	}

	@DisplayName("메뉴 그룹을 조회할 수 있다.")
	@Test
	void list() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/menu-groups")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<MenuGroupResponse> menuGroups = toList(mvcResult, MenuGroupResponse.class);
		assertThat(menuGroups).isNotEmpty();
	}
}
