package kitchenpos.order.service;

import kitchenpos.IntegrationTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.util.OrderRequestBuilder;
import kitchenpos.table.service.OrderTableServiceJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderServiceJpa orderServiceJpa;

    @Autowired
    private OrderTableServiceJpa orderTableServiceJpa;


    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createOrder() {
        OrderResponse orderResponse = orderServiceJpa.create(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        assertThat(orderResponse.getId()).isNotNull();
    }

    @DisplayName("주문의 메뉴는 1개 이상이어야 한다.")
    @Test
    void expectedExceptionOrderInMenuGraterThanOne() {
        // when then
        assertThatThrownBy(() -> orderServiceJpa.create(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .build()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 메뉴는 1개 이상이어야 합니다.");
    }

    @DisplayName("테이블이 비어있지 않으면 주문이 불가능하다.")
    @Test
    void expectedExceptionNotEmptyTable() {
        // given
        orderTableServiceJpa.changeEmpty(1L, false);

        // when then
        assertThatThrownBy(() -> orderServiceJpa.create(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블이 비어있지 않습니다.");
    }

    @DisplayName("메뉴는 생성된 상태여야 한다.")
    @Test
    void expectedExceptionNotExistMenu() {
        // when then
        assertThatThrownBy(() -> orderServiceJpa.create(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(10L, 1)
                .build()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴를 찾을수 없습니다.");
    }

    @DisplayName("주문 목록을 가져올 수 있다.")
    @Test
    void findAllOrders() {
        orderServiceJpa.create(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        orderServiceJpa.create(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        assertThat(orderServiceJpa.list()).hasSize(2);
    }

    @DisplayName("완료된 주문 상태 변경 요청")
    @Test
    void expectedExceptionAlreadyCompleteOrderStatus() {
        // given
        long orderId = orderServiceJpa.create(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build()).getId();
        orderServiceJpa.changeOrderStatus(orderId, Order.OrderStatus.COMPLETION.name());

        // when then
        assertThatThrownBy(() -> orderServiceJpa.changeOrderStatus(orderId, Order.OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");

    }
}
