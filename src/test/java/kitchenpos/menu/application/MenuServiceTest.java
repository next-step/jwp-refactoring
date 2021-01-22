package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuServiceTest {

    private static final String MENU_NAME = "메뉴1";
    private static final BigDecimal PRICE = BigDecimal.valueOf(10_000);

    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() {
        // given
        List<MenuProductRequest> menuProductRequests = Arrays.asList(
            new MenuProductRequest(1L, 1),
            new MenuProductRequest(2L, 1)
        );
        MenuRequest menuRequest = new MenuRequest(
            MENU_NAME, BigDecimal.valueOf(32_000), 4L, menuProductRequests
        );

        // when
        MenuResponse savedMenu = menuService.create(menuRequest);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getMenuProducts()).size().isEqualTo(2);
    }

    @DisplayName("메뉴 등록 예외 - 메뉴 금액은 0보다 커야 한다.")
    @Test
    void create_exception1() {
        // given
        MenuRequest menuRequest = new MenuRequest(
            MENU_NAME, null, 1L, Collections.EMPTY_LIST
        );

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuService.create(menuRequest))
            .withMessage("메뉴 금액은 0보다 커야 한다.");
    }

    @DisplayName("메뉴 등록 예외 - 메뉴는 메뉴 그룹에 속해야 한다.")
    @Test
    void create_exception2() {
        // given
        MenuRequest menuRequest = new MenuRequest(
            MENU_NAME, PRICE, 999999L, Collections.EMPTY_LIST
        );

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴 등록 예외 - 메뉴의 금액은 메뉴 상품의 합과 같아야 한다.")
    @Test
    void create_exception3() {
        // given
        List<MenuProductRequest> menuProductRequests = Arrays.asList(
            new MenuProductRequest(1L, 1),
            new MenuProductRequest(2L, 1)
        );
        MenuRequest menuRequest = new MenuRequest(
            MENU_NAME, BigDecimal.valueOf(16_000), 4L, menuProductRequests
        );

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuService.create(menuRequest))
            .withMessage("메뉴의 금액은 메뉴 상품의 합과 같아야 한다.");
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // when
        List<MenuResponse> list = menuService.list();

        // then
        assertThat(list.size()).isGreaterThan(0);
    }
}
