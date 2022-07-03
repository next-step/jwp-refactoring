package kitchenpos.menu.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
    @DisplayName("메뉴를 생성한다.")
    @Test
    void construct() {
        MenuProduct menuProduct = new MenuProduct(new Product("테스트상품", BigDecimal.valueOf(1500L)), 1L);
        Menu menu = new Menu("치킨", BigDecimal.valueOf(1000L), 1L, Arrays.asList(menuProduct));

        assertThat(menu).isNotNull();
    }

    @DisplayName("상품 가격이 음수이면 메뉴를 생성할 수 없다")
    @Test
    void negativePrice() {
        MenuProduct menuProduct = new MenuProduct(new Product("테스트상품", BigDecimal.valueOf(1500L)), 1L);

        assertThatThrownBy(() -> new Menu("치킨", BigDecimal.valueOf(-1L), 1L, Arrays.asList(menuProduct)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_PRICE.getMessage());
    }

    @DisplayName("메뉴의 가격은 상품들 금액의 합보다 작아야 한다.")
    @Test
    void negativePrice1() {
        MenuProduct menuProduct = new MenuProduct(new Product("테스트상품", BigDecimal.valueOf(900L)), 1L);

        assertThatThrownBy(() -> new Menu("치킨", BigDecimal.valueOf(1000L), 1L, Arrays.asList(menuProduct)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_MENU_PRICE.getMessage());
    }
}
