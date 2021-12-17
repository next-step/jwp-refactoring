package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.domain.fixture.QuantityFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {
    private Product 강정치킨;
    private Quantity 상품_갯수;

    @BeforeEach
    void setup() {
        강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        상품_갯수 = QuantityFixture.create(2L);
    }

    @DisplayName("메뉴 상품 생성 확인")
    @Test
    void 메뉴_상품_생성_확인() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(강정치킨, 상품_갯수);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("정상 가격 확인")
    @Test
    void 정상_가격_확인() {
        // when
        MenuProduct 메뉴_상품 =  MenuProduct.of(강정치킨, 상품_갯수);

        // then
        assertThat(메뉴_상품.getOriginalPrice()).isEqualTo(BigDecimal.valueOf(34_000));
    }

    @DisplayName("생성 실패 테스트")
    @Nested
    class TestCreateFail {
        @DisplayName("상품은 반드시 존재")
        @Test
        void 상품은_반드시_존재() {
            // when
            ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(null, 상품_갯수);

            // then
            assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품 갯수은 반드시 존재")
        @Test
        void 상품_갯수은_반드시_존재() {
            // when
            ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(강정치킨, null);

            // then
            assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
