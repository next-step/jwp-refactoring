package kitchenpos.menu.domain.embedded;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuPriceTest {

    @Test
    @DisplayName("null 체크 테스트")
    void nullTest(){
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new MenuPrice(null)
        );
    }

    @Test
    @DisplayName("음수 체크 테스트")
    void negativeTest(){
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new MenuPrice(new BigDecimal(-100))
        );
    }

    @Test
    @DisplayName("비교 테스트 - price 가 더 작다")
    void greaterTest(){
        // given
        MenuPrice price = new MenuPrice(8000);

        // when & then
        assertThat(price.isLessThan(new BigDecimal(9000))).isTrue();
    }

    @Test
    @DisplayName("비교 테스트 - price 가 작지 않다")
    void lessThanTest(){
        // given
        MenuPrice price = new MenuPrice(10000);

        // when & then
        assertThat(price.isLessThan(new BigDecimal(9000))).isFalse();
    }

    @Test
    @DisplayName("곱셈 테스트")
    void multiplyTest(){
        // given
        MenuPrice price = new MenuPrice(10000);

        // when
        BigDecimal fiftyThousand = price.multiply(5);

        // then
        assertThat(fiftyThousand.intValue()).isEqualTo(50000);
    }
}
