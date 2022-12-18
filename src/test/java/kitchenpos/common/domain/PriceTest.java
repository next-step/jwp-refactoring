package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {

    @Test
    @DisplayName("null 체크 테스트")
    void nullTest(){
        // given
        Price price = new Price();

        // when & then
        assertThat(price.isNull()).isTrue();
    }

    @Test
    @DisplayName("비교 테스트 - price 가 더 작다")
    void greaterTest(){
        // given
        Price price = new Price(8000);

        // when & then
        assertThat(price.isLessThan(new BigDecimal(9000))).isTrue();
    }

    @Test
    @DisplayName("비교 테스트 - price 가 작지 않다")
    void lessThanTest(){
        // given
        Price price = new Price(10000);

        // when & then
        assertThat(price.isLessThan(new BigDecimal(9000))).isFalse();
    }

    @Test
    @DisplayName("곱셈 테스트")
    void multiplyTest(){
        // given
        Price price = new Price(10000);

        // when
        BigDecimal fiftyThousand = price.multiply(5);

        // then
        assertThat(fiftyThousand.intValue()).isEqualTo(50000);
    }
}
