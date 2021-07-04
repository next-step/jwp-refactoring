package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;

@DisplayName("상품 도메인 테스트")
class ProductTest {
    @DisplayName("상품 생성 실패 - 가격이 음수")
    @Test
    void createFailedByPrice() {
        // given
        // when
        // then
        assertThatThrownBy(() -> Product.of("순대", -1000)).isInstanceOf(CustomException.class)
                .hasMessage(ErrorInfo.PRICE_CAN_NOT_NEGATIVE.message());
    }
}