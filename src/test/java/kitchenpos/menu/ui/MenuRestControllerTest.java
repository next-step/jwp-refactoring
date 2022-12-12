package kitchenpos.menu.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.MenuRestController;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MenuRestController ui 테스트")
@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends ControllerTest {
    @MockBean
    private MenuService menuService;

    private Product 후라이드;
    private MenuGroup 한마리치킨;
    private Menu 후라이드치킨;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        후라이드 = new Product(1L, "후라이드", new BigDecimal(18_000));
        한마리치킨 = new MenuGroup(1L, "한마리치킨");
        후라이드치킨 = new Menu(1L, "후라이드치킨", new BigDecimal(18_000), 1L, Lists.newArrayList(new MenuProduct(1L, 1)));
    }

    @DisplayName("메뉴 생성 api 테스트")
    @Test
    void createMenu() throws Exception {
        given(menuService.create(any(Menu.class))).willReturn(후라이드치킨);

        mockMvc.perform(post("/api/menus")
                        .content(mapper.writeValueAsString(후라이드치킨))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 조회 api 테스트")
    @Test
    void listMenus() throws Exception {
        given(menuService.list()).willReturn(Lists.newArrayList(후라이드치킨));

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk());
    }
}
