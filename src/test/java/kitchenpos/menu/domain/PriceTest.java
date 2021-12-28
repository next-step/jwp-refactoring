package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PriceTest {
    
    @DisplayName("가격은 0원 이상이어야한다")
    @Test
    void 가격_0원_이상() {
        // given, when, then
        assertThatThrownBy(() -> {
            Price.from(-3000L);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("가격은 0원 이상이어야 합니다");
    }
    
    @DisplayName("가격은 곱셈이 가능하다")
    @ParameterizedTest
    @CsvSource(value = { "3000:1:3000", "200:6:1200" }, delimiter = ':')
    void 가격_곱셈(long price, long quantity, long expected) {
        // given
        Price 가격 = Price.from(price);
        
        // when
        Price 수량에_따른_가격 = 가격.multiply(quantity);
        
        // then
        assertThat(수량에_따른_가격.getValue()).isEqualTo(expected);
    }
    
    @DisplayName("가격이 비교대상 가격보다 큰지 비교할 수 있다")
    @ParameterizedTest
    @CsvSource(value = { "3000:5000:false", "200:6:true" }, delimiter = ':')
    void 가격_비교(long price, long targetPrice, boolean expected) {
        // given
        Price 가격 = Price.from(price);
        
        // when
        boolean 가격_비교 = 가격.isGreaterThan(Price.from(targetPrice));
        
        // then
        assertThat(가격_비교).isEqualTo(expected);
    }
    
    @DisplayName("가격을 더할 수 있다")
    @ParameterizedTest
    @CsvSource(value = { "3000:5000:8000", "200:6:206" }, delimiter = ':')
    void 가격_합계(long price, long addPrice, long expected) {
        // given
        Price 가격 = Price.from(price);
        
        // when
        Price 합해진_가격 = 가격.add(Price.from(addPrice));
        
        // then
        assertThat(합해진_가격.getValue()).isEqualTo(expected);
    }

}
