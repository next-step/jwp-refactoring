package kitchenpos.menu.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

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
    private static final Long PRODUCT_ID_1 = 1L;
    private static final Long PRODUCT_ID_2 = 2L;
    private static final Long PRODUCT_ID_3 = 3L;

    @MockBean
    private MenuService menuService;

    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 스테이크;
    private Product 에이드;
    private HashMap<Long, Long> quantityOfProducts;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(50000), 양식);

        스테이크 = new Product("스테이크", new BigDecimal(25000));
        스파게티 = new Product("스파게티", new BigDecimal(18000));
        에이드 = new Product("에이드", new BigDecimal(3500));

        양식_세트.addMenuProduct(new MenuProduct(양식_세트, 스테이크, 1L));
        양식_세트.addMenuProduct(new MenuProduct(양식_세트, 스파게티, 1L));
        양식_세트.addMenuProduct(new MenuProduct(양식_세트, 에이드, 2L));

        quantityOfProducts = new HashMap<>();
        quantityOfProducts.put(PRODUCT_ID_1, 1L);
        quantityOfProducts.put(PRODUCT_ID_2, 1L);
        quantityOfProducts.put(PRODUCT_ID_3, 2L);

        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트, "id", 1L);
        ReflectionTestUtils.setField(스테이크, "id", PRODUCT_ID_1);
        ReflectionTestUtils.setField(스파게티, "id", PRODUCT_ID_2);
        ReflectionTestUtils.setField(에이드, "id", PRODUCT_ID_3);
    }

    @Test
    void 메뉴_등록() throws Exception {
        MenuRequest request = new MenuRequest("양식 세트", new BigDecimal(50000), 양식.getId(), quantityOfProducts);
        given(menuService.create(any(MenuRequest.class))).willReturn(MenuResponse.of(양식_세트));

        webMvc.perform(post("/api/menus")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("양식 세트")))
                .andExpect(jsonPath("$.price", is(50000)))
                .andExpect(jsonPath("$.menuGroupId", is(양식.getId().intValue())))
                .andExpect(jsonPath("$.menuProducts", hasSize(3)));
    }

    @Test
    void 메뉴_등록_실패() throws Exception {
        MenuRequest request = new MenuRequest("양식 세트", new BigDecimal(55000), 양식.getId(), quantityOfProducts);
        given(menuService.create(any(MenuRequest.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/menus")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 메뉴_목록_조회() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(MenuResponse.of(양식_세트)));

        webMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
