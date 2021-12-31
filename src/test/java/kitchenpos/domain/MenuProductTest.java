package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyProductException;
import kitchenpos.common.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품 관련 테스트")
class MenuProductTest {

    @DisplayName("생성 시, 상품 정보가 필요합니다")
    @Test
    void validateTest2() {
        assertThatThrownBy(() -> MenuProduct.of(null, 2L))
                .isInstanceOf(EmptyProductException.class);
    }

    @DisplayName("생성 시, 수량 정보가 0 이상이어야 합니다")
    @Test
    void validateTest3() {
        assertThatThrownBy(() -> MenuProduct.of(Product.of("상품", 1000), -1L))
                .isInstanceOf(NegativeQuantityException.class);
    }
}
