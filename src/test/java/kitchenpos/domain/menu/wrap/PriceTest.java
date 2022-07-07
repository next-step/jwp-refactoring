package kitchenpos.domain.menu.wrap;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:Menu:Wrap:Price")
class PriceTest {

    @Test
    @DisplayName("메뉴 가격이, 메뉴에 구성된 상품 가격 총합보다 큰 경우 예외")
    public void throwException_WhenMenuPriceIsOverThanMenuProductsTotalPrice() {
        // Given
        final BigDecimal givenMenuProductsPrice = new BigDecimal(10000);
        final BigDecimal givenMenuPrice = givenMenuProductsPrice.add(new BigDecimal(1));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> Price.of(givenMenuPrice, givenMenuProductsPrice));
    }
}
