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

    @ParameterizedTest
    @NullSource
    void 가격이_NULL인_경우(BigDecimal price) {
        Product product = new Product("양념치킨", price);
        assertThatThrownBy(product::validatePrice).isInstanceOf(KitchenposException.class);
    }

    @Test
    void 가격이_마이너스인_경우() {
        Product product = new Product("후라이드", BigDecimal.valueOf(-1));
        assertThatThrownBy(product::validatePrice).isInstanceOf(KitchenposException.class);
    }
}
