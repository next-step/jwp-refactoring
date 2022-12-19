package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class ProductTest {
    @Test
    void 생성() {
        Product product = new Product("후라이드", BigDecimal.valueOf(16000));

        assertThat(product.getName()).isEqualTo("후라이드");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(16000));
    }
}
