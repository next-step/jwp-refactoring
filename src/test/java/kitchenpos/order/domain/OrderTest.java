package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private OrderTable 주문테이블;
    private OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable(1L, null, 4, false);
        주문항목 = new OrderLineItem(1L, null, null, 1L);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        //given
        OrderStatus 식사중 = OrderStatus.MEAL;
        Order order = OrderFactory.create(1L, 주문테이블, Collections.singletonList(주문항목));
        //when
        order.changeStatus(식사중);
        //then
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(식사중);

    }

    @DisplayName("주문의 상태가 완료이면 상태를 변경할 수 없다.")
    @Test
    void changeStatusCompletion() {
        //given
        OrderStatus 식사중 = OrderStatus.MEAL;
        Order order = OrderFactory.create(1L, 주문테이블, Collections.singletonList(주문항목));
        order.changeStatus(OrderStatus.COMPLETION);
        //when & then
        assertThatThrownBy(() -> order.changeStatus(식사중))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문항목이 비어 있으면 주문을 생성할 수 없다.")
    @Test
    void validOrderLineItems() {
        //when & then
        assertThatThrownBy(() -> OrderFactory.create(1L, 주문테이블, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 비어 있으면 주문을 생성할 수 없다.")
    @Test
    void validOrderTable() {
        //given
        OrderTable 빈주문테이블 = new OrderTable(1L, null, 0, true);
        Order order = OrderFactory.create(1L, 주문테이블, Collections.singletonList(주문항목));
        //when & then
        assertThatThrownBy(() -> OrderFactory.create(1L, 빈주문테이블, Collections.singletonList(주문항목)))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
