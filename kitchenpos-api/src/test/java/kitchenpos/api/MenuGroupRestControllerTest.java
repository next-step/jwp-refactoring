package kitchenpos.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
	private static final String BASE_URL = "/api/menu-groups";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MenuGroupRestController menuGroupRestController;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private MenuGroupService menuGroupService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(MockMvcResultHandlers.print())
				.build();
	}

	@Test
	void createTest() throws Exception {
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest("치킨");
		MenuGroupResponse aMenuGroupResponse = new MenuGroupResponse(1L, "치킨");
		BDDMockito.given(menuGroupService.create(menuGroupRequest)).willReturn(aMenuGroupResponse);

		mockMvc.perform(
				MockMvcRequestBuilders.post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(aMenuGroupResponse)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("location", BASE_URL + "/1"));
	}

	@Test
	void listTest() throws Exception {
		List<MenuGroupResponse> menuGroupRespons = Lists.list(new MenuGroupResponse(1L, "치킨"), new MenuGroupResponse(2L, "피자"));
		BDDMockito.given(menuGroupService.list()).willReturn(menuGroupRespons);

		mockMvc.perform(
				MockMvcRequestBuilders.get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("치킨")));
	}
}
