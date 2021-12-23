package kitchenpos.order.domain;

import kitchenpos.order.fixture.OrderLineItemFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테스트")
class OrderTest {
    OrderLineItem 생성된_주문_항목;

    @BeforeEach
    void setup() {
        생성된_주문_항목 = OrderLineItemFixture.create(1L, 1L, 1L);
    }

    @DisplayName("주문 생성")
    @Test
    void 주문_생성() {
        // given
        Long 테이블_ID = 1L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Order.of(테이블_ID, Collections.singletonList(생성된_주문_항목));

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("생성시 테이블 ID는 비어있을 수 없음")
    @Test
    void 생성시_테이블은_비어있을_수_없음() {
        // given
        Long 테이블_ID = null;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Order.of(테이블_ID, Collections.singletonList(생성된_주문_항목));

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성시 주문 항목은 비어있을 수 없음")
    @Test
    void 생성시_주문_항목은_비어있을_수_없음() {
        // given
        Long 테이블_ID = 1L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Order.of(테이블_ID, Collections.emptyList());

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
