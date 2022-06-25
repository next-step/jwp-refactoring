package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 가격 관련 Domain 단위 테스트")
class ProductPriceTest {


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
