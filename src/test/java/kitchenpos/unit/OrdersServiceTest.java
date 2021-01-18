package kitchenpos.unit;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OrdersServiceTest {

	@Autowired
	private OrderService orderService;

	@Test
	@DisplayName("주문을 등록한다")
	void createOrders() {
		List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2L);
		orderLineItemRequests.add(orderLineItemRequest);
		OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

		OrderResponse orderResponse = orderService.create(orderRequest);
		assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
	}

	@Test
	@DisplayName("주문을 조회한다")
	void listOrders() {
		this.createOrders();
		List<OrderResponse> orderResponses = orderService.listOrders();
		assertThat(orderResponses.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("주문의 상태를 변경할 수 있다")
	void changeOrderStatus() {
		List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2L);
		orderLineItemRequests.add(orderLineItemRequest);
		OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

		OrderResponse orderResponse = orderService.create(orderRequest);
		assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

		OrderRequest orderRequest2 = new OrderRequest(OrderStatus.MEAL.name());
		OrderResponse orderResponse2 = orderService.changeOrderStatus(orderResponse.getId(), orderRequest2);
		assertThat(orderResponse2.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@Test
	@DisplayName("계산 완료 상태의 주문이 조회될 순 없다")
	void givenOrderStatusCompletionWhenFindOrderThenError() {
		this.createOrders();
		OrderRequest orderRequest = new OrderRequest(OrderStatus.COMPLETION.name());
		assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(1L, orderRequest));
	}

}