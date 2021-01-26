package kitchenpos.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends BaseServiceTest {
    @Autowired
    private MenuService menuService;

    /**
     * Menu: 후라이드_치킨_메뉴
     *  ㄴ MenuGroup: 한마리_메뉴_그룹
     *  ㄴ Price: 16,000
     *  ㄴ MenuProducts:
     *      1. MenuProduct 후라이드_1개
     *          ㄴ Price: 16000
     **/
    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        // given
        Menu menu = Menu.of(등록되어_있지_않은_menu_id, "후라이드양념치킨", BigDecimal.valueOf(16000), 등록된_menuGroup_id);
        menu.addMenuProduct(MenuProduct.of(등록된_product_id, 1));

        // when
        Menu result = menuService.create(menu);

        // then
        assertThat(result).isEqualTo(menu);

        MenuProduct resultMenuProduct = result.getMenuProducts().get(0);
        assertThat(resultMenuProduct.getMenuId()).isEqualTo(menu.getId());
    }

    @DisplayName("메뉴 가격을 설정 안했을 경우 등록하지 못한다.")
    @Test
    void createMenuException1() {
        Menu menu = Menu.of(등록되어_있지_않은_menu_id, "후라이드양념치킨", null, 등록된_menuGroup_id);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0보다 작을 경우 등록하지 못한다.")
    @Test
    void createMenuException2() {
        Menu menu = Menu.of(등록되어_있지_않은_menu_id, "후라이드양념치킨", BigDecimal.valueOf(-1), 등록된_menuGroup_id);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 등록되어 있지 않을 경우 등록하지 못한다.")
    @Test
    void createMenuException3() {
        Menu menu = Menu.of(등록되어_있지_않은_menu_id, "후라이드양념치킨", BigDecimal.valueOf(-1), 등록되어_있지_않은_menuGroup_id);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있지 않은 상품으로 만들어진 메뉴 상품이 있으면 등록할 수 없다.")
    @Test
    void createMenuException4() {
        Menu menu = Menu.of(등록되어_있지_않은_menu_id, "후라이드양념치킨", BigDecimal.valueOf(16000), 등록된_menuGroup_id);
        menu.addMenuProduct(MenuProduct.of(등록되어_있지_않은_product_id, 1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 총 금액보다 크면 등록하지 못한다.")
    @Test
    void createMenuException5() {
        Menu menu = Menu.of(등록되어_있지_않은_menu_id, "후라이드양념치킨", BigDecimal.valueOf(17000), 등록된_menuGroup_id);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
