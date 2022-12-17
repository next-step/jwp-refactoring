package kitchenpos.unit;

import kitchenpos.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("가격기능")
public class PriceTest {

    private BigDecimal _3000 = new BigDecimal(3000);
    private BigDecimal _2000 = new BigDecimal(2000);
    private BigDecimal _5000 = new BigDecimal(5000);

    private BigDecimal 음수 = new BigDecimal(-1);

    @Test
    @DisplayName("가격 NULL 비교 기능")
    void priceTest1() {
        assertThat(new Price(null).isNull()).isTrue();
        assertThat(new Price(_3000).isNull()).isFalse();
    }

    @Test
    @DisplayName("가격 음수확인 기능")
    void priceTest2() {
        assertThat(new Price(음수).isNegative()).isTrue();
        assertThat(new Price(_3000).isNegative()).isFalse();
    }

    @Test
    @DisplayName("가격 합산 기능")
    void priceTest3() {
        Price price = new Price(_2000);
        price.add(_3000);
        assertThat(price.value()).isEqualTo(_5000);
    }

    @Test
    @DisplayName("가격 크기 비교 기능")
    void priceTest4() {
        assertThat(new Price(_3000).isGather(new Price(_2000))).isTrue();
        assertThat(new Price(_2000).isGather(new Price(_3000))).isFalse();
    }

}
