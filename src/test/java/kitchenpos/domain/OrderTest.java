package kitchenpos.domain;

import kitchenpos.exception.OrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    Order 주문;
    OrderTable 테이블;

    @Test
    @DisplayName("주문상태를 완료상태로 변경하면 완료상태로 변경된다")
    void changeOrderStatus() {
        테이블 = new OrderTable(1L, 2, false);
        주문 = new Order(테이블);

        주문.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(주문.getOrderStatus().name()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문상태를 완료상태로 변경하면 완료상태로 변경된다")
    void changeOrderStatusAlreadyComplete() {
        //given
        테이블 = new OrderTable(1L, 2, false);
        주문 = new Order(테이블);
        주문.setOrderStatus(OrderStatus.COMPLETION);

        //then
        assertThatThrownBy(() -> 주문.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(OrderStatusException.class)
                .hasMessageContaining(OrderStatusException.COMPLETE_DOES_NOT_CHANGE_MSG);
    }
}