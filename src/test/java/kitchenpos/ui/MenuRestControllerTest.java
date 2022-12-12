package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
public class MenuRestControllerTest {
    @Autowired
    protected MockMvc webMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuService menuService;

    private Product 후라이드치킨_상품;
    private Product 콜라_상품;

    private MenuGroup 한마리메뉴_메뉴그룹;

    private Menu 후라이드치킨_메뉴;
    private Menu 콜라_메뉴;

    private MenuProduct 후라이드치킨_메뉴상품;
    private MenuProduct 콜라_메뉴상품;

    private List<MenuProduct> 메뉴_상품;

    @BeforeEach
    void setUp() {
        한마리메뉴_메뉴그룹 = MenuGroup.of(1L, "한마리메뉴");

        후라이드치킨_상품 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(16_000L));
        콜라_상품 = Product.of(2L, "콜라", BigDecimal.valueOf(2_000L));

        후라이드치킨_메뉴 = Menu.of(1L, "후라이드치킨", 후라이드치킨_상품.getPrice(), 한마리메뉴_메뉴그룹.getId());
        콜라_메뉴 = Menu.of(2L, "콜라", 콜라_상품.getPrice(), 한마리메뉴_메뉴그룹.getId());

        후라이드치킨_메뉴상품 = MenuProduct.of(1L, 후라이드치킨_메뉴.getId(), 후라이드치킨_상품.getId(), 1L);
        콜라_메뉴상품 = MenuProduct.of(2L, 콜라_메뉴.getId(), 콜라_상품.getId(), 1L);

        메뉴_상품 = Arrays.asList(후라이드치킨_메뉴상품, 콜라_메뉴상품);
        후라이드치킨_메뉴.setMenuProducts(메뉴_상품);
    }

    @DisplayName("메뉴 등록에 실패한다.")
    @Test
    void 메뉴_등록에_실패한다() throws Exception {
        given(menuService.create(any(Menu.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/menus")
                        .content(objectMapper.writeValueAsString(후라이드치킨_메뉴))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("메뉴 등록에 성공한다.")
    @Test
    void 메뉴_등록에_성공한다() throws Exception {
        given(menuService.create(any(Menu.class))).willReturn(후라이드치킨_메뉴);

        webMvc.perform(post("/api/menus")
                        .content(objectMapper.writeValueAsString(후라이드치킨_메뉴))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(후라이드치킨_메뉴.getId().intValue())))
                .andExpect(jsonPath("$.name", is(후라이드치킨_메뉴.getName())))
                .andExpect(jsonPath("$.price", is(후라이드치킨_메뉴.getPrice().intValue())))
                .andExpect(jsonPath("$.menuGroupId", is(한마리메뉴_메뉴그룹.getId().intValue())))
                .andExpect(jsonPath("$.menuProducts", hasSize(2)));
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록을_조회한다() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(후라이드치킨_메뉴, 콜라_메뉴));

        webMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
