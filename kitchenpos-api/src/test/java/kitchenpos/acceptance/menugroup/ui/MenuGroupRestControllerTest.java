package kitchenpos.acceptance.menugroup.ui;

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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.acceptance.menugroup.MenuGroupAcceptance;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.ui.MenuGroupRestController;

@DisplayName("메뉴 그룹 Controller 테스트")
@WebMvcTest(MenuGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
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
		MenuGroupRequest request = MenuGroupRequest.of("세마리메뉴");
		MenuGroupResponse response = MenuGroupResponse.of(1L, "세마리메뉴");
		given(menuGroupService.create(any())).willReturn(response);

		// when
		final ResultActions resultActions = mvc.perform(post(MenuGroupAcceptance.MENU_GROUP_REQUEST_URL)
			.content(mapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl(MenuGroupAcceptance.MENU_GROUP_REQUEST_URL + "/" + response.getId()))
			.andExpect(jsonPath("$.name").value(request.getName()))
			.andDo(log());
	}

	@DisplayName("메뉴 그룹 목록을 조회한다.")
	@Test
	void selectMenuGroupListTest() throws Exception {
		// given
		given(menuGroupService.list()).willReturn(Arrays.asList(
			MenuGroupResponse.of(1L, "두마리메뉴"),
			MenuGroupResponse.of(2L, "세마리메뉴"),
			MenuGroupResponse.of(3L, "온가족세트")
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
