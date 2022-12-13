package kitchenpos.product;

import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {

    @DisplayName("같은 가격 equals 테스트")
    @Test
    void priceEqualTest() {
        //given
        int price = 10000;
        final Price price1 = new Price(new BigDecimal(price));
        final Price price2 = new Price(new BigDecimal(price));

        //when
        //then
        assertThat(price1.equals(price2)).isTrue();
    }

    @DisplayName("다른 가격 equals 테스트")
    @Test
    void priceDifferentTest() {
        //given
        final Price price1 = new Price(new BigDecimal(1000));
        final Price price2 = new Price(new BigDecimal(2000));

        //when
        //then
        assertThat(price1.equals(price2)).isFalse();
    }

    @DisplayName("null로 생성 오류 테스트")
    @Test
    public void createNullExceptionTest() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("음수로 생성 오류 테스트")
    @Test
    public void createUnderZeroExceptionTest() {
        //given
        final BigDecimal bigDecimal = new BigDecimal(-100);

        //when
        //then
        assertThatThrownBy(() -> new Price(bigDecimal))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
