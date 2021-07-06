package kitchenpos.ui.menu;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.ui.MenuRestController;

@DisplayName("메뉴 컨트롤러 테스트")
@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MenuService menuService;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();
	}

	@DisplayName("create")
	@Test
	public void create() throws Exception {
		Menu menu = new Menu(1L, "맛있는 치킨", BigDecimal.valueOf(1_000), 1L, null);
		given(menuService.create(any())).willReturn(menu);

		mockMvc.perform(post("/api/menus")
			.content(objectMapper.writeValueAsString(menu))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/menus/" + menu.getId()))
		;
	}

	@DisplayName("list")
	@Test
	public void list() throws Exception {
		Menu menu = new Menu(1L, "맛있는 치킨", BigDecimal.valueOf(1_000), 1L, null);
		given(menuService.list()).willReturn(Arrays.asList(menu));

		mockMvc.perform(get("/api/menus"))
			.andExpect(status().isOk())
		;
	}
}
