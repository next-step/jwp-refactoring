package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @DisplayName("생성 검증")
    @Test
    void create() {
        Product product = new Product("후라이드", new BigDecimal("10000"));

        assertThat(product).isNotNull();
    }

    @DisplayName("금액이 없거나 음수일 경우 예외처리")
    @Test
    void priceError() {

        assertThatThrownBy(
                () -> new Product("후라이드", new BigDecimal("-10000"))
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(
                () -> new Product("후라이드", null)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
