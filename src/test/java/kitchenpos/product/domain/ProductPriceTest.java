package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.menu.domain.MenuProductQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 가격 관련 Domain 단위 테스트")
class ProductPriceTest {

    @DisplayName("금액을 계산한다.")
    @Test
    void calculateAmount() {
        //given
        ProductPrice price = new ProductPrice(1000);

        //when
        int amount1 = price.calculateAmount(new MenuProductQuantity(10));
        int amount2 = price.calculateAmount(new MenuProductQuantity(7));

        //then
        assertThat(amount1).isEqualTo(10_000);
        assertThat(amount2).isEqualTo(7_000);
    }


    @DisplayName("상품 가격은 0원 미만 이거나 null 일 수 없다.")
    @Test
    void validate(){

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(()->  new ProductPrice(-1000));
        assertThatIllegalArgumentException()
                .isThrownBy(()->  new ProductPrice(null));
    }

}
