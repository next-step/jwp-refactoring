package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ProductPriceTest {
    
    @DisplayName("가격은 0원 이상이어야한다")
    @Test
    void 가격_0원_이상() {
        // given, when, then
        assertThatThrownBy(() -> {
            ProductPrice.from(-3000L);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("가격은 0원 이상이어야 합니다");
    }
    
    @DisplayName("가격은 곱셈이 가능하다")
    @ParameterizedTest
    @CsvSource(value = { "3000:1:3000", "200:6:1200" }, delimiter = ':')
    void 가격_곱셈(long price, long quantity, long expected) {
        // given
        ProductPrice 가격 = ProductPrice.from(price);
        
        // when
        ProductPrice 수량에_따른_가격 = 가격.multiply(quantity);
        
        // then
        assertThat(수량에_따른_가격.getValue()).isEqualTo(expected);
    }
}
