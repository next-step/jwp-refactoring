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
	void create() {
		List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2);
		orderLineItemRequests.add(orderLineItemRequest);
		OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

		OrderResponse orderResponse = orderService.create(orderRequest);
		assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
	}

	@Test
	@DisplayName("주문을 조회한다")
	void list() {
		this.create();
		List<OrderResponse> orderResponses = orderService.list();
		assertThat(orderResponses.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("주문의 상태를 변경할 수 있다")
	void changeOrderStatus() {
		this.create();
		OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());
		OrderResponse orderResponse = orderService.changeOrderStatus(1L, orderRequest);
		assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@Test
	@DisplayName("계산 완료 상태의 주문이 조회될 순 없다")
	void givenOrderStatusCompletionWhenFindOrderThenError() {
		this.create();
		OrderRequest orderRequest = new OrderRequest(OrderStatus.COMPLETION.name());
		assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(1L, orderRequest));
	}

}