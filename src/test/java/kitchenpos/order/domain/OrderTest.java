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

    @DisplayName("주문 테이블이 존재해야 한다.")
    @Test
    void createExistOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            final Order 주문테이블_없는_주문 = new Order(null, Lists.newArrayList(주문_항목, 주문_항목2));

            // then
        }).isInstanceOf(NotFoundEntityException.class);
    }

    @DisplayName("주문 테이블은 비어있지 않아야 한다.")
    @Test
    void createEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            final Order 빈_주문테이블_주문 = new Order(new OrderTable(0), Lists.newArrayList(주문_항목, 주문_항목2));

            // then
        }).isInstanceOf(EmptyOrderTableException.class);
    }

    @DisplayName("주문 상태 변경 시 주문 상태는 완료된 상태가 아니어야 한다.")
    @Test
    void changeOrderStatusNotCompletedStatusExceptionTest() {
        assertThatThrownBy(() -> {
            주문_상태를_변경한다(주문, OrderStatus.COMPLETION);
        }).isInstanceOf(OrderStatusCompletedException.class);
    }

    public static void 주문_상태를_변경한다(Order order, OrderStatus orderStatus) {
        order.changeOrderStatus(orderStatus);
    }
}