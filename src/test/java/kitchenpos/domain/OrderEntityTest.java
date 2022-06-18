package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Order 클래스 테스트")
class OrderEntityTest {

    private final List<OrderLineItemEntity> orderLineItems = Arrays.asList(new OrderLineItemEntity(1L, 1L));

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        OrderEntity order = new OrderEntity(1L);
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(order.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("테이블없이 주문을 생성한다.")
    @Test
    void failureWithOrderTable() {
        assertThatThrownBy(() -> {
            new OrderEntity(null);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("주문에 주문 항목을 추가한다.")
    @Test
    void addOrderLineItems() {
        OrderEntity order = new OrderEntity(1L);

        order.addOrderLineItems(orderLineItems);

        assertAll(
                () -> assertThat(order.getOrderLineItems()).hasSize(1),
                () -> assertThat(order.getOrderLineItems()).element(0)
                                                           .satisfies(it -> {
                                                               assertThat(it.getOrder()).isNotNull();
                                                           })
        );
    }

    @DisplayName("주문에 빈 주문 항목을 추가한다.")
    @Test
    void addOrderLimeItemsWithEmpty() {
        OrderEntity order = new OrderEntity(1L);

        assertThatThrownBy(() -> {
            order.addOrderLineItems(Collections.emptyList());
        }).isInstanceOf(EmptyOrderLineItemsException.class)
        .hasMessageContaining("주문 항목이 비었습니다.");
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        OrderEntity order = new OrderEntity(1L);

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatusWithCompleted() {
        OrderEntity order = new OrderEntity(1L);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COMPLETION);
        }).isInstanceOf(CannotChangeOrderStatusException.class)
        .hasMessageContaining("주문 상태를 변경할 수 없습니다.");
    }
}
