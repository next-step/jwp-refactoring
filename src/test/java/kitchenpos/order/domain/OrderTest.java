package kitchenpos.order.domain;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Long orderTableId;
    private Long menuId;

    @BeforeEach
    void setUp() {
        orderTableId = Long.valueOf(3L);
        menuId = Long.valueOf(3L);
    }

    @Test
    @DisplayName("주문 생성")
    void 주문등록() {
        Order order = 테스트_주문_생성();
        Assertions.assertAll("새로 생성된 주문을 확인한다."
                , () -> assertThat(order.getOrderTableId()).isEqualTo(orderTableId)
                , () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
                , () -> assertThatAllOrderLineItemIncludedToOrder(order)
        );
    }

    private Order 테스트_주문_생성() {
        Long menuId = Long.valueOf(3L);
        OrderLineItem orderLineItem1 = new OrderLineItem(new OrderLineMenu(menuId, "메뉴1", BigDecimal.valueOf(1000)), 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(new OrderLineMenu(menuId, "메뉴2", BigDecimal.valueOf(2000)), 4);

        return new Order(orderTableId, Lists.newArrayList(orderLineItem1, orderLineItem2));
    }

    private void assertThatAllOrderLineItemIncludedToOrder(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems().toList();
        List<OrderLineItem> includedOrderLineItems = orderLineItems.stream()
                .filter(orderLineItem -> orderLineItem.getOrder() == order)
                .collect(toList());
        assertThat(includedOrderLineItems).hasSize(orderLineItems.size());
    }

    @Test
    @DisplayName("주문항목이 없는경우 주문 등록 실패")
    void 주문등록_주문항목이_없는경우() {
        List<OrderLineItem> orderLineItems = emptyList();
        assertThatThrownBy(() -> new Order(orderTableId, orderLineItems))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void 주문상태_변경() {
        Order order = 테스트_주문_생성();
        order.changeOrderStatus(OrderStatus.COMPLETION);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("주문 상태가 계산완료인 경우 주문 상태 변경 불가")
    void 주문상태_변경_이미_계산완료상태인경우() {
        Order order = 테스트_주문_생성();

        order.changeOrderStatus(OrderStatus.COMPLETION);
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(CannotChangeOrderStatusException.class);
    }
}
