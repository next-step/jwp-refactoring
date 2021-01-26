package kitchenpos.order.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.order.exception.InvalidChangeOrderStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends BaseServiceTest {
    @Autowired
    private OrderService orderService;

    private List<OrderLineItemRequest> orderLineItemRequests;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderLineItemRequests = Collections.singletonList(new OrderLineItemRequest(등록된_menu_id, 2));
        orderService.create(new OrderRequest(1L, 비어있지_않은_orderTable_id, orderLineItemRequests));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void createOrder() {
        OrderRequest orderRequest = new OrderRequest(2L, 비어있지_않은_orderTable_id, orderLineItemRequests);
        OrderResponse orderResponse = orderService.create(orderRequest);

        assertThat(orderResponse.getOrderTableId()).isEqualTo(비어있지_않은_orderTable_id);
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(orderResponse.getOrderedTime()).isNotNull();
        assertThat(orderResponse.getOrderLineItemResponses().get(0).getOrderId()).isEqualTo(2L);
    }

    @DisplayName("주문 항목이 하나도 없을 경우 등록할 수 없다.")
    @Test
    void createOrderException1() {
        OrderRequest orderRequest = new OrderRequest(2L, 비어있지_않은_orderTable_id, null);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyOrderLineItemsException.class)
                .hasMessage("주문 항목이 없어 등록할 수 없습니다.");
    }

    @DisplayName("선택한 주문 항목의 메뉴가 등록되어 있지 않으면 등록할 수 없다.")
    @Test
    void createOrderException2() {
        orderLineItemRequests = Collections.singletonList(new OrderLineItemRequest(등록되어_있지_않은_menu_id, 2));
        OrderRequest orderRequest = new OrderRequest(2L, 비어있지_않은_orderTable_id, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("등록되지 않은 메뉴가 있습니다.");
    }

    @DisplayName("해당 주문 테이블이 등록되어 있지 않으면 등록할 수 없다.")
    @Test
    void createOrderException3() {
        OrderRequest orderRequest = new OrderRequest(2L, 등록되어_있지_않은_orderTable_id, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("해당 주문 테이블이 등록되어 있지 않습니다.");
    }

    @DisplayName("빈 테이블일 경우 등록할 수 없다.")
    @Test
    void createOrderException4() {
        OrderRequest orderRequest = new OrderRequest(2L, 빈_orderTable_id1, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyOrderTableException.class)
                .hasMessage("빈 테이블일 경우 등록할 수 없습니다.");
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        OrderRequest changeOrder = new OrderRequest(1L, 비어있지_않은_orderTable_id, OrderStatus.MEAL, orderLineItemRequests);

        OrderResponse orderResponse = orderService.changeOrderStatus(1L, changeOrder);

        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatusException1() {
        OrderRequest changeOrder = new OrderRequest(2L, 비어있지_않은_orderTable_id, OrderStatus.COOKING, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.changeOrderStatus(2L, changeOrder))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("주문이 등록되어 있지 않습니다.");
    }

    @DisplayName("주문 상태가 계산 완료일 경우 변경할 수 없다.")
    @Test
    void changeOrderStatusException2() {
        OrderRequest order = new OrderRequest(1L, 비어있지_않은_orderTable_id, OrderStatus.COMPLETION, orderLineItemRequests);
        orderService.changeOrderStatus(1L, order);

        OrderRequest changeOrder = new OrderRequest(1L, 비어있지_않은_orderTable_id, OrderStatus.MEAL, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
                .isInstanceOf(InvalidChangeOrderStatusException.class)
                .hasMessage("주문 상태가 계산 완료일 경우 변경할 수 없습니다.");
    }
}
