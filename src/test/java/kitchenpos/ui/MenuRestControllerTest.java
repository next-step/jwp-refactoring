package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.menu.dto.MenuRequest;

@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends WebMvcTestConfiguration {
	@MockBean
	private MenuService menuService;

	@Test
	void createTest() throws Exception {
		// Given
		when(menuService.create(any())).thenReturn(new MenuRequest());
		// When, Then
		mockMvc.perform(post("/api/menus")
			.content(objectMapper.writeValueAsString(new MenuRequest())).contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void listTest() throws Exception {
		// Given
		when(menuService.list()).thenReturn(Arrays.asList(new MenuRequest("1번 메뉴", new BigDecimal(10000), 1L), new MenuRequest("2번 메뉴", new BigDecimal(20000), 2L)));
		// When, Then
		mockMvc.perform(get("/api/menus"))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
