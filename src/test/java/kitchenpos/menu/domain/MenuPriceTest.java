package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 가격 관련 Domain 단위 테스트")
class MenuPriceTest {

    @DisplayName("메뉴가격이 금액을 초과 했는지 확인한다.")
    @Test
    void overTo() {
        //given
        Amounts amounts1 = new Amounts();
        amounts1.addAmount(new Amount(new ProductPrice(5000), 1));
        amounts1.addAmount(new Amount(new ProductPrice(5000), 2));

        Amounts amounts2 = new Amounts();
        amounts2.addAmount(new Amount(new ProductPrice(10000), 1));
        amounts2.addAmount(new Amount(new ProductPrice(10000), 1));

        //when
        MenuPrice menuPrice = new MenuPrice(20000);

        //then
        assertThat(menuPrice.overTo(amounts1)).isTrue();
        assertThat(menuPrice.overTo(amounts2)).isFalse();
    }

    @DisplayName("메뉴가격은 0원미만 or null 일 수 없다.")
    @Test
    void validate() {

        //then
        assertThatIllegalArgumentException()
                .isThrownBy(()-> new MenuPrice(-1000));
        assertThatIllegalArgumentException()
                .isThrownBy(()-> new MenuPrice(null));
    }
}
