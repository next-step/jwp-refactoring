package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.fixture.OrderTableFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테스트")
class OrderTest {
    private OrderTable 테이블;

    @BeforeEach
    void setup() {
        테이블 = OrderTableFixture.create(1L, 4, false);
    }

    @DisplayName("주문 생성")
    @Test
    void 주문_생성() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Order.of(테이블);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("생성시 테이블은 비어있을 수 없음")
    @Test
    void 생성시_테이블은_비어있을_수_없음() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Order.of(null);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
