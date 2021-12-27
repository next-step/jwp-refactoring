package kitchenpos.order.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("주문 통합테스트")
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문 관리")
    public void orderManage() {
        // given
        // 주문 요청 생성
        OrderLineItemRequest 주문_메뉴_요청 = new OrderLineItemRequest(1L, 1L);
        OrderRequest 주문_요청 = new OrderRequest(9L, Collections.singletonList(주문_메뉴_요청));
        // when
        // 주문을 등록한다.
        OrderResponse savedOrder = orderService.create(주문_요청);
        // then
        // 주문이 성공적으로 등록된다.
        assertThat(savedOrder.getId()).isNotNull();

        // when
        // 주문 리스트를 조회한다.
        List<OrderResponse> savedOrders = orderService.list();

        // then
        // 주문 리스트에 등록한 주문이 포함되어있다.
        assertThat(savedOrders).contains(savedOrder);

        // given
        // 주문 상태 변경 요청 생성
        OrderStatusRequest 주문_상태_변경_요청 = new OrderStatusRequest(OrderStatus.MEAL.name());

        // when
        // 주문 상태를 변경한다.
        OrderResponse changedOrder = orderService.changeOrderStatus(savedOrder.getId(), 주문_상태_변경_요청);

        // then
        // 주문 상태가 변경된다.
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

}
