package kitchenpos.common;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("금액 생성 성공")
    void createPrice() {
        //when & then
        Price price = new Price(new BigDecimal(11L));
    }

    @Test
    @DisplayName("금액이 음수 이거나 null 이면 에러 발생")
    void validatePrice() {
        //when & then
        Assertions.assertThatThrownBy(() -> new Price(new BigDecimal(-1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격정보가 없거나 0원미만이면 안됩니다.");

        //when & then
        Assertions.assertThatThrownBy(() -> new Price(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격정보가 없거나 0원미만이면 안됩니다.");
    }

    @Test
    @DisplayName("금액이 적은지 확인")
    void checkLessthan() {
        //given
        Price price = new Price(100L);

        //when
        boolean lessThan = price.isGreaterThan(new Price(BigDecimal.valueOf(99)));

        //then
        Assertions.assertThat(lessThan).isTrue();
    }

    @Test
    @DisplayName("곱샘 확인")
    void checkMultiply() {
        //when
        Price price = new Price(100L).multiply(BigDecimal.valueOf(5));

        //then
        Assertions.assertThat(price).isEqualTo(new Price(500L));
    }

    @Test
    @DisplayName("덧셈 확인")
    void checkAdd() {
        //when
        Price price = new Price(100L).add(new Price(11L));

        //then
        Assertions.assertThat(price).isEqualTo(new Price(111L));
    }
}