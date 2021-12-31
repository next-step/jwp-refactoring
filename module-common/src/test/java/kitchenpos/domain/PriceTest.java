package kitchenpos.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("Price 단위 테스트")
class PriceTest {

    @Test
    @DisplayName("가격 등록 테스트")
    void savePrice(){
        Price price = new Price(10000);
        Assertions.assertThat(price.getPrice()).isEqualTo(new BigDecimal(10000));
    }

    @Test
    @DisplayName("가격 음수 테스트")
    void saveMinusAmountPrice(){
        Assertions.assertThatThrownBy(()->{
            new Price(-10000);
        }).isInstanceOf(InputDataException.class)
                        .hasMessageContaining(InputDataErrorCode.THE_PRICE_CAN_NOT_INPUT_LESS_THAN_ZERO.errorMessage());
    }

}
