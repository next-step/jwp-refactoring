package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    private Product product;
    private MenuProduct menuProduct;
    private Menu menu;

    /**
     * Menu: 후라이드_치킨_메뉴
     *  ㄴ MenuGroup: 한마리_메뉴_그룹
     *  ㄴ Price: 16,000
     *  ㄴ MenuProducts:
     *      1. MenuProduct 후라이드_1개
     *          ㄴ Price: 16000
     **/
    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = MenuGroup.of(등록된_menuGroup_id, "한마리메뉴");
        product = product_생성(1L, "후라이드", BigDecimal.valueOf(16000));
        menuProduct = menuProduct_생성(product.getId(), 1);

        menu = menu_생성(7L, "후라이드양념치킨", BigDecimal.valueOf(16000), menuGroup.getId());
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        // given
        menu.addMenuProduct(menuProduct);

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
        assertThatThrownBy(() -> menuService.create(menu_price_변경(menu, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0보다 작을 경우 등록하지 못한다.")
    @Test
    void createMenuException2() {
        assertThatThrownBy(() -> menuService.create(menu_price_변경(menu, BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 등록되어 있지 않을 경우 등록하지 못한다.")
    @Test
    void createMenuException3() {
        assertThatThrownBy(() -> menuService.create(menu_menuGroupId_변경(menu, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있지 않은 상품으로 만들어진 메뉴 상품이 있으면 등록할 수 없다.")
    @Test
    void createMenuException4() {
        menu.addMenuProduct(menuProduct_생성(7L, 1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 총 금액보다 크면 등록하지 못한다.")
    @Test
    void createMenuException5() {
        assertThatThrownBy(() -> menuService.create(menu_price_변경(menu, BigDecimal.valueOf(17000))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
