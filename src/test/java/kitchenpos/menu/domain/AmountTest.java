package kitchenpos.menu.domain;

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
                .isThrownBy(()-> Amount.from(price));
    }

    @Test
    @DisplayName("금액은 필수이다")
    void priceIsNotNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> Amount.from((BigDecimal) null));
    }


    @Test
    @DisplayName("금액의 합계를 구한다")
    void totalAmount() {
        List<Amount> amounts = Arrays.asList(Amount.from(10), Amount.from(20));

        assertThat(Amount.from(30)).isEqualTo(Amount.createSumAmounts(amounts));

    }

}
