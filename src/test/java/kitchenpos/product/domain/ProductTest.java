package kitchenpos.product.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_시_이름이_비어있으면_예외가_발생한다(String name) {
        assertThatThrownBy(() -> new Product(name, BigDecimal.valueOf(5L)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성_시_금액이_0_이하이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Product("test", BigDecimal.valueOf(-1L)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
