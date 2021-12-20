package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

class MenuGroupRestControllerTest extends IntegrationTest {

	private static final String BASE_PATH = "/api/menu-groups";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MenuGroupService service;

	@DisplayName("메뉴 그룹 등록")
	@Test
	void create() throws Exception {
		//given
		Map<String, String> menuGroup = 메뉴그룹_정보_등록("추천메뉴");
		MenuGroup expectedMenuGroup = new MenuGroup(1L, menuGroup.get("name"));
		given(service.create(any(MenuGroup.class)))
			.willReturn(expectedMenuGroup);

		//when
		MockHttpServletResponse response = mockMvc.perform(post(BASE_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menuGroup))
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		MenuGroup savedMenuGroup = objectMapper.readValue(response.getContentAsString(), MenuGroup.class);
		assertThat(savedMenuGroup).isEqualTo(expectedMenuGroup);
	}

	@DisplayName("메뉴 그룹 조회")
	@Test
	void list() throws Exception {
		//given
		List<MenuGroup> expectedMenuGroups = Arrays.asList(
			new MenuGroup(1L, "추천메뉴"),
			new MenuGroup(2L, "베스트메뉴"));
		given(service.list())
			.willReturn(expectedMenuGroups);

		//when
		MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH)
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		List<MenuGroup> findMenuGroups = objectMapper.readValue(response.getContentAsString(),
			new TypeReference<List<MenuGroup>>() {
			});
		assertThat(findMenuGroups).containsAll(expectedMenuGroups);
	}

	private Map<String, String> 메뉴그룹_정보_등록(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return params;
	}
}
