package kitchenpos.common.domain;

import kitchenpos.menu.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PriceTest {

    @DisplayName("0원 미만의 가격을 생성하면 예외가 발생해야 한다")
    @Test
    void createPriceByMinusValueTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> 가격_생성(-1));
    }

    @DisplayName("기존 가격에 가격을 더하면 정상 동작해야 한다")
    @Test
    void addPriceTest() {
        // given
        int defaultPrice = 1_000;
        int addPrice = 500;
        Price 가격 = 가격_생성(defaultPrice);

        // when
        Price 결과 = 가격.add(가격_생성(addPrice));

        // then
        assertThat(결과).isEqualTo(가격_생성(defaultPrice + addPrice));
    }

    @DisplayName("기존 가격 수량을 곱하면 정상 동작해야 한다")
    @Test
    void multiplyPriceTest() {
        // given
        int defaultPrice = 1_000;
        Quantity quantity = new Quantity(2L);
        Price 가격 = 가격_생성(defaultPrice);

        // when
        Price 결과 = 가격.multiplyByQuantity(quantity);

        // then
        assertThat(결과).isEqualTo(가격_생성(defaultPrice * 2));
    }

    @DisplayName("다른 가격 비교는 정상 동작해야 한다")
    @Test
    void isNotSamePriceTest() {
        // given
        Price 가격 = 가격_생성(1_000);
        Price 같은_가격 = 가격_생성(1_000);
        Price 다른_가격 = 가격_생성(1_001);

        // when
        boolean 같은_가격_결과 = 가격.isNotSame(같은_가격);
        boolean 다른_가격_결과 = 가격.isNotSame(다른_가격);

        // then
        assertThat(같은_가격_결과).isFalse();
        assertThat(다른_가격_결과).isTrue();
    }

    public static Price 가격_생성(int value) {
        return new Price(new BigDecimal(value));
    }
}