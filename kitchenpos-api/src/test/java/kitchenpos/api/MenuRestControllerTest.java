package kitchenpos.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.math.BigDecimal;
import java.util.List;

@DisplayName("메뉴 컨트롤러 테스트")
@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest {
	private static final String BASE_URL = "/api/menus";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private MenuRestController menuRestController;

	@MockBean
	private MenuService menuService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(MockMvcResultHandlers.print())
				.build();
	}

	@Test
	void createTest() throws Exception {
		MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(10000), null, null);
		MenuResponse menuResponse = new MenuResponse(1L, "치킨", BigDecimal.valueOf(10000), null, null);

		BDDMockito.given(menuService.create(menuRequest)).willReturn(menuResponse);

		mockMvc.perform(
				MockMvcRequestBuilders.post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(menuRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("location", BASE_URL + "/1"));
	}

	@Test
	void listTest() throws Exception {
		List<MenuResponse> menuResponses = Lists.list(new MenuResponse(1L, "치킨", BigDecimal.valueOf(10000), null, null), new MenuResponse());
		BDDMockito.given(menuService.list()).willReturn(menuResponses);

		mockMvc.perform(
				MockMvcRequestBuilders.get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("치킨")));
	}
}
