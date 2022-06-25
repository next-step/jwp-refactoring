package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("금액들 관련 Domain 단위 테스트")
class AmountsTest {

    @DisplayName("총 금액을 계산한다.")
    @Test
    void calculateTotalAmount(){
        //given
        Amounts amounts = new Amounts();
        amounts.addAmount(new Amount(new ProductPrice(1000), new MenuProductQuantity(5)));
        amounts.addAmount(new Amount(new ProductPrice(2000), new MenuProductQuantity(5)));

        //when then
        assertThat(amounts.calculateTotalAmount()).isEqualTo(15_000);
    }
}
