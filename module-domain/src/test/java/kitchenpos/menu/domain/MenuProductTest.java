package kitchenpos.menu.domain;

import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.fixture.ProductFixture;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductTest {

    @DisplayName("가격 x 수량 금액 계산한다.")
    @Test
    void calculatePriceQuantity() {
        Product 후라이드 = ProductFixture.생성("후라이드", new BigDecimal("5000"));
        MenuProduct 오천원후라이드2개 = MenuProductFixture.생성(후라이드, 2L);

        BigDecimal 금액 = 오천원후라이드2개.calculatePriceQuantity(후라이드.getPrice());

        assertThat(금액).isEqualTo(new BigDecimal("10000"));

    }
}
