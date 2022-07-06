package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    public void createOrder() {
        //given
        OrderRequest order = OrderRequest.of(1l, new ArrayList<>(Arrays.asList(OrderLineItemRequest.of( 1l, 1))));
        //when
        OrderResponse result = orderService.create(order);
        //then
        assertThat(result).isNotNull();
    }


    @DisplayName("주문 항목이 빈값이면 에러")
    @Test
    public void createWithOrderLineItemEmpty() {
        //when
        //then
        OrderRequest order = OrderRequest.of(3l, Arrays.asList(OrderLineItemRequest.of( 1l, 1)));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 항목이 존재하지 않으면 에러")
    @Test
    public void createWithNoExistOrderLineItem() {
        //when
        //then
        OrderRequest order = OrderRequest.of(999l, Arrays.asList(OrderLineItemRequest.of( 1l, 1)));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목에 중복된 것이 존재하면 에러")
    @Test
    public void createWithDuplicateOrderLineItem() {
        //given

        OrderRequest order = OrderRequest.of(3l, Arrays.asList(OrderLineItemRequest.of(1l, 1), OrderLineItemRequest.of(1l, 1)));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 존재하지 않으면 에러")
    @Test
    public void createWithNoExistOrderTable() {
        //given
        OrderRequest order = OrderRequest.of(999l, Arrays.asList(OrderLineItemRequest.of( 1l, 1)));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 테이블이 빈 값이면 에러")
    @Test
    public void createWithEmptyOrderTable() {
        //given
        OrderRequest order = OrderRequest.of(999l, Arrays.asList());
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 전체를 조회한다.")
    @Test
    public void getOrders() {
        //given
        OrderRequest order = OrderRequest.of(1l, Arrays.asList(OrderLineItemRequest.of( 1l, 1)));
        orderService.create(order);
        //when
        List<OrderResponse> result = orderService.list();
        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    public void updateOrderStatus() {
        //given
        OrderRequest order = OrderRequest.of(1l, Arrays.asList(OrderLineItemRequest.of( 1l, 1)));
        OrderResponse orderResponse = orderService.create(order);

        OrderStatusRequest orderStatusRequest = OrderStatusRequest.from(OrderStatus.MEAL);
        //when
        OrderResponse result = orderService.changeOrderStatus(orderResponse.getId(), orderStatusRequest);
        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }


    @DisplayName("주문 상태가 완료이면 변경할 수 없다.")
    @Test
    public void updateWithCompleteStatus() {
        //given
        OrderRequest order = OrderRequest.of(1l, Arrays.asList(OrderLineItemRequest.of( 1l, 1)));
        OrderResponse orderResponse = orderService.create(order);
        OrderStatusRequest orderStatusRequest = OrderStatusRequest.from(OrderStatus.COMPLETION);
        orderService.changeOrderStatus(orderResponse.getId(), orderStatusRequest);
        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), OrderStatusRequest.from(OrderStatus.MEAL))).isInstanceOf(IllegalArgumentException.class);
    }




}
