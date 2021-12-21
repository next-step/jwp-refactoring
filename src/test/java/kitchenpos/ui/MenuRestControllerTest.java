package kitchenpos.ui;

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
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.testfixture.CommonTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class MenuRestControllerTest extends IntegrationTest {

    private static final String BASE_PATH = "/api/menus";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 등록")
    @Test
    void create() throws Exception {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 2),
            new MenuProduct(2L, 1));
        String menuName = "후라이드+후라이드";
        BigDecimal price = BigDecimal.valueOf(19000);
        Long menuGroupId = 1L;
        Menu requestMenu = new Menu(menuName, price, menuGroupId, menuProducts);
        Menu expectedMenu = new Menu(1L, menuName, price, menuGroupId, menuProducts);
        given(menuService.create(any())).willReturn(expectedMenu);

        //when, then
        mockMvc.perform(post(BASE_PATH)
                .content(CommonTestFixtures.asJsonString(requestMenu))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedMenu.getId()))
            .andExpect(jsonPath("$.name").value(expectedMenu.getName()));
    }

    @DisplayName("메뉴 조회")
    @Test
    void list() throws Exception {
        //given
        List<MenuProduct> menuProducts1 = Arrays.asList(new MenuProduct(1L, 1L, 1L, 2),
            new MenuProduct(2L, 1L, 2L, 3));
        List<MenuProduct> menuProducts2 = Arrays.asList(new MenuProduct(3L, 2L, 1L, 1),
            new MenuProduct(4L, 2L, 2L, 2));
        List<Menu> expectedMenus = Arrays.asList(
            new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), 1L, menuProducts1),
            new Menu(2L, "오븐구이+순살강정", BigDecimal.valueOf(23000), 2L, menuProducts2));
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
