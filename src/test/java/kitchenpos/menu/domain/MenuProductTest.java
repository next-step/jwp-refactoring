package kitchenpos.menu.domain;

import kitchenpos.fixture.MenuProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductTest {

    @DisplayName("가격 x 수량 금액 계산한다.")
    @Test
    void calculatePriceQuantity() {
        MenuProduct 오천원후라이드2개 = MenuProductFixture.후라이드두마리();

        BigDecimal 금액 = 오천원후라이드2개.calculatePriceQuantity();

        assertThat(금액).isEqualTo(new BigDecimal("10000"));

    }
}
