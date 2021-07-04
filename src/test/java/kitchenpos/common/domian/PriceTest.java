package kitchenpos.common.domian;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.error.CustomException;
import kitchenpos.error.ErrorInfo;

@DisplayName("가격 테스트")
class PriceTest {
    @DisplayName("생성")
    @Test
    void create() {
        // given
        // when
        Price price = new Price(100);
        // then
        assertThat(price).isEqualTo(new Price(100));
    }

    @DisplayName("생성 실패 - 가격이 음수")
    @Test
    void createFailedByPrice() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Price(-100))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorInfo.PRICE_CAN_NOT_NEGATIVE.message());
    }
}