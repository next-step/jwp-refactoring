package kitchenpos.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.exception.PriceNotAcceptableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("메뉴 가격 생성")
    @Test
    void constructor() {
        BigDecimal value = BigDecimal.valueOf(1000L);
        Price price = new Price(value);
        assertThat(price).isEqualTo(new Price(value));
    }

    @DisplayName("메뉴 가격은 0원 이상이어야 한다.")
    @Test
    void constructor_exception() {
        BigDecimal value = BigDecimal.valueOf(-1L);
        assertThatThrownBy(() -> new Price(value))
            .isInstanceOf(PriceNotAcceptableException.class);
    }
}
