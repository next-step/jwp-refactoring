package kitchenpos.product.domain;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void valid_price() {
        Product menu = new Product("닭", BigDecimal.valueOf(1));
        assertThat(menu).isNotNull();
    }

    @DisplayName("가격이 음수이면 생성할 수 없다.")
    @Test
    void invalid_price_1() {
        assertThatThrownBy(() -> new Product("닭", BigDecimal.valueOf(-1)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.INVALID_PRICE.getMessage());
    }

    @DisplayName("가격이 null이면 생성할 수 없다.")
    @Test
    void invalid_price_2() {
        assertThatThrownBy(() -> new Product("닭", BigDecimal.valueOf(-1)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.INVALID_PRICE.getMessage());
    }
}
