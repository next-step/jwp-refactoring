package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.Exception.InvalidPriceException;
import kitchenpos.common.Price;
import kitchenpos.order.domain.Quantity;
import org.junit.jupiter.api.Test;

class PriceTest {
    @Test
    void 음수_예외() {
        assertThatThrownBy(
                () -> Price.from(-100)
        ).isInstanceOf(InvalidPriceException.class);
    }

    @Test
    void 더하기() {
        // given
        Price price1 = Price.from(100);
        Price price2 = Price.from(1000);

        // when
        Price sum = price1.add(price2);

        // then
        assertThat(sum).isEqualTo(Price.from(1100));
    }

    @Test
    void 곱하기() {
        // given
        Price price = Price.from(100);

        // when
        Price result = price.multiply(Quantity.from(10));

        // then
        assertThat(result).isEqualTo(Price.from(1000));
    }
}
