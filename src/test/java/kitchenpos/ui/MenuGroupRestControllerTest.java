package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 컨트롤러 테스트")
@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MenuGroupService menuGroupService;

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
		MenuGroup menuGroup = new MenuGroup(1L, "치킨세트");
		given(menuGroupService.create(any())).willReturn(menuGroup);

		mockMvc.perform(post("/api/menu-groups")
			.content(objectMapper.writeValueAsString(menuGroup))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/menu-groups/1"))
		;
	}

	@DisplayName("list")
	@Test
	public void list() throws Exception {
		MenuGroup menuGroup = new MenuGroup(1L, "치킨세트");
		given(menuGroupService.list()).willReturn(Arrays.asList(menuGroup));

		mockMvc.perform(get("/api/menu-groups"))
			.andExpect(status().isOk())
		;
	}
}
