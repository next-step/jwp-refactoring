package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.core.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품에 대한 단위 테스트")
class MenuProductTest {

    @DisplayName("메뉴 상품이 상품정보를 갖고있지 않으면 예외가 발생한다")
    @Test
    void exception_test() {
        // given
        MenuProduct menuProduct = MenuProduct.of(null, 3);

        // then
        assertThatThrownBy(menuProduct::validateHasProduct)
            .isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.CONTAINS_NOT_EXIST_PRODUCT.getMessage());
    }

    @DisplayName("상품의 가격, 개수의 합을 정상적으로 반환해야 한다")
    @Test
    void total_price_test() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, 3);

        // then
        assertThat(menuProduct.getTotalPrice()).isEqualTo(new Price(BigDecimal.valueOf(1500L)));
    }

    @DisplayName("메뉴 상품에 메뉴 객체를 매핑하면 정상적으로 매핑되어야 한다")
    @Test
    void mapping_test() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, 3);

        // when
        Menu menu = Menu.of("menu", BigDecimal.valueOf(500L), null, Collections.singletonList(menuProduct));

        // then
        assertNotNull(menuProduct.getMenu());
        assertThat(menuProduct.getMenu()).isEqualTo(menu);
    }
}
