package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.utils.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductTest {

    @Test
    @DisplayName("메뉴 구성 상품의 가격은 가격과 수량을 곱하여 계산한다.")
    void calculateMenuProductPrice() {
        //given
        BigDecimal price = TestFixture.메뉴상품_후라이드.getProductPrice();
        long quantity = TestFixture.메뉴상품_후라이드.getQuantity();
        BigDecimal expected = price.multiply(BigDecimal.valueOf(quantity));

        //when
        Price pricePerQuantity = TestFixture.메뉴상품_후라이드.getPricePerQuantity();

        //then
        assertThat(pricePerQuantity.value()).isEqualByComparingTo(expected);
    }
}
