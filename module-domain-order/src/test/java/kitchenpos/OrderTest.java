package kitchenpos;


import kitchenpos.common.exception.CannotUpdateException;
import kitchenpos.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.exception.Message.ERROR_ORDER_SHOULD_HAVE_NON_EMPTY_TABLE;
import static kitchenpos.common.exception.Message.ERROR_ORDER_STATUS_CANNOT_BE_CHANGED_WHEN_COMPLETED;
import static kitchenpos.order.OrderStatus.COMPLETION;
import static kitchenpos.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("주문 생성 시, 주문 테이블이 비어있는 상태인 경우 예외발생")
    @Test
    void 비어있는_테이블_주문_생성시_예외발생() {
        Long 비어있는_테이블 = null;

        assertThatThrownBy(() -> new Order(비어있는_테이블, COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_ORDER_SHOULD_HAVE_NON_EMPTY_TABLE.showText());
    }

    @DisplayName("완료처리된 주문의 상태를 변경하는 경우 예외발생")
    @Test
    void 완료처리된_주문_상태_변경시_예외발생() {
        Long 주문테이블_ID = 3L;
        Order 주문 = new Order(주문테이블_ID, COMPLETION);

        assertThatThrownBy(() -> 주문.changeOrderStatus(COOKING))
                .isInstanceOf(CannotUpdateException.class)
                .hasMessage(ERROR_ORDER_STATUS_CANNOT_BE_CHANGED_WHEN_COMPLETED.showText());
    }
}
