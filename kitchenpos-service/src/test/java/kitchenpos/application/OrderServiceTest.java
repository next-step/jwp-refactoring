package kitchenpos.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderValidator orderValidator;

	@InjectMocks
	private OrderService orderService;

	private OrderRequest orderRequest;
	private OrderLineItemRequest orderLineItemRequest;

	@BeforeEach
	void setUp() {
		orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
		OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1L);
		orderRequest = new OrderRequest(1L, null, Arrays.asList(orderLineItemRequest, orderLineItemRequest2));
	}

	@Test
	void orderCreateTest() {
		Order order = new Order(1L, OrderStatus.COOKING.name(), null);

		Mockito.when(orderRepository.save(order)).thenReturn(order);
		assertThat(orderService.create(this.orderRequest)).isNotNull();
	}

	@Test
	void getOrderListTest() {
		Mockito.when(orderRepository.findAll()).thenReturn(Lists.list(new Order(), new Order()));
		assertThat(orderService.list()).hasSize(2);
	}

	@Test
	void changeOrderStatusTest() {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), null);

		Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		assertThat(orderService.changeOrderStatus(1L, OrderStatus.MEAL.name()).getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}


	@Test
	@DisplayName("이미 완료되지 않은 주문 테이블이 존재하면 익셉션 발생")
	void checkProcessingOrderTest() {
		Mockito.when(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
		Mockito.when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

		Assertions.assertThatThrownBy(() -> orderService.checkProcessingOrder(1L))
				.isInstanceOf(IllegalArgumentException.class);

		Assertions.assertThatThrownBy(() -> orderService.checkProcessingOrders(Arrays.asList(1L, 2L)))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
