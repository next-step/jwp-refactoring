package kichenpos.order.order.domain.event;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;

import kichenpos.order.order.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 생성 이벤트")
@ExtendWith(MockitoExtension.class)
class OrderCreatedEventTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> OrderCreatedEvent.from(mock(Order.class)));
    }

    @Test
    @DisplayName("이벤트 주문은 필수")
    void instance_nullOrder_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> OrderCreatedEvent.from(null))
            .withMessage("이벤트 대상 주문은 필수입니다.");
    }
}
