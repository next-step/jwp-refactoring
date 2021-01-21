package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("메뉴 가격을 올바른 금액으로 생성")
    @Test
    void createMenuGivenPriceBeGreaterThanOne() {
        final Price price = Price.of(new BigDecimal(1));

        assertThat(price).isNotNull();
    }

    @DisplayName("메뉴 가격을 0원 이하 금액으로 생성시 예외 발생")
    @Test
    void createMenuWhenGivenPriceBeLessThanZero() {
        assertThatThrownBy(
            () -> Price.of(new BigDecimal(0))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
