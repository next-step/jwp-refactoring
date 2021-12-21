package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {
    private Product 강정치킨;

    @BeforeEach
    void setup() {
        강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
    }

    @DisplayName("메뉴 상품 생성 확인")
    @Test
    void 메뉴_상품_생성_확인() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(강정치킨.getId(), 2L);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("생성 실패 테스트")
    @Nested
    class TestCreateFail {
        @DisplayName("상품은 반드시 존재")
        @Test
        void 상품은_반드시_존재() {
            // when
            ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(null, 2L);

            // then
            assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품 갯수은 반드시 존재")
        @Test
        void 상품_갯수은_반드시_존재() {
            // when
            ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(강정치킨.getId(), null);

            // then
            assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
