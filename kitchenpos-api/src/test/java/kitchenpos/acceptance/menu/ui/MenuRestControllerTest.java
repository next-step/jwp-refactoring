package kitchenpos.acceptance.menu.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

import kitchenpos.acceptance.menu.MenuAcceptance;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.product.domain.Price;

@DisplayName("메뉴 Controller 테스트")
@WebMvcTest(MenuRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MenuRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private MenuService menuService;

	private List<MenuProductRequest> menuProductRequests;
	private List<MenuProductResponse> menuProductResponses;

	@BeforeEach
	void setUp() {
		menuProductRequests = Arrays.asList(
			MenuProductRequest.of(1L, 2),
			MenuProductRequest.of(2L, 1));

		menuProductResponses = Arrays.asList(
			MenuProductResponse.of(1L, 1L, 2),
			MenuProductResponse.of(2L, 2L, 1)
		);
	}

	@DisplayName("메뉴를 등록한다.")
	@Test
	void createMenuTest() throws Exception {
		// given
		MenuRequest request = MenuRequest.of("치피세트", 50000, 1L, menuProductRequests);
		MenuResponse response = MenuResponse.of(1L, "치피세트", Price.of(50000), 1L, menuProductResponses);
		given(menuService.create(any())).willReturn(response);

		// when
		final ResultActions resultActions = mvc.perform(post(MenuAcceptance.MENUS_REQUEST_URL)
			.content(mapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl(MenuAcceptance.MENUS_REQUEST_URL + "/" + response.getId()))
			.andExpect(jsonPath("$.id").value(response.getId()))
			.andDo(log());
	}
}
