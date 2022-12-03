package kitchenpos.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MenuRestController 테스트")
@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends ControllerTest {

    @MockBean
    private MenuService menuService;

    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 스테이크;
    private Product 에이드;
    private MenuProduct 양식_스파게티;
    private MenuProduct 양식_스테이크;
    private MenuProduct 양식_에이드;
    private List<MenuProduct> 메뉴_상품;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = new MenuGroup(1L, "양식");
        양식_세트 = new Menu(1L, "양식 세트", new BigDecimal(50000), 양식.getId());

        스파게티 = new Product(1L, "스파게티", new BigDecimal(18000));
        스테이크 = new Product(2L, "스테이크", new BigDecimal(25000));
        에이드 = new Product(3L, "에이드", new BigDecimal(3500));

        양식_스파게티 = new MenuProduct(1L, 양식_세트.getId(), 스파게티.getId(), 1);
        양식_스테이크 = new MenuProduct(2L, 양식_세트.getId(), 스테이크.getId(), 1);
        양식_에이드 = new MenuProduct(3L, 양식_세트.getId(), 에이드.getId(), 2);

        메뉴_상품 = Arrays.asList(양식_스파게티, 양식_스테이크, 양식_에이드);
        양식_세트.setMenuProducts(메뉴_상품);
    }

    @Test
    void 메뉴_등록() throws Exception {
        given(menuService.create(any(Menu.class))).willReturn(양식_세트);

        webMvc.perform(post("/api/menus")
                    .content(mapper.writeValueAsString(양식_세트))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(양식_세트.getId().intValue())))
                .andExpect(jsonPath("$.name", is(양식_세트.getName())))
                .andExpect(jsonPath("$.price", is(양식_세트.getPrice().intValue())))
                .andExpect(jsonPath("$.menuGroupId", is(양식_세트.getMenuGroupId().intValue())))
                .andExpect(jsonPath("$.menuProducts", hasSize(3)));
    }

    @Test
    void 메뉴_등록_실패() throws Exception {
        given(menuService.create(any(Menu.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/menus")
                    .content(mapper.writeValueAsString(양식_세트))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 메뉴_목록_조회() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(양식_세트));

        webMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
