package kitchenpos.fixture;

import kitchenpos.domain.*;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestOrderFactory {
    public static OrderRequest 주문_생성_요청(final Long orderTableId) {
        return OrderRequest.of(orderTableId, Lists.newArrayList());
    }

    public static OrderLineItemRequest 주문_상품_생성_요청(final Long menuId, final long quantity) {
        return OrderLineItemRequest.of(menuId, quantity);
    }

    public static OrderRequest 주문_완료_요청() {
        return OrderRequest.from(OrderStatus.COMPLETION);
    }

    public static Order 주문_생성_Cooking_단계(final OrderTable orderTable) {
        return Order.from(orderTable);
    }

    public static Order 주문_생성_Cooking_단계(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.of(numberOfGuests, empty);
        return Order.from(orderTable);
    }

    public static Order 주문_생성_주문테이블_없음() {
        return Order.from(null);
    }

    public static void 주문_주문아이템_추가(final Order order, final List<OrderLineItem> orderLineItems) {
        order.addOrderLineItems(orderLineItems);
    }

    public static OrderLineItem 주문_상품_생성(final Menu menu, final long quantity) {
        return OrderLineItem.of(menu, quantity);
    }

    public static void 주문_상태_cooking_meal_변경(final Order order) {
        order.updateStatus(OrderStatus.MEAL);
    }

    public static void 주문_상태_meal_complete_변경(final Order order) {
        order.updateStatus(OrderStatus.COMPLETION);
    }

    public static int 주문_상품_목록_개수_확인(final Order order) {
        return order.getOrderLineItems().toList().size();
    }

    public static OrderStatus 주문_상태_확인(final Order order) {
        return order.getOrderStatus();
    }

    public static List<Order> 주문_목록_조회됨(final int countOrder) {
        final List<Order> orders = new ArrayList<>();
        for (int i = 1; i <= countOrder; i++) {
            orders.add(Order.of(Long.valueOf(i), OrderTable.of(1L, 10, false), OrderStatus.COOKING, OrderLineItems.from(new ArrayList<>())));
        }
        return orders;
    }

    public static Order 주문_cooking_조회됨(final int orderLineItemcount, final Long id, final OrderTable orderTable, final Menu menu, final int quantity) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (int i = 1; i <= orderLineItemcount; i++) {
            OrderLineItem.of(Long.valueOf(i), menu, quantity);
        }
        return Order.of(id, orderTable, OrderStatus.COOKING, OrderLineItems.from(orderLineItems));
    }

    public static Order 주문_meal_조회됨(final int orderLineItemcount, final Long id, final OrderTable orderTable, final Menu menu, final int quantity) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (int i = 1; i <= orderLineItemcount; i++) {
            OrderLineItem.of(Long.valueOf(i), menu, quantity);
        }
        return Order.of(id, orderTable, OrderStatus.MEAL, OrderLineItems.from(orderLineItems));
    }

    public static Order 주문_complete_조회됨(final int orderLineItemcount, final Long id, final OrderTable orderTable, final Menu menu, final int quantity) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (int i = 1; i <= orderLineItemcount; i++) {
            OrderLineItem.of(Long.valueOf(i), menu, quantity);
        }
        return Order.of(id, orderTable, OrderStatus.COMPLETION, OrderLineItems.from(orderLineItems));
    }

    public static void 주문_생성_확인됨(final OrderResponse orderResponse, final Order order) {
        assertAll(
                () -> assertThat(orderResponse).isNotNull(),
                () -> assertThat(orderResponse.getId()).isEqualTo(order.getId()),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(order.getOrderStatus()),
                () -> assertThat(orderResponse.getOrderLineItems()).hasSize(order.getOrderLineItems().toList().size()),
                () -> assertThat(orderResponse.getOrderTableResponse().getId()).isEqualTo(order.getOrderTable().getId()),
                () -> assertThat(orderResponse.getOrderTableResponse().getNumberOfGuests()).isEqualTo(order.getOrderTable().getNumberOfGuests().toInt())
        );
    }

    public static void 주문_목록_확인됨(final List<OrderResponse> orderResponse, List<Order> orders) {
        assertThat(orderResponse).hasSize(orders.size());
    }

    public static void 주문_완료_요청_확인됨(OrderResponse orderResponse) {
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
