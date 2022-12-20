package kitchenpos.global.domain;

import kitchenpos.product.message.PriceMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    @DisplayName("가격 생성시 [long] 타입 가격이 주어진 경우 생성에 성공한다.")
    void createLongTypePriceTest() {
        // when
        Price price = Price.of(1_000L);

        // then
        assertThat(price).isEqualTo(Price.of(1_000L));
    }

    @Test
    @DisplayName("가격 생성시 [BigDecimal] 타입 가격이 주어진 경우 생성에 성공한다.")
    void createBigDecimalTypePriceTest() {
        // when
        Price price = Price.of(new BigDecimal(1_000L));

        // then
        assertThat(price).isEqualTo(Price.of(1_000L));
    }

    @Test
    @DisplayName("가격 생성시 0원 미만의 가격이 주어진 경우 예외처리되어 생성에 실패한다.")
    void createPriceThrownByInvalidPriceTest() {
        // when & then
        assertThatThrownBy(() -> Price.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PriceMessage.CREATE_ERROR_PRICE_MUST_BE_GREATER_THAN_ZERO.message());
    }

    @Test
    @DisplayName("가격 추가에 성공한다.")
    void addPriceTest() {
        // given
        Price price = Price.of(1_000L);

        // when
        Price addedPrice = price.add(Price.of(1_000L));

        // then
        assertThat(addedPrice).isEqualTo(Price.of(2_000L));
    }

    @Test
    @DisplayName("주어진 수량을 곱한 가격을 반환한다.")
    void multiplyQuantityTest() {
        // given
        Price price = Price.of(1_000L);

        // when
        Price multiplyQuantityPrice = price.multiplyQuantity(Quantity.of(2L));

        // then
        assertThat(multiplyQuantityPrice).isEqualTo(Price.of(2_000L));
    }

    @Test
    @DisplayName("주어진 가격보다 높은 가격인지 검사 후 높은 가격이라면 [true]를 반환한다.")
    void isGreaterThanTest() {
        // given
        Price price = Price.of(2_000L);

        // when
        boolean greaterThan = price.isGreaterThan(Price.of(1_000L));

        // then
        assertThat(greaterThan).isTrue();
    }
}
