package kitchenpos.menu.ui;

import static kitchenpos.menu.application.MenuServiceTest.감자튀김_가격;
import static kitchenpos.menu.application.MenuServiceTest.치즈버거_가격;
import static kitchenpos.menu.application.MenuServiceTest.치즈버거세트_가격;
import static kitchenpos.menu.application.MenuServiceTest.콜라_가격;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("메뉴 API")
@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends RestControllerTest<MenuRequest> {

    public static final String BASE_URL = "/api/menus";
    private static final Product 치즈버거 = new Product(1L, "치즈버거", 치즈버거_가격);
    private static final Product 감자튀김 = new Product(2L, "감자튀김", 감자튀김_가격);
    private static final Product 콜라 = new Product(3L, "콜라", 콜라_가격);
    private static final MenuProduct 치즈버거세트_치즈버거 = new MenuProduct(1L, 치즈버거, 1L);
    private static final MenuProduct 치즈버거세트_감자튀김 = new MenuProduct(1L, 감자튀김, 1L);
    private static final MenuProduct 치즈버거세트_콜라 = new MenuProduct(1L, 콜라, 1L);
    private static final MenuGroup 패스트푸드 = new MenuGroup(1L, "패스트푸드");
    private static final Menu 치즈버거세트 = new Menu(1L, "치즈버거세트", 치즈버거세트_가격, 패스트푸드, Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(menuService.create(any())).willReturn(MenuResponse.of(치즈버거세트));

        // When & Then
        post(BASE_URL, MenuRequest.of(치즈버거세트))
            .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        List<MenuResponse> menus = new ArrayList<>(Arrays.asList(MenuResponse.of(치즈버거세트)));
        given(menuService.list()).willReturn(menus);

        // When & Then
        get(BASE_URL)
            .andExpect(jsonPath("$.*", hasSize(menus.size())));
    }
}
