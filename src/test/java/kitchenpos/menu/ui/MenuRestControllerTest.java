package kitchenpos.menu.ui;

import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.common.ui.RestControllerTest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends RestControllerTest {

    private static final String API_MENU_ROOT = "/api/menus";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuService menuService;

    MenuGroup 두마리치킨;
    Product 후라이드치킨;
    MenuRequest 후라이드_후라이드_요청;
    MenuResponse 후라이드_후라이드_응답;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        두마리치킨 = MenuGroupFixture.from("두마리치킨");

        후라이드_후라이드_요청 = MenuRequest.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(16000),
                두마리치킨.getId(),
                Collections.singletonList(MenuProductRequest.of(후라이드치킨.getId(), 2)));

        MenuProduct 후라이드_후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨.getId(), 2);
        Menu 후라이드_후라이드 = Menu.of(
                후라이드_후라이드_요청.getName(),
                후라이드_후라이드_요청.getPrice(),
                후라이드_후라이드_요청.getMenuGroupId());
        후라이드_후라이드.addMenuProduct(후라이드_후라이드_메뉴_상품);
        후라이드_후라이드_응답 = MenuResponse.from(후라이드_후라이드);
    }

    @Test
    void 메뉴_생성() throws Exception {
        //given
        given(menuService.create(any())).willReturn(후라이드_후라이드_응답);

        //when
        ResultActions actions = mockMvc.perform(post(API_MENU_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(후라이드_후라이드_요청)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(후라이드_후라이드_응답.getId()))
                .andExpect(jsonPath("$.name").value(후라이드_후라이드_응답.getName()))
                .andExpect(jsonPath("$.price").value(후라이드_후라이드_응답.getPrice()))
                .andExpect(jsonPath("$.menuProducts[0].seq").value(후라이드_후라이드_응답.getMenuProducts().get(0).getSeq()))
                .andExpect(jsonPath("$.menuProducts[0].menuId").value(후라이드_후라이드_응답.getMenuProducts().get(0).getMenuId()))
                .andExpect(jsonPath("$.menuProducts[0].productId").value(후라이드_후라이드_응답.getMenuProducts().get(0).getProductId()))
                .andExpect(jsonPath("$.menuProducts[0].quantity").value(후라이드_후라이드_응답.getMenuProducts().get(0).getQuantity()));
    }

    @Test
    void 메뉴_조회() throws Exception {
        //given
        List<MenuResponse> menus = new ArrayList<>();
        menus.add(후라이드_후라이드_응답);

        given(menuService.list()).willReturn(menus);

        //when
        ResultActions actions = mockMvc.perform(get(API_MENU_ROOT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(menus.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(menus.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(menus.get(0).getPrice()))
                .andExpect(jsonPath("$[0].menuProducts[0].seq").value(후라이드_후라이드_응답.getMenuProducts().get(0).getSeq()))
                .andExpect(jsonPath("$[0].menuProducts[0].menuId").value(후라이드_후라이드_응답.getMenuProducts().get(0).getMenuId()))
                .andExpect(jsonPath("$[0].menuProducts[0].productId").value(후라이드_후라이드_응답.getMenuProducts().get(0).getProductId()))
                .andExpect(jsonPath("$[0].menuProducts[0].quantity").value(후라이드_후라이드_응답.getMenuProducts().get(0).getQuantity()));
    }

}
