package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("금액들 관련 Domain 단위 테스트")
class AmountsTest {

    @DisplayName("총 금액을 계산한다.")
    @Test
    void calculateTotalAmount(){
        //given
        Amounts amounts = new Amounts();
        amounts.addAmount(new Amount(1000, 5));
        amounts.addAmount(new Amount(2000, 5));

        //when then
        assertThat(amounts.calculateTotalAmount()).isEqualTo(15_000);
    }
}
