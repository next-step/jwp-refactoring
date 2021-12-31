package kitchenpos.order.domain;

import kitchenpos.common.exception.EmptyOrderTableException;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.common.exception.OrderStatusCompletedException;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 도메인 테스트")
class OrderTest {
    private Order 주문;
    private OrderLineItem 주문_항목;
    private OrderLineItem 주문_항목2;
    private OrderTable 주문_테이블_1번;

    @BeforeEach
    void setUp() {
        주문_테이블_1번 = new OrderTable(3);
        주문_항목 = new OrderLineItem();
        주문_항목2 = new OrderLineItem();
        주문 = new Order(주문_테이블_1번, Lists.newArrayList(주문_항목, 주문_항목2));
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createOrderTest() {
        assertAll(
                () -> assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.NONE),
                () -> assertThat(주문.getOrderTable().getNumberOfGuests()).isEqualTo(3)
        );
    }
}