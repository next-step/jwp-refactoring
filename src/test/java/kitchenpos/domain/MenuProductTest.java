package kitchenpos.domain;

import kitchenpos.common.exceptions.NegativeQuantityException;
import kitchenpos.common.exceptions.ProductRequiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품 관련 테스트")
class MenuProductTest {

    private MenuProduct 메뉴상품_생성;
    private Product 상품;

    @BeforeEach
    void setUp() {
        상품 = Product.of("상품", 12000);
        메뉴상품_생성 = MenuProduct.of(null, 상품, 1L);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(메뉴상품_생성)
                .isEqualTo(MenuProduct.of(null, 상품, 1L));
    }

    @DisplayName("생성 시, 상품 정보가 필요합니다")
    @Test
    void validateTest2() {
        assertThatThrownBy(() -> MenuProduct.of(null, 2L))
                .isInstanceOf(ProductRequiredException.class);
    }

    @DisplayName("생성 시, 수량 정보가 0 이상이어야 합니다")
    @Test
    void validateTest3() {
        assertThatThrownBy(() -> MenuProduct.of(상품, -1L))
                .isInstanceOf(NegativeQuantityException.class);
    }

}
