package kitchenpos.product;

import kitchenpos.common.Quantity;
import kitchenpos.common.Price;
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

    @DisplayName("수량 곱 테스트")
    @Test
    void multiplyQuantityTest() {
        //given
        long quantityValue = 10;
        int priceValue = 5000;
        final BigDecimal result = new BigDecimal(quantityValue * priceValue);
        final Quantity quantity = new Quantity(quantityValue);
        final Price price = new Price(new BigDecimal(priceValue));

        //when
        //then
        assertThat(price.multiplyQuantity(quantity))
                .isEqualTo(new Price(result));
    }

    @DisplayName("가격 덧셈 테스트")
    @Test
    void addPriceTest() {
        //given
        int priceValue1 = 1000;
        int priceValue2 = 2000;
        int result = priceValue1 + priceValue2;
        final Price price1 = new Price(new BigDecimal(priceValue1));
        final Price price2 = new Price(new BigDecimal(priceValue2));

        //when
        //then
        assertThat(price1.add(price2))
                .isEqualTo(new Price(new BigDecimal(result)));
    }

    @DisplayName("기준가격보다 적은가격인지 확인하는 테스트")
    @Test
    void priceLessOrEqualTest() {
        //given
        BigDecimal priceValue1 = new BigDecimal(1000);
        BigDecimal priceValue2 = new BigDecimal(2000);
        Price price1 = new Price(priceValue1);
        Price price2 = new Price(priceValue2);

        //when
        //then
        assertThat(price1.lessOrEqualThan(price2))
                .isTrue();
    }
}
