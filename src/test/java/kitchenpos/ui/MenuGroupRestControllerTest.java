package kitchenpos.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.domain.MenuGroup;
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
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName("aa");

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/menu-groups", menuGroup))
				.andExpect(status().isCreated())
				.andReturn();

		MenuGroup created = toObject(mvcResult.getResponse().getContentAsString(), MenuGroup.class);
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

		List<MenuGroup> menuGroups = toList(mvcResult, MenuGroup.class);
		assertThat(menuGroups).isNotEmpty();
	}
}
