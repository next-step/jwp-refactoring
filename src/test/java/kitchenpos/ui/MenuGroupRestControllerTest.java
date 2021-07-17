package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.menuGroup.application.MenuGroupService;
import kitchenpos.menuGroup.ui.MenuGroupRestController;
import kitchenpos.menuGroup.dto.MenuGroupRequest;

@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest extends WebMvcTestConfiguration{
	@MockBean
	private MenuGroupService menuGroupService;

	@Test
	void createTest() throws Exception {
		// Given
		when(menuGroupService.create(any())).thenReturn(new MenuGroupRequest());
		// When, Then
		mockMvc.perform(post("/api/menu-groups")
			.content(objectMapper.writeValueAsString(new MenuGroupRequest())).contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void list() throws Exception {
		// Given
		when(menuGroupService.list()).thenReturn(Arrays.asList(new MenuGroupRequest("1번 메뉴 그룹"), new MenuGroupRequest("2번 메뉴 그룹")));
		// When, Then
		mockMvc.perform(get("/api/menu-groups"))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
