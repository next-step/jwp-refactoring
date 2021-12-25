package kitchenpos.menu.ui;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.CommonTestFixtures;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    private static final String BASE_PATH = "/api/menus";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    private Product 타코야끼;
    private Product 뿌링클;
    private MenuGroup 추천메뉴그룹;

    @BeforeEach
    void setUp() {
        타코야끼 = new Product(1L, "타코야끼", new Price(BigDecimal.valueOf(12000)));
        뿌링클 = new Product(2L, "뿌링클", new Price(BigDecimal.valueOf(22000)));
        추천메뉴그룹 = new MenuGroup(1L, "추천메뉴");
    }

    @DisplayName("메뉴 등록")
    @Test
    void create() throws Exception {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼, new Quantity(2)),
            new MenuProduct(뿌링클, new Quantity(1)));
        String menuName = "후라이드+후라이드";
        Price price = new Price(BigDecimal.valueOf(19000));
        MenuRequest menuRequest = MenuTestFixtures.convertToMenuRequest(
            new Menu(menuName, price, 추천메뉴그룹, menuProducts));
        MenuResponse expectedMenu = MenuResponse.from(
            new Menu(1L, menuName, price, 추천메뉴그룹, menuProducts));
        given(menuService.create(any())).willReturn(expectedMenu);

        //when, then
        mockMvc.perform(post(BASE_PATH)
                .content(CommonTestFixtures.asJsonString(menuRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedMenu.getId()))
            .andExpect(jsonPath("$.name").value(expectedMenu.getName()));
    }

    @DisplayName("메뉴 조회")
    @Test
    void list() throws Exception {
        //given
        List<MenuProduct> menuProducts1 = Arrays.asList(
            new MenuProduct(타코야끼, new Quantity(2)),
            new MenuProduct(뿌링클, new Quantity(3)));
        List<MenuProduct> menuProducts2 = Arrays.asList(
            new MenuProduct(타코야끼, new Quantity(1)),
            new MenuProduct(뿌링클, new Quantity(2)));

        List<MenuResponse> expectedMenus = MenuResponse.fromList(Arrays.asList(
            new Menu(1L, "후라이드+후라이드", new Price(BigDecimal.valueOf(19000)), 추천메뉴그룹,
                menuProducts1),
            new Menu(2L, "오븐구이+순살강정", new Price(BigDecimal.valueOf(23000)), 추천메뉴그룹,
                menuProducts2)
        ));
        given(menuService.list()).willReturn(expectedMenus);

        //when, then
        mockMvc.perform(get(BASE_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*]['id']",
                containsInAnyOrder(
                    expectedMenus.stream()
                        .mapToInt(menu -> menu.getId().intValue()).boxed()
                        .toArray(Integer[]::new))));
    }
}
