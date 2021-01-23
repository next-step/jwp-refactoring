package kitchenpos.order.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
            new OrderLineItemRequest(1L, 1),
            new OrderLineItemRequest(2L, 1)
        );
        OrderRequest orderRequest = new OrderRequest(1L, OrderStatus.COOKING.name(), orderLineItemRequests);

        // when
        OrderResponse savedOrder = orderService.create(orderRequest);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(2);
    }

    @DisplayName("주문 등록 예외 - 주문하고자 하는 메뉴가 등록되어 있어야 한다.")
    @Test
    void create_exception1() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
            new OrderLineItemRequest(1L, 1),
            new OrderLineItemRequest(999999L, 1)
        );
        OrderRequest orderRequest = new OrderRequest(1L, OrderStatus.COOKING.name(), orderLineItemRequests);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 등록 예외 - 주문하고자 하는 테이블이 등록되어 있어야 한다.")
    @Test
    void create_exception2() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
            new OrderLineItemRequest(1L, 1),
            new OrderLineItemRequest(2L, 1)
        );
        OrderRequest orderRequest = new OrderRequest(999999L, OrderStatus.COOKING.name(), orderLineItemRequests);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderService.create(orderRequest))
            .withMessage("테이블이 등록되어 있어야 한다.");
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
            new OrderLineItemRequest(1L, 1),
            new OrderLineItemRequest(2L, 1)
        );
        OrderRequest orderRequest = new OrderRequest(1L, OrderStatus.COOKING.name(), orderLineItemRequests);
        OrderResponse savedOrder = orderService.create(orderRequest);

        // when
        List<OrderResponse> list = orderService.list();

        // then
        assertThat(list).extracting("id").contains(savedOrder.getId());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
            new OrderLineItemRequest(1L, 1),
            new OrderLineItemRequest(2L, 1)
        );
        OrderRequest orderRequest = new OrderRequest(1L, OrderStatus.COOKING.name(), orderLineItemRequests);

        // when
        OrderResponse updatedOrder = orderService.changeOrderStatus(1L, orderRequest);

        // then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문 상태 변경 예외 - 이미 완료 상태는 변경할 수 없다.")
    @Test
    void changeOrderStatus_exception1() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
            new OrderLineItemRequest(1L, 1),
            new OrderLineItemRequest(2L, 1)
        );
        OrderRequest orderRequest = new OrderRequest(2L, OrderStatus.COOKING.name(), orderLineItemRequests);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderService.changeOrderStatus(2L, orderRequest))
            .withMessage("이미 완료 상태는 변경할 수 없다.");
    }
}
