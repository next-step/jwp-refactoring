package kitchenpos.order.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 항목 테스트")
class OrderLineItemTest {
    @DisplayName("주문 항목 생성")
    @Test
    void 주문_항목_생성() {
        // given
        Long 메뉴_ID = 1L;
        Long 수량 = 2L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> OrderLineItem.of(메뉴_ID, 수량);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("생성시 메뉴는 비어있을 수 없음")
    @Test
    void 생성시_메뉴는_비어있을_수_없음() {
        // given
        Long 메뉴_ID = null;
        Long 수량 = 2L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> OrderLineItem.of(메뉴_ID, 수량);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성시 수량은 비어있을 수 없음")
    @Test
    void 생성시_수량은_비어있을_수_없음() {
        // given
        Long 메뉴_ID = 1L;
        Long 수량 = null;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> OrderLineItem.of(메뉴_ID, 수량);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
