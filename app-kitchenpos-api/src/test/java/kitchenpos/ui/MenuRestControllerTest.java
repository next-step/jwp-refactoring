package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_후라이드_메뉴_상품)));
        후라이드_후라이드_응답 = MenuResponse.from(후라이드_후라이드);
    }

    @Test
    void 메뉴_생성() throws Exception {
        //given
        BDDMockito.given(menuService.create(ArgumentMatchers.any())).willReturn(후라이드_후라이드_응답);

        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(API_MENU_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(후라이드_후라이드_요청)))
                .andDo(MockMvcResultHandlers.print());

        //then
        actions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(후라이드_후라이드_응답.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(후라이드_후라이드_응답.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(후라이드_후라이드_응답.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.menuProducts[0].seq").value(후라이드_후라이드_응답.getMenuProducts().get(0).getSeq()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.menuProducts[0].menuId").value(후라이드_후라이드_응답.getMenuProducts().get(0).getMenuId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.menuProducts[0].productId").value(후라이드_후라이드_응답.getMenuProducts().get(0).getProductId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.menuProducts[0].quantity").value(후라이드_후라이드_응답.getMenuProducts().get(0).getQuantity()));
    }

    @Test
    void 메뉴_조회() throws Exception {
        //given
        List<MenuResponse> menus = new ArrayList<>();
        menus.add(후라이드_후라이드_응답);

        BDDMockito.given(menuService.list()).willReturn(menus);

        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(API_MENU_ROOT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        //then
        actions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(menus.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(menus.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(menus.get(0).getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].menuProducts[0].seq").value(후라이드_후라이드_응답.getMenuProducts().get(0).getSeq()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].menuProducts[0].menuId").value(후라이드_후라이드_응답.getMenuProducts().get(0).getMenuId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].menuProducts[0].productId").value(후라이드_후라이드_응답.getMenuProducts().get(0).getProductId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].menuProducts[0].quantity").value(후라이드_후라이드_응답.getMenuProducts().get(0).getQuantity()));
    }

}
