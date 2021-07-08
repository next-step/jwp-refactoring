package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.api.MenuRestController;
import kitchenpos.ui.dto.menu.MenuProductResponse;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest {
    private static final String URI = "/api/menus";

    @Autowired
    private MenuRestController menuRestController;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Menu menu1;
    private MenuRequest menuRequest1;
    private MenuResponse menuResponse1;
    private MenuProductResponse menuProductResponse1;
    private MenuProductResponse menuProductResponse2;
    private Menu menu2;
    private MenuResponse menuResponse2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        MenuProduct menuProduct1 = MenuProduct.of(1L, 1L, 1L, 1);
        MenuProduct menuProduct2 = MenuProduct.of(2L, 1L, 2L, 1);
        menu1 = Menu.of(1L, "후라이드반+양념반", BigDecimal.valueOf(21000), 1L, Lists.list(menuProduct1, menuProduct2));
        menuRequest1 = MenuRequest.of(menu1);
        menuResponse1 = MenuResponse.of(menu1);
        menuProductResponse1 = MenuProductResponse.of(menuProduct1);
        menuProductResponse2 = MenuProductResponse.of(menuProduct2);

        MenuProduct menuProduct3 = MenuProduct.of(3L, 2L, 3L, 1);
        menu2 = Menu.of(2L, "1인세트", BigDecimal.valueOf(15000), 1L, Lists.list(menuProduct3));
        menuResponse2 = MenuResponse.of(menu2);
    }

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() throws Exception {
        //given
        given(menuService.create(any())).willReturn(menu1);

        //when
        ResultActions actions = mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuRequest1)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", URI + "/" + menuResponse1.getId()))
                .andExpect(jsonPath("$.name").value(menuResponse1.getName()))
                .andExpect(jsonPath("$.price").value(menuResponse1.getPrice()))
                .andExpect(jsonPath("$.menuGroupId").value(menuResponse1.getMenuGroupId()))
                .andExpect(jsonPath("$.menuProducts[0].seq").value(menuProductResponse1.getSeq()))
                .andExpect(jsonPath("$.menuProducts[0].menuId").value(menuProductResponse1.getMenuId()))
                .andExpect(jsonPath("$.menuProducts[0].productId").value(menuProductResponse1.getProductId()))
                .andExpect(jsonPath("$.menuProducts[0].quantity").value(menuProductResponse1.getQuantity()))
                .andExpect(jsonPath("$.menuProducts[1].seq").value(menuProductResponse2.getSeq()))
                .andExpect(jsonPath("$.menuProducts[1].menuId").value(menuProductResponse2.getMenuId()))
                .andExpect(jsonPath("$.menuProducts[1].productId").value(menuProductResponse2.getProductId()))
                .andExpect(jsonPath("$.menuProducts[1].quantity").value(menuProductResponse2.getQuantity()))
        ;
    }

    @DisplayName("메뉴를 모두 조회한다.")
    @Test
    void list() throws Exception {
        //given
        given(menuService.list()).willReturn(Arrays.asList(menu1, menu2));

        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(menuResponse1.getId()))
                .andExpect(jsonPath("$[0].name").value(menuResponse1.getName()))
                .andExpect(jsonPath("$[0].price").value(menuResponse1.getPrice()))
                .andExpect(jsonPath("$[0].menuGroupId").value(menuResponse1.getMenuGroupId()))
                .andExpect(jsonPath("$[0].menuProducts[0]").isNotEmpty())
                .andExpect(jsonPath("$[0].menuProducts[1]").isNotEmpty())
                .andExpect(jsonPath("$[1].id").value(menuResponse2.getId()))
                .andExpect(jsonPath("$[1].name").value(menuResponse2.getName()))
                .andExpect(jsonPath("$[1].price").value(menuResponse2.getPrice()))
                .andExpect(jsonPath("$[1].menuGroupId").value(menuResponse2.getMenuGroupId()))
                .andExpect(jsonPath("$[1].menuProducts[0]").isNotEmpty())
        ;
    }
}
