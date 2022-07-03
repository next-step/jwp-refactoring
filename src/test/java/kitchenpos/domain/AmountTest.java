package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmountTest {

    @DisplayName("금액은 0원 이상 이어야한다.")
    @Test
    void priceValid() {
        //given
        BigDecimal price = BigDecimal.valueOf(-1);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(()-> Amount.of(price));
    }

    @Test
    @DisplayName("금액은 필수이다")
    void priceIsNotNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> Amount.of((BigDecimal) null));
    }

    @Test
    @DisplayName("수량은 필수이다")
    void qtyIsNotNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> Amount.of(new Product("이름", BigDecimal.ONE), null));
    }


    @Test
    @DisplayName("제품의 가격은 필수이다")
    void productPriceIsNotNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> Amount.of(new Product("이름", (Price) null), Quantity.of(10)));
    }

    @Test
    @DisplayName("금액의 합계를 구한다")
    void totalAmount() {
        List<Amount> amounts = Arrays.asList(Amount.of(10), Amount.of(20));

        assertThat(Amount.of(30)).isEqualTo(Amount.createSumAmounts(amounts));

    }

}