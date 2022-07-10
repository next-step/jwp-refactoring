package kitchenpos.product.domain;

import kitchenpos.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceTest {

    @Test
    @DisplayName("Price 생성 테스트")
    void create() {
        // when
        final Price price = Price.of(BigDecimal.valueOf(1000));
        // then
        assertThat(price).isInstanceOf(Price.class);
    }

    @Test
    @DisplayName("Price 최솟값 오류 테스트")
    void createException() {
        // when
        final Price price = Price.of(BigDecimal.valueOf(1000));
        // then
        assertThat(price).isInstanceOf(Price.class);
    }
}
