package kitchenpos.menu.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;

class MenuTest {

    @DisplayName("메뉴 그룹 없이는 메뉴를 만들 수 없다.")
    @Test
    void createMenuNonExistMenuGroup() {
        // given
        BigDecimal price = new BigDecimal(16000);

        // when && then
        assertThatThrownBy(() -> Menu.of("후라이드치킨", price, null, new ArrayList<>()))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }

    @DisplayName("메뉴 상품이 없이는 메뉴를 만들 수 없다.")
    @Test
    void createMenuNotExistMenuProduct() {
        // given
        MenuGroup menuGroup = new MenuGroup("두마리 메뉴");
        BigDecimal price = new BigDecimal(16000);

        // when && then
        assertThatThrownBy(() -> Menu.of("후라이드치킨", price, menuGroup, new ArrayList<>()))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }

    @DisplayName("메뉴 가격이 총 상품 가격보다 크면 메뉴를 만들 수 없다.")
    @Test
    void createMenuWrongMenuPrice() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
            MenuProduct.of(Product.of("후라이드", new BigDecimal(3000)), 2),
            MenuProduct.of(Product.of("후라이드", new BigDecimal(3000)), 3)
        );
        MenuGroup menuGroup = new MenuGroup("두마리 메뉴");
        BigDecimal price = new BigDecimal(16000);

        // when && then
        assertThatThrownBy(() -> Menu.of("후라이드치킨", price, menuGroup, menuProducts))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }
}
