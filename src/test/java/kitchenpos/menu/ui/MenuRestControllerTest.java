package kitchenpos.menu.ui;

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
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.WebMvcTestConfiguration;

@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends WebMvcTestConfiguration {
	@MockBean
	private MenuService menuService;

	@Test
	void createTest() throws Exception {
		// Given
		when(menuService.create(any())).thenReturn(new MenuResponse());
		// When, Then
		mockMvc.perform(post("/api/menus")
			.content(objectMapper.writeValueAsString(new MenuRequest())).contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void listTest() throws Exception {
		// Given
		when(menuService.list()).thenReturn(Arrays.asList(new MenuResponse(1L, "1번 메뉴", new Price(new BigDecimal(10000)), new MenuGroup(), Arrays.asList(new MenuProduct())), new MenuResponse(2L, "2번 메뉴", new Price(new BigDecimal(20000)), new MenuGroup(), Arrays.asList(new MenuProduct()))));
		// When, Then
		mockMvc.perform(get("/api/menus"))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
