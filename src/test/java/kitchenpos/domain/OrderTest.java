package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.Exception.OrderStatusCompleteException;
import kitchenpos.Exception.OrderTableAlreadyEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {
    private OrderTable orderTable;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(NumberOfGuests.from(5), false);
        orderLineItems = Arrays.asList(new OrderLineItem(1L, null, 1L, 1));
    }

    @Test
    void 주문_항목_추가() {
        // given
        Order order = new Order(orderTable);

        // when
        order.addOrderLineItems(OrderLineItems.from(orderLineItems));

        // then
        assertThat(order.getOrderLineItems().getValues().get(0).getSeq())
                .isEqualTo(orderLineItems.get(0).getSeq());
    }

    @Test
    void 주문_상태_변경_완료_상태_예외() {
        // given
        Order order = new Order(1L, orderTable, OrderStatus.COMPLETION, null, orderLineItems);

        // when, then
        assertThatThrownBy(
                () -> order.changeOrderStatus(OrderStatus.COOKING)
        ).isInstanceOf(OrderStatusCompleteException.class);
    }

    @Test
    void 주문_생성_빈_테이블_예외() {
        // given
        orderTable.changeEmpty(true);

        // when, then
        assertThatThrownBy(
                () -> new Order(orderTable)
        ).isInstanceOf(OrderTableAlreadyEmptyException.class);
    }
}
