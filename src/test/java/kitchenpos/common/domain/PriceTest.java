package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.PriceEmptyException;
import kitchenpos.common.exception.PriceNegativeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("가격 테스트")
public class PriceTest {

    @DisplayName("가격은 null 로 생성할 수 없다.")
    @Test
    void createFail_01() {
        assertThatThrownBy(() -> new Price(null))
            .isInstanceOf(PriceEmptyException.class);
    }

    @DisplayName("가격은 음수가 될 수 없다.")
    @Test
    void createFail_02() {
        assertThatThrownBy(() -> new Price(-1))
            .isInstanceOf(PriceNegativeException.class);
    }

    @DisplayName("더하기")
    @Test
    void plus() {
        Price price = Price.wonOf(2);
        assertThat(Price.wonOf(1).plus(price)).isEqualTo(Price.wonOf(3));
    }

    @DisplayName("곱하기")
    @Test
    void multiply() {
        Quantity quantity = new Quantity(3);
        assertThat(Price.wonOf(2).multiply(quantity)).isEqualTo(Price.wonOf(6));
    }

}
