package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품")
class ProductTest {
    private static final Product 후라이드 = Product.of("후라이드", 16000);

    @DisplayName("해당 상품의 개수를 입력하면 총 금액을 계산할 수 있다.")
    @Test
    void calculateTotal() {
        assertThat(후라이드.calculateTotal(2)).isEqualTo(Price.from(32000));
    }
}
