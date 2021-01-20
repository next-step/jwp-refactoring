package kitchenpos.ui;

import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.acceptance.Menu.MenuGroupAcceptance;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 생성한다.")
	@Test
	void createMenuGroupTest() throws Exception {
		// given
		MenuGroup menuGroup = MenuGroup.of(1L, "세마리메뉴");
		given(menuGroupService.create(any())).willReturn(menuGroup);

		// when
		final ResultActions resultActions = mvc.perform(post(MenuGroupAcceptance.MENU_GROUP_REQUEST_URL)
			.content(mapper.writeValueAsString(menuGroup))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl(MenuGroupAcceptance.MENU_GROUP_REQUEST_URL + "/" + menuGroup.getId()))
			.andExpect(jsonPath("$.name").value(menuGroup.getName()))
			.andDo(log());
	}

	@DisplayName("메뉴 그룹 목록을 조회한다.")
	@Test
	void selectMenuGroupListTest() throws Exception {
		// given
		given(menuGroupService.list()).willReturn(Arrays.asList(
			MenuGroup.of(1L, "두마리메뉴"),
			MenuGroup.of(2L, "세마리메뉴"),
			MenuGroup.of(3L, "온가족세트")
		));

		// when
		final ResultActions resultActions = mvc.perform(get(MenuGroupAcceptance.MENU_GROUP_REQUEST_URL)
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].name").value("두마리메뉴"))
			.andExpect(jsonPath("$[1].name").value("세마리메뉴"))
			.andExpect(jsonPath("$[2].name").value("온가족세트"))
			.andDo(log());
	}
}
