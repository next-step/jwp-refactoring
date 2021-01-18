package kitchenpos.order.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 비즈니스 로직을 처리하는 서비스 테스트")
@SpringBootTest
@Sql("/db/test_data.sql")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    private OrderRequest orderRequest;

    private List<OrderLineItemRequest> orderLineItemRequestList;
    private OrderLineItemRequest orderLineItemRequest;

    @BeforeEach
    void setUp() {
        orderLineItemRequestList = new ArrayList<>();

        orderLineItemRequest = new OrderLineItemRequest();
        orderLineItemRequest.setMenuId(1L);
        orderLineItemRequest.setQuantity(2L);

        orderLineItemRequestList.add(orderLineItemRequest);

        orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.COOKING.name());
        orderRequest.setOrderTableId(1L);
        orderRequest.setOrderLineItems(orderLineItemRequestList);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        final OrderResponse savedOrder = orderService.create(orderRequest);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderTableId()).isEqualTo(orderRequest.getOrderTableId());
        assertThat(savedOrder.getOrderStatus().name()).isEqualTo(orderRequest.getOrderStatus());

        assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(1);
        assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(1);
        assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(2);
    }

    @DisplayName("상품을 주문하지 않는 경우 주문을 생성할 수 없다.")
    @Test
    void 상품을_주문하지_않는_경우_주문_생성() {
        orderRequest.setOrderLineItems(null);

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 등록되지 않은 상품을 주문하는 경우 주문을 생성할 수 없다.")
    @Test
    void 등록되지_않은_상품을_주문하는_경우_주문_생성() {
        final List<OrderLineItemRequest> invalidOrderLineItemRequestList = new ArrayList<>();

        OrderLineItemRequest invalidOrderLineItemRequest = new OrderLineItemRequest();
        invalidOrderLineItemRequest.setMenuId(121L);
        invalidOrderLineItemRequest.setQuantity(2L);

        invalidOrderLineItemRequestList.add(invalidOrderLineItemRequest);

        orderRequest.setOrderLineItems(invalidOrderLineItemRequestList);

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는(비어있는) 주문 테이블을 주문하는 경우 주문을 생성할 수 없다.")
    @Test
    void 존재하지_않는_주문_테이블을_주문하는_경우_주문_생성() {
        orderRequest.setOrderTableId(1829L);

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void 주문_조회() {
        orderService.create(orderRequest);

        final List<OrderResponse> orderResponseList = orderService.getOrderList();

        assertThat(orderResponseList.size()).isGreaterThan(0);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        final OrderResponse orderResponse = orderService.create(orderRequest);

        orderRequest.setOrderStatus(OrderStatus.MEAL.name());

        final OrderResponse changedOrder = orderService.changeOrderStatus(orderResponse.getId(), orderRequest);
        assertThat(changedOrder.getOrderStatus().name()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("없는 주문의 경우 상태를 변경할 수 없다..")
    @Test
    void 주문이_존재하지_않는_경우_주문_상태_변경() {
        orderRequest.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(182891L, orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 완료된 상태인 경우 주문의 상태를 변경할 수 없다.")
    @Test
    void 주문이_완료된_상태인_경우_주문_상태_변경() {
        final OrderResponse orderResponse = orderService.create(orderRequest);

        orderRequest.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(orderResponse.getId(), orderRequest);

        orderRequest.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
