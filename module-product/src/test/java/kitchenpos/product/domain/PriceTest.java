package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격 관련")
public class PriceTest {

    @Test
    @DisplayName("가격이 0보다 작을 수 없다.")
    void createPrice() {
        // when
        Integer price = -10;
        // then
        assertThatThrownBy(() -> Price.from(price)).isInstanceOf(IllegalArgumentException.class);
    }
}
