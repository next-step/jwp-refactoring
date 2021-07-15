package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;

@DisplayName("메뉴 기능")
@SpringBootTest
class MenuRestControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private MenuService menuService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MenuRestController menuRestController;

	private Menu menu;

	@BeforeEach
	void setup() {
		// MockMvc
		mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
			.addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
			.alwaysDo(print())
			.build();

		menu = new Menu();
		menu.setId(1L);
		menu.setName("양념치킨");
		menu.setPrice(new BigDecimal(16000));
	}

	@Test
	@DisplayName("메뉴를 생성할 수 있다.")
	public void create() throws Exception {
		// given
		given(menuService.create(any())).willReturn(menu);

		// when
		final ResultActions actions = 메뉴_생성_요청();

		// then
		메뉴_생성에_성공함(actions, menu);
	}

	@Test
	@DisplayName("메뉴 목록을 조회할 수 있다.")
	public void list() throws Exception {
		// given
		given(menuService.list()).willReturn(Arrays.asList(menu));

		// when
		final ResultActions actions = 메뉴_조회_요청();

		// then
		메뉴_조회에_성공함(actions, menu);
	}

	private ResultActions 메뉴_생성_요청() throws Exception {
		return mockMvc.perform(post("/api/menus")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menu)));
	}

	private void 메뉴_생성에_성공함(ResultActions actions, Menu menu) throws Exception {
		actions.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/menus" + "/1"))
			.andExpect(content().string(containsString(menu.getName())))
			.andExpect(content().string(containsString(String.valueOf(menu.getPrice())))
			);
	}

	private ResultActions 메뉴_조회_요청() throws Exception {
		return mockMvc.perform(get("/api/menus")
			.contentType(MediaType.APPLICATION_JSON));
	}

	private void 메뉴_조회에_성공함(ResultActions actions, Menu menu) throws Exception {
		actions.andExpect(status().isOk())
			.andExpect(content().string(containsString(menu.getName())))
			.andExpect(content().string(containsString(String.valueOf(menu.getPrice()))));
	}
}
