package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("금액 관련 Domain 단위 테스트")
class AmountTest {

    @DisplayName("금액(가격 * 수량)을 계산한다.")
    @Test
    void calculateAmount() {
        //given
        int quantity = 10;
        int price = 10000;
        Amount amount = new Amount(new Price(price), new Quantity(quantity));

        //when then
        assertThat(amount.getAmount()).isEqualTo(100_000);
    }
}
