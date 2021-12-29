package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MenuPriceTest {
    
    @DisplayName("가격은 0원 이상이어야한다")
    @Test
    void 가격_0원_이상() {
        // given, when, then
        assertThatThrownBy(() -> {
            MenuPrice.from(-3000L);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("가격은 0원 이상이어야 합니다");
    }
    
    
    @DisplayName("가격이 비교대상 가격보다 큰지 비교할 수 있다")
    @ParameterizedTest
    @CsvSource(value = { "3000:5000:false", "200:6:true" }, delimiter = ':')
    void 가격_비교(long price, long targetPrice, boolean expected) {
        // given
        MenuPrice 가격 = MenuPrice.from(price);
        
        // when
        boolean 가격_비교 = 가격.isGreaterThan(targetPrice);
        
        // then
        assertThat(가격_비교).isEqualTo(expected);
    }
}
