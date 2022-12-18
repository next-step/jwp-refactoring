package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 메뉴 가격 테스트")
class OrderOrderMenuPriceTest {
    @DisplayName("동등성")
    @Test
    void equals() {
        assertThat(OrderMenuPrice.from(10000L)).isEqualTo(OrderMenuPrice.from(10000L));
        assertThat(OrderMenuPrice.from(10000L)).isNotEqualTo(OrderMenuPrice.from(12000L));
    }

    @DisplayName("가격 없음")
    @Test
    void validate_price_null() {
        assertThatThrownBy(() -> OrderMenuPrice.from(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 0원")
    @Test
    void validate_price_zero() {
        assertThatThrownBy(() -> OrderMenuPrice.from(0L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
