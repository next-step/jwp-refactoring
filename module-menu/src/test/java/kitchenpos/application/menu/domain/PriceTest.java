package kitchenpos.application.menu.domain;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Price;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("가격 테스트")
class PriceTest {

    @DisplayName("가격은 0보다 큰수를 입력해야 한다.")
    @Test
    void notZeroPrice() {
        // given && when && then
        Assertions.assertThatThrownBy(() -> Price.of(BigDecimal.valueOf(-1000)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("기존 가격이 전체 가격보다 크면 안된다.")
    @Test
    void thePriceMustBeLessThanTheTotalPrice() {
        // given
        Price price = Price.of(BigDecimal.valueOf(10_000));

        // when && then
        Assertions.assertThatThrownBy(() -> price.validateTotalPrice(Price.of(BigDecimal.valueOf(9000))))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
