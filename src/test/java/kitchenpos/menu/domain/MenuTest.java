package kitchenpos.menu.domain;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.menu.message.MenuMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("한가지메뉴");
        menuProducts = Arrays.asList(
                MenuProduct.of(Product.of("후라이드", 16_000L), 1L),
                MenuProduct.of(Product.of("콜라", 1_000L), 1L)
        );
    }

    @Test
    @DisplayName("메뉴 생성에 성공한다")
    void createMenuTest() {
        // when
        Menu newMenu = Menu.of("후라이드치킨", 17_000L, menuGroup, menuProducts);

        // then
        assertThat(newMenu).isEqualTo(Menu.of("후라이드치킨", 17_000L, menuGroup, menuProducts));
    }

    @Test
    @DisplayName("메뉴 생성시 메뉴 그룹이 누락 된 경우 예외처리되어 생성에 실패한다")
    void createMenuThrownByEmptyMenuGroupTest() {
        // when & then
        assertThatThrownBy(() -> Menu.of("후라이드치킨", 16_000L, null, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuMessage.CREATE_MENU_ERROR_MENU_GROUP_MUST_BE_NON_NULL.message());
    }

    @Test
    @DisplayName("메뉴 생성시 메뉴 가격이 등록된 상품 요금의 합산된 금액보다 클경우 예외처리되어 생성에 실패한다")
    void createMenuThrownByInValidPriceTest() {
        // when & then
        assertThatThrownBy(() -> Menu.of("후라이드치킨", 20_000L, menuGroup, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuMessage.ADD_PRODUCT_ERROR_IN_VALID_PRICE.message());
    }
}
