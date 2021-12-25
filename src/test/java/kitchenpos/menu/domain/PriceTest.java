package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PriceTest {
    
    @DisplayName("가격이 잘 생성되는지 확인")
    @ParameterizedTest
    @CsvSource(value = { "3000:3000", "5000:5000" }, delimiter = ':')
    void 가격_생성(int price, int expected) {
        // given, when
        Price 가격 = Price.from(price);
        
        // then
        assertThat(가격.intValue()).isEqualTo(expected);
    }
    
    @DisplayName("가격은 0원 이상이어야한다 - 예외처리")
    @Test
    void 가격_0원_이상() {
        // given, when, then
        assertThatThrownBy(() -> {
            Price.from(-3000);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("상품은 0원 이상이어야 합니다");
    }
    
    @DisplayName("수량에 따라 총 가격이 잘 만들어지는지 확인")
    @ParameterizedTest
    @CsvSource(value = { "3000:1:3000", "200:6:1200" }, delimiter = ':')
    void 수량별_가격(int price, int quantity, int expected) {
        // given
        Price 가격 = Price.from(price);
        
        // when
        Price 수량에_따른_가격 = 가격.multiply(quantity);
        
        // then
        assertThat(수량에_따른_가격.intValue()).isEqualTo(expected);
    }
    
    @DisplayName("가격끼리 비교가 잘 되는지 확인")
    @ParameterizedTest
    @CsvSource(value = { "3000:5000:-1", "200:6:1" }, delimiter = ':')
    void 가격_비교(int price, int targetPrice, int expected) {
        // given
        Price 가격 = Price.from(price);
        
        // when
        int 가격_비교 = 가격.compareTo(Price.from(targetPrice));
        
        // then
        assertThat(가격_비교).isEqualTo(expected);
    }
    
    @DisplayName("가격끼리  잘 더해지는지 확인")
    @ParameterizedTest
    @CsvSource(value = { "3000:5000:8000", "200:6:206" }, delimiter = ':')
    void 가격_합계(int price, int addPrice, int expected) {
        // given
        Price 가격 = Price.from(price);
        
        // when
        Price 합해진_가격 = 가격.add(Price.from(addPrice));
        
        // then
        assertThat(합해진_가격.intValue()).isEqualTo(expected);
    }

}
