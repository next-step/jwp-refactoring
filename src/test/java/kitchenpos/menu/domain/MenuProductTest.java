package kitchenpos.menu.domain;

import kitchenpos.common.valueobject.exception.NegativeQuantityException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MenuProductTest {

    @DisplayName("메뉴의 메뉴상품 갯수는 음수가 될 수 없다.")
    @Test
    void createMenuExceptionIfMenuProductQuantityIsNull() {
        //when
        assertThatThrownBy(() -> MenuProduct.of(-1))
                .isInstanceOf(NegativeQuantityException.class); //then
    }

    @DisplayName("상품 가격을 계산한다.")
    @Test
    void getCalculatedPrice() {
        //given
        Product 후라이드 = Product.of("후라이드", BigDecimal.valueOf(10000));
        MenuProduct menuProduct = MenuProduct.of(후라이드, 4);

        //when
        BigDecimal actual = menuProduct.getCalculatedPrice();

        //then
        assertThat(actual.longValue()).isEqualTo(40000L);
    }
}
