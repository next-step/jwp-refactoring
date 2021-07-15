package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 기능")
@SpringBootTest
class MenuGroupRestControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private MenuGroupService menuGroupService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MenuGroupRestController menuGroupRestController;

	private MenuGroup menuGroup;

	@BeforeEach
	void setup() {
		// MockMvc
		mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
			.addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
			.alwaysDo(print())
			.build();
		
		menuGroup = new MenuGroup();
		menuGroup.setId(1L);
		menuGroup.setName("추천 메뉴");
	}

	@Test
	@DisplayName("메뉴 그룹을 생성할 수 있다.")
	public void create() throws Exception {
		// given
		given(menuGroupService.create(any())).willReturn(menuGroup);

		// when
		final ResultActions actions = 메뉴_그룹_생성_요청();

		// then
		메뉴_그룹_생성에_성공함(actions, menuGroup.getName());
	}

	@Test
	@DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
	public void list() throws Exception {
		// given
		given(menuGroupService.list()).willReturn(Arrays.asList(menuGroup));

		// when
		final ResultActions actions = 메뉴_그룹_조회_요청();

		// then
		메뉴_그룹_조회에_성공함(actions, menuGroup.getName());
	}

	private ResultActions 메뉴_그룹_생성_요청() throws Exception {
		return mockMvc.perform(post("/api/menu-groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menuGroup)));
	}

	private void 메뉴_그룹_생성에_성공함(ResultActions actions, String menuName) throws Exception {
		actions.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/menu-groups" + "/1"))
			.andExpect(content().string(containsString(menuName)));
	}

	private ResultActions 메뉴_그룹_조회_요청() throws Exception {
		return mockMvc.perform(get("/api/menu-groups")
			.contentType(MediaType.APPLICATION_JSON));
	}

	private void 메뉴_그룹_조회에_성공함(ResultActions actions, String menuName) throws Exception {
		actions.andExpect(status().isOk())
			.andExpect(content().string(containsString(menuName)));
	}

}
