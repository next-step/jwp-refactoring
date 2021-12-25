package kitchenpos.menu.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {
    @DisplayName("메뉴 상품 생성 확인")
    @Test
    void 메뉴_상품_생성_확인() {
        // given
        Long 상품_ID = 1L;
        Long 상품_수량 = 2L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(상품_ID, 상품_수량);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("생성 실패 테스트")
    @Nested
    class TestCreateFail {
        @DisplayName("상품은 반드시 존재")
        @Test
        void 상품은_반드시_존재() {
            // given
            Long 상품_ID = null;
            Long 상품_수량 = 2L;

            // when
            ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(상품_ID, 상품_수량);

            // then
            assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품 갯수은 반드시 존재")
        @Test
        void 상품_갯수은_반드시_존재() {
            // given
            Long 상품_ID = 1L;
            Long 상품_수량 = null;

            // when
            ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProduct.of(상품_ID, 상품_수량);

            // then
            assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
